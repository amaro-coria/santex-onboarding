import { useAuth0 } from '@auth0/auth0-react'
import { useState } from 'react'
import LoginButton from './components/LoginButton'
import LogoutButton from './components/LogoutButton'
import Profile from './components/Profile'
import { useApi } from './hooks/useApi'
import './App.css'

function App() {
  const { isLoading, error, isAuthenticated } = useAuth0()
  const { testApi } = useApi()
  const [apiMessage, setApiMessage] = useState('')
  const [apiError, setApiError] = useState('')

  const testBackendConnection = async () => {
    try {
      setApiError('')
      setApiMessage('Testing connection...')
      const response = await testApi.hello()
      setApiMessage(response.data)
    } catch (err) {
      setApiError(`Error: ${err.message}`)
      setApiMessage('')
    }
  }

  if (isLoading) {
    return (
      <div className="app-container">
        <div className="loading-state">
          <p className="loading-text">Loading...</p>
        </div>
      </div>
    )
  }

  if (error) {
    return (
      <div className="app-container">
        <div className="error-state">
          <h1 className="error-title">Authentication Error</h1>
          <p className="error-message">{error.message}</p>
          <p className="error-sub-message">Please try again or contact support if the problem persists.</p>
        </div>
      </div>
    )
  }

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
          {!isAuthenticated ? (
            <>
              <p className="action-text">
                Welcome! Please log in to access your tasks and manage your account.
              </p>
              <LoginButton />
            </>
          ) : (
            <div className="logged-in-section">
              <p className="logged-in-message">✓ Successfully Authenticated</p>
              <Profile />
              <p className="action-text">You are now logged in and can access all features.</p>

              <div style={{ margin: '1.5rem 0' }}>
                <button onClick={testBackendConnection} className="button login">
                  Test Backend Connection
                </button>
                {apiMessage && (
                  <p className="logged-in-message" style={{ marginTop: '1rem', fontSize: '0.9rem' }}>
                    {apiMessage}
                  </p>
                )}
                {apiError && (
                  <p className="error-message" style={{ marginTop: '1rem', fontSize: '0.9rem' }}>
                    {apiError}
                  </p>
                )}
              </div>

              <LogoutButton />
            </div>
          )}
        </div>
      </div>
    </div>
  )
}

export default App
