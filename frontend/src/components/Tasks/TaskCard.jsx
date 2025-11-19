import { Link } from 'react-router-dom'
import TaskStatusBadge from './TaskStatusBadge'

/**
 * TaskCard component displays a task in card format
 * @param {Object} props - Component props
 * @param {Object} props.task - Task data
 * @param {Function} props.onDelete - Delete handler
 * @param {Function} props.onStatusChange - Status change handler
 */
export default function TaskCard({ task, onDelete, onStatusChange }) {
  const formatDate = (dateString) => {
    if (!dateString) return 'No due date'
    const date = new Date(dateString)
    return date.toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' })
  }

  const isOverdue = task.dueDate && new Date(task.dueDate) < new Date() && task.status !== 'DONE'

  return (
    <div className={`task-card ${isOverdue ? 'overdue' : ''}`}>
      <div className="task-card-header">
        <Link to={`/tasks/${task.id}`} className="task-card-title">
          {task.title}
        </Link>
        <TaskStatusBadge status={task.status} />
      </div>

      {task.description && (
        <p className="task-card-description">{task.description}</p>
      )}

      <div className="task-card-footer">
        <div className="task-card-info">
          <span className="task-card-date">
            📅 {formatDate(task.dueDate)}
          </span>
          {isOverdue && <span className="task-card-overdue">⚠️ Overdue</span>}
        </div>

        <div className="task-card-actions">
          <Link to={`/tasks/${task.id}/edit`} className="task-action-btn edit">
            Edit
          </Link>
          <button
            onClick={() => onDelete(task.id)}
            className="task-action-btn delete"
          >
            Delete
          </button>
        </div>
      </div>
    </div>
  )
}
