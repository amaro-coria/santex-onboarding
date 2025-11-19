import { useAuth0 } from '@auth0/auth0-react'
import { Link } from 'react-router-dom'
import { useMemo } from 'react'
import { useTasks } from '../hooks/useTasks'
import TaskCard from '../components/Tasks/TaskCard'
import Profile from '../components/Profile'

export default function DashboardPage() {
  const { user } = useAuth0()
  const { tasks, loading, error, deleteTask, updateTaskStatus } = useTasks()

  // Calculate statistics
  const stats = useMemo(() => {
    const total = tasks.length
    const inProgress = tasks.filter((task) => task.status === 'IN_PROGRESS').length
    const completed = tasks.filter((task) => task.status === 'DONE').length
    const overdue = tasks.filter(
      (task) =>
        task.dueDate &&
        new Date(task.dueDate) < new Date() &&
        task.status !== 'DONE'
    ).length

    return { total, inProgress, completed, overdue }
  }, [tasks])

  // Get upcoming tasks (next 7 days, not done)
  const upcomingTasks = useMemo(() => {
    const now = new Date()
    const nextWeek = new Date(now.getTime() + 7 * 24 * 60 * 60 * 1000)

    return tasks
      .filter((task) => {
        if (!task.dueDate || task.status === 'DONE') return false
        const dueDate = new Date(task.dueDate)
        return dueDate >= now && dueDate <= nextWeek
      })
      .sort((a, b) => new Date(a.dueDate) - new Date(b.dueDate))
      .slice(0, 3)
  }, [tasks])

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

  if (loading) {
    return (
      <div className="dashboard-container">
        <div className="loading-spinner">Loading dashboard...</div>
      </div>
    )
  }

  if (error) {
    return (
      <div className="dashboard-container">
        <div className="error-message">
          <h2>Error Loading Dashboard</h2>
          <p>{error}</p>
        </div>
      </div>
    )
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
              <h3 className="stat-card-value">{stats.total}</h3>
              <p className="stat-card-label">Total Tasks</p>
            </div>
          </div>

          <div className="stat-card">
            <div className="stat-card-icon">⏳</div>
            <div className="stat-card-content">
              <h3 className="stat-card-value">{stats.inProgress}</h3>
              <p className="stat-card-label">In Progress</p>
            </div>
          </div>

          <div className="stat-card">
            <div className="stat-card-icon">✅</div>
            <div className="stat-card-content">
              <h3 className="stat-card-value">{stats.completed}</h3>
              <p className="stat-card-label">Completed</p>
            </div>
          </div>

          <div className="stat-card">
            <div className="stat-card-icon">🔴</div>
            <div className="stat-card-content">
              <h3 className="stat-card-value">{stats.overdue}</h3>
              <p className="stat-card-label">Overdue</p>
            </div>
          </div>
        </div>

        {upcomingTasks.length > 0 && (
          <div className="dashboard-section">
            <h2 className="section-title">Upcoming Tasks (Next 7 Days)</h2>
            <div className="task-grid">
              {upcomingTasks.map((task) => (
                <TaskCard
                  key={task.id}
                  task={task}
                  onDelete={handleDelete}
                  onStatusChange={handleStatusChange}
                />
              ))}
            </div>
          </div>
        )}

        <div className="dashboard-actions">
          <Link to="/tasks" className="dashboard-action-button">
            View All Tasks
          </Link>
          <Link to="/tasks/new" className="dashboard-action-button secondary">
            Create New Task
          </Link>
        </div>

        <Profile />
      </div>
    </div>
  )
}
