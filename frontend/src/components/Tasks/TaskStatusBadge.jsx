/**
 * TaskStatusBadge component displays task status with color coding
 * @param {Object} props - Component props
 * @param {string} props.status - Task status (TODO, IN_PROGRESS, DONE)
 */
export default function TaskStatusBadge({ status }) {
  const statusConfig = {
    TODO: { label: 'To Do', className: 'status-todo', icon: '📋' },
    IN_PROGRESS: { label: 'In Progress', className: 'status-in-progress', icon: '⏳' },
    DONE: { label: 'Done', className: 'status-done', icon: '✅' },
  }

  const config = statusConfig[status] || statusConfig.TODO

  return (
    <span className={`task-status-badge ${config.className}`}>
      <span className="status-icon">{config.icon}</span>
      <span className="status-label">{config.label}</span>
    </span>
  )
}
