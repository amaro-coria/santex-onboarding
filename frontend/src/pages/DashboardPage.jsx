import { useAuth0 } from '@auth0/auth0-react'
import { Link } from 'react-router-dom'
import { useState, useEffect } from 'react'
import { useApi } from '../hooks/useApi'
import Profile from '../components/Profile'

export default function DashboardPage() {
  const { user } = useAuth0()
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

  return (
    <div className="dashboard-container">
      <div className="dashboard-header">
        <h1 className="dashboard-title">Welcome back, {user?.name}!</h1>
        <p className="dashboard-subtitle">Here's what's happening with your tasks today</p>
      </div>

      <div className="dashboard-content">
        <div className="dashboard-grid">
          <div className="stat-card">
            <div className="stat-card-icon">📋</div>
            <div className="stat-card-content">
              <h3 className="stat-card-value">0</h3>
              <p className="stat-card-label">Total Tasks</p>
            </div>
          </div>

          <div className="stat-card">
            <div className="stat-card-icon">⏳</div>
            <div className="stat-card-content">
              <h3 className="stat-card-value">0</h3>
              <p className="stat-card-label">In Progress</p>
            </div>
          </div>

          <div className="stat-card">
            <div className="stat-card-icon">✅</div>
            <div className="stat-card-content">
              <h3 className="stat-card-value">0</h3>
              <p className="stat-card-label">Completed</p>
            </div>
          </div>

          <div className="stat-card">
            <div className="stat-card-icon">🔴</div>
            <div className="stat-card-content">
              <h3 className="stat-card-value">0</h3>
              <p className="stat-card-label">Overdue</p>
            </div>
          </div>
        </div>

        <div className="dashboard-actions">
          <Link to="/tasks" className="dashboard-action-button">
            View All Tasks
          </Link>
          <button onClick={testBackendConnection} className="dashboard-action-button secondary">
            Test Backend Connection
          </button>
        </div>

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

        <Profile />
      </div>
    </div>
  )
}
