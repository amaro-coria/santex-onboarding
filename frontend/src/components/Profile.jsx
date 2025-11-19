import { useAuth0 } from '@auth0/auth0-react'

export default function Profile() {
  const { user, isAuthenticated } = useAuth0()

  if (!isAuthenticated) {
    return null
  }

  return (
    <div className="profile-section">
      <h2 className="profile-section-title">Profile Information</h2>
      <div className="profile-card action-card">
        {user.picture && (
          <img
            src={user.picture}
            alt={user.name}
            className="profile-picture"
          />
        )}
        <h3 className="profile-name">{user.name}</h3>
        <p className="profile-email">{user.email}</p>
      </div>
    </div>
  )
}
