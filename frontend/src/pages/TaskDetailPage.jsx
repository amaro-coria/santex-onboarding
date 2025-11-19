import { useParams, useNavigate, Link } from 'react-router-dom'
import { useTask } from '../hooks/useTasks'
import { useApi } from '../hooks/useApi'
import TaskStatusBadge from '../components/Tasks/TaskStatusBadge'

/**
 * TaskDetailPage displays full details of a single task
 */
export default function TaskDetailPage() {
  const { id } = useParams()
  const navigate = useNavigate()
  const { task, loading, error } = useTask(id)
  const { taskApi } = useApi()

  const handleDelete = async () => {
    if (window.confirm('Are you sure you want to delete this task?')) {
      try {
        await taskApi.delete(id)
        navigate('/tasks')
      } catch (err) {
        console.error('Error deleting task:', err)
        alert('Failed to delete task')
      }
    }
  }

  const handleStatusChange = async (newStatus) => {
    try {
      await taskApi.updateStatus(id, newStatus)
      window.location.reload()
    } catch (err) {
      console.error('Error updating task status:', err)
      alert('Failed to update task status')
    }
  }

  const formatDate = (dateString) => {
    if (!dateString) return 'Not set'
    const date = new Date(dateString)
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    })
  }

  if (loading) {
    return (
      <div className="page-container">
        <div className="loading-spinner">Loading task details...</div>
      </div>
    )
  }

  if (error) {
    return (
      <div className="page-container">
        <div className="error-message">
          <h2>Error Loading Task</h2>
          <p>{error}</p>
          <Link to="/tasks" className="button login">
            Back to Tasks
          </Link>
        </div>
      </div>
    )
  }

  if (!task) {
    return (
      <div className="page-container">
        <div className="error-message">
          <h2>Task Not Found</h2>
          <p>The task you're looking for doesn't exist.</p>
          <Link to="/tasks" className="button login">
            Back to Tasks
          </Link>
        </div>
      </div>
    )
  }

  const isOverdue = task.dueDate && new Date(task.dueDate) < new Date() && task.status !== 'DONE'

  return (
    <div className="page-container">
      <div className="task-detail-header">
        <Link to="/tasks" className="back-link">
          ← Back to Tasks
        </Link>
        <div className="task-detail-actions">
          <Link to={`/tasks/${id}/edit`} className="button login">
            Edit Task
          </Link>
          <button onClick={handleDelete} className="button logout">
            Delete Task
          </button>
        </div>
      </div>

      <div className="task-detail-card">
        <div className="task-detail-main">
          <div className="task-detail-title-section">
            <h1 className="task-detail-title">{task.title}</h1>
            <TaskStatusBadge status={task.status} />
          </div>

          {task.description && (
            <div className="task-detail-section">
              <h2 className="section-title">Description</h2>
              <p className="task-detail-description">{task.description}</p>
            </div>
          )}

          <div className="task-detail-section">
            <h2 className="section-title">Details</h2>
            <div className="task-detail-grid">
              <div className="detail-item">
                <span className="detail-label">Status:</span>
                <span className="detail-value">
                  <TaskStatusBadge status={task.status} />
                </span>
              </div>

              <div className="detail-item">
                <span className="detail-label">Due Date:</span>
                <span className={`detail-value ${isOverdue ? 'overdue-text' : ''}`}>
                  {formatDate(task.dueDate)}
                  {isOverdue && <span className="overdue-badge">⚠️ Overdue</span>}
                </span>
              </div>

              <div className="detail-item">
                <span className="detail-label">Created:</span>
                <span className="detail-value">{formatDate(task.createdAt)}</span>
              </div>

              <div className="detail-item">
                <span className="detail-label">Last Updated:</span>
                <span className="detail-value">{formatDate(task.updatedAt)}</span>
              </div>
            </div>
          </div>

          <div className="task-detail-section">
            <h2 className="section-title">Quick Status Update</h2>
            <div className="status-buttons">
              <button
                onClick={() => handleStatusChange('TODO')}
                className={`status-button ${task.status === 'TODO' ? 'active' : ''}`}
                disabled={task.status === 'TODO'}
              >
                📋 To Do
              </button>
              <button
                onClick={() => handleStatusChange('IN_PROGRESS')}
                className={`status-button ${task.status === 'IN_PROGRESS' ? 'active' : ''}`}
                disabled={task.status === 'IN_PROGRESS'}
              >
                ⏳ In Progress
              </button>
              <button
                onClick={() => handleStatusChange('DONE')}
                className={`status-button ${task.status === 'DONE' ? 'active' : ''}`}
                disabled={task.status === 'DONE'}
              >
                ✅ Done
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
