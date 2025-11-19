import { useAuth0 } from '@auth0/auth0-react'
import Profile from '../components/Profile'

export default function ProfilePage() {
  const { user } = useAuth0()

  return (
    <div className="profile-page-container">
      <div className="profile-page-header">
        <h1 className="page-title">Profile</h1>
        <p className="page-subtitle">Manage your account settings</p>
      </div>

      <div className="profile-page-content">
        <Profile />

        <div className="profile-details-card action-card" style={{ marginTop: '2rem' }}>
          <h3 className="profile-section-title">Account Information</h3>
          <div className="profile-detail-item">
            <span className="profile-detail-label">Email:</span>
            <span className="profile-detail-value">{user?.email}</span>
          </div>
          <div className="profile-detail-item">
            <span className="profile-detail-label">Email Verified:</span>
            <span className="profile-detail-value">
              {user?.email_verified ? '✅ Yes' : '❌ No'}
            </span>
          </div>
          <div className="profile-detail-item">
            <span className="profile-detail-label">User ID:</span>
            <span className="profile-detail-value">{user?.sub}</span>
          </div>
        </div>
      </div>
    </div>
  )
}
