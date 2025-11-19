import { useAuth0 } from '@auth0/auth0-react'
import { useNavigate } from 'react-router-dom'
import { useEffect } from 'react'
import LoginButton from '../components/LoginButton'

export default function HomePage() {
  const { isAuthenticated } = useAuth0()
  const navigate = useNavigate()

  useEffect(() => {
    if (isAuthenticated) {
      navigate('/dashboard')
    }
  }, [isAuthenticated, navigate])

  return (
    <div className="app-container">
      <div className="main-card-wrapper">
        <img
          src="https://cdn.auth0.com/blog/auth0-react-sample/assets/logo.png"
          alt="Auth0"
          className="auth0-logo"
        />

        <h1 className="main-title">Santex Onboarding</h1>

        <div className="action-card">
          <p className="action-text">
            Welcome! Please log in to access your tasks and manage your account.
          </p>
          <LoginButton />
        </div>
      </div>
    </div>
  )
}
