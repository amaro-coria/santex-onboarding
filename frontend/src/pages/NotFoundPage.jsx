import { Link } from 'react-router-dom'

export default function NotFoundPage() {
  return (
    <div className="app-container">
      <div className="main-card-wrapper">
        <div className="error-state">
          <h1 className="error-title" style={{ fontSize: '4rem', marginBottom: '1rem' }}>
            404
          </h1>
          <h2 className="error-title">Page Not Found</h2>
          <p className="error-message">
            The page you're looking for doesn't exist.
          </p>
          <Link to="/" className="button login" style={{ marginTop: '2rem', display: 'inline-block' }}>
            Go Home
          </Link>
        </div>
      </div>
    </div>
  )
}
