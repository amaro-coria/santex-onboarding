import { Link } from 'react-router-dom'
import { useAuth0 } from '@auth0/auth0-react'
import LogoutButton from '../LogoutButton'

export default function Navbar() {
  const { user, isAuthenticated } = useAuth0()

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <Link to="/" className="navbar-logo">
          <img
            src="https://cdn.auth0.com/blog/auth0-react-sample/assets/logo.png"
            alt="Auth0"
            className="navbar-logo-img"
          />
          <span className="navbar-title">Santex Onboarding</span>
        </Link>

        {isAuthenticated && (
          <div className="navbar-menu">
            <Link to="/dashboard" className="navbar-link">
              Dashboard
            </Link>
            <Link to="/tasks" className="navbar-link">
              Tasks
            </Link>
            <Link to="/profile" className="navbar-link">
              Profile
            </Link>
            <div className="navbar-user">
              {user?.picture && (
                <img
                  src={user.picture}
                  alt={user.name}
                  className="navbar-user-avatar"
                />
              )}
              <span className="navbar-user-name">{user?.name}</span>
              <LogoutButton />
            </div>
          </div>
        )}
      </div>
    </nav>
  )
}
