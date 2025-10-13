import { useState, useEffect } from 'react'
import axios from 'axios'
import './App.css'

function App() {
  const [message, setMessage] = useState('')
  const [timestamp, setTimestamp] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    fetchMessage()
  }, [])

  const fetchMessage = async () => {
    try {
      setLoading(true)
      setError(null)
      
      // Use the backend URL based on environment
      const baseURL = import.meta.env.VITE_API_URL || 'http://localhost:8080'
      const response = await axios.get(`${baseURL}/api/hello`)
      
      setMessage(response.data.message)
      setTimestamp(response.data.timestamp)
    } catch (err) {
      setError('Failed to fetch message from backend: ' + err.message)
      console.error('Error fetching message:', err)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="App">
      <div className="container">
        <h1>🚀 Spring Boot + React</h1>
        <p className="subtitle">Full-Stack Application Demo</p>
        
        <div className="card">
          {loading && <p className="loading">Loading...</p>}
          
          {error && (
            <div className="error">
              <p>{error}</p>
              <button onClick={fetchMessage} className="retry-btn">
                Retry
              </button>
            </div>
          )}
          
          {!loading && !error && (
            <div className="success">
              <h2>✅ Backend Response</h2>
              <p className="message">{message}</p>
              <p className="timestamp">
                <strong>Timestamp:</strong> {new Date(timestamp).toLocaleString()}
              </p>
              <button onClick={fetchMessage} className="refresh-btn">
                Refresh
              </button>
            </div>
          )}
        </div>

        <div className="info">
          <h3>Tech Stack</h3>
          <ul>
            <li><strong>Backend:</strong> Spring Boot 3.2.0 (Java 17)</li>
            <li><strong>Frontend:</strong> React 18 + Vite</li>
            <li><strong>HTTP Client:</strong> Axios</li>
            <li><strong>Containerization:</strong> Docker + Docker Compose</li>
          </ul>
        </div>
      </div>
    </div>
  )
}

export default App
