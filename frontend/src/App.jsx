import { useAuth0 } from '@auth0/auth0-react'
import AppRouter from './router/AppRouter'
import './App.css'

function App() {
  const { isLoading, error } = useAuth0()

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

  return <AppRouter />
}

export default App
