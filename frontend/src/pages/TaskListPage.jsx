import { Link } from 'react-router-dom'

export default function TaskListPage() {
  return (
    <div className="task-list-container">
      <div className="task-list-header">
        <h1 className="page-title">Tasks</h1>
        <Link to="/tasks/new" className="button login">
          Create New Task
        </Link>
      </div>

      <div className="task-list-content">
        <div className="empty-state">
          <div className="empty-state-icon">📝</div>
          <h2 className="empty-state-title">No tasks yet</h2>
          <p className="empty-state-message">
            Get started by creating your first task
          </p>
          <Link to="/tasks/new" className="button login">
            Create Task
          </Link>
        </div>
      </div>
    </div>
  )
}
