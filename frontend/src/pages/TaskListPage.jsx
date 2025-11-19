import { useState } from 'react'
import { Link } from 'react-router-dom'
import { useTasks } from '../hooks/useTasks'
import TaskCard from '../components/Tasks/TaskCard'

export default function TaskListPage() {
  const { tasks, loading, error, deleteTask, updateTaskStatus, refetch } = useTasks()
  const [filterStatus, setFilterStatus] = useState('ALL')
  const [sortBy, setSortBy] = useState('dueDate')

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this task?')) {
      try {
        await deleteTask(id)
      } catch (err) {
        alert('Failed to delete task')
      }
    }
  }

  const handleStatusChange = async (id, status) => {
    try {
      await updateTaskStatus(id, status)
    } catch (err) {
      alert('Failed to update task status')
    }
  }

  // Filter tasks by status
  const filteredTasks = tasks.filter((task) => {
    if (filterStatus === 'ALL') return true
    return task.status === filterStatus
  })

  // Sort tasks
  const sortedTasks = [...filteredTasks].sort((a, b) => {
    switch (sortBy) {
      case 'dueDate':
        if (!a.dueDate) return 1
        if (!b.dueDate) return -1
        return new Date(a.dueDate) - new Date(b.dueDate)
      case 'title':
        return a.title.localeCompare(b.title)
      case 'status':
        return a.status.localeCompare(b.status)
      case 'createdAt':
        return new Date(b.createdAt) - new Date(a.createdAt)
      default:
        return 0
    }
  })

  if (loading) {
    return (
      <div className="task-list-container">
        <div className="loading-spinner">Loading tasks...</div>
      </div>
    )
  }

  if (error) {
    return (
      <div className="task-list-container">
        <div className="error-message">
          <h2>Error Loading Tasks</h2>
          <p>{error}</p>
          <button onClick={refetch} className="button login">
            Retry
          </button>
        </div>
      </div>
    )
  }

  return (
    <div className="task-list-container">
      <div className="task-list-header">
        <h1 className="page-title">Tasks</h1>
        <Link to="/tasks/new" className="button login">
          Create New Task
        </Link>
      </div>

      {tasks.length > 0 && (
        <div className="task-list-filters">
          <div className="filter-group">
            <label htmlFor="status-filter" className="filter-label">
              Filter by Status:
            </label>
            <select
              id="status-filter"
              value={filterStatus}
              onChange={(e) => setFilterStatus(e.target.value)}
              className="filter-select"
            >
              <option value="ALL">All Tasks</option>
              <option value="TODO">To Do</option>
              <option value="IN_PROGRESS">In Progress</option>
              <option value="DONE">Done</option>
            </select>
          </div>

          <div className="filter-group">
            <label htmlFor="sort-by" className="filter-label">
              Sort by:
            </label>
            <select
              id="sort-by"
              value={sortBy}
              onChange={(e) => setSortBy(e.target.value)}
              className="filter-select"
            >
              <option value="dueDate">Due Date</option>
              <option value="title">Title</option>
              <option value="status">Status</option>
              <option value="createdAt">Created Date</option>
            </select>
          </div>
        </div>
      )}

      <div className="task-list-content">
        {sortedTasks.length === 0 ? (
          <div className="empty-state">
            <div className="empty-state-icon">📝</div>
            <h2 className="empty-state-title">
              {tasks.length === 0 ? 'No tasks yet' : 'No tasks match your filter'}
            </h2>
            <p className="empty-state-message">
              {tasks.length === 0
                ? 'Get started by creating your first task'
                : 'Try adjusting your filter settings'}
            </p>
            {tasks.length === 0 && (
              <Link to="/tasks/new" className="button login">
                Create Task
              </Link>
            )}
          </div>
        ) : (
          <div className="task-grid">
            {sortedTasks.map((task) => (
              <TaskCard
                key={task.id}
                task={task}
                onDelete={handleDelete}
                onStatusChange={handleStatusChange}
              />
            ))}
          </div>
        )}
      </div>
    </div>
  )
}
