import { useState, useEffect } from 'react'
import { useParams, useNavigate, Link } from 'react-router-dom'
import { useTask } from '../hooks/useTasks'
import { useApi } from '../hooks/useApi'
import TaskForm from '../components/Tasks/TaskForm'

/**
 * TaskFormPage handles both creating new tasks and editing existing ones
 */
export default function TaskFormPage() {
  const { id } = useParams()
  const navigate = useNavigate()
  const isEditMode = Boolean(id)
  const { task, loading: taskLoading } = useTask(id)
  const { taskApi } = useApi()
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [error, setError] = useState(null)

  const handleSubmit = async (formData) => {
    setIsSubmitting(true)
    setError(null)

    try {
      if (isEditMode) {
        await taskApi.update(id, formData)
      } else {
        await taskApi.create(formData)
      }
      navigate('/tasks')
    } catch (err) {
      console.error('Error saving task:', err)
      setError(err.response?.data?.message || 'Failed to save task')
      setIsSubmitting(false)
    }
  }

  const handleCancel = () => {
    navigate(isEditMode ? `/tasks/${id}` : '/tasks')
  }

  if (isEditMode && taskLoading) {
    return (
      <div className="page-container">
        <div className="loading-spinner">Loading task...</div>
      </div>
    )
  }

  if (isEditMode && !task) {
    return (
      <div className="page-container">
        <div className="error-message">
          <h2>Task Not Found</h2>
          <p>The task you're trying to edit doesn't exist.</p>
          <Link to="/tasks" className="button login">
            Back to Tasks
          </Link>
        </div>
      </div>
    )
  }

  return (
    <div className="page-container">
      <div className="form-page-header">
        <h1>{isEditMode ? 'Edit Task' : 'Create New Task'}</h1>
        <p className="form-page-subtitle">
          {isEditMode
            ? 'Update the task details below'
            : 'Fill in the details to create a new task'}
        </p>
      </div>

      {error && (
        <div className="error-message">
          <p>{error}</p>
        </div>
      )}

      <div className="form-page-card">
        <TaskForm
          initialData={isEditMode ? task : null}
          onSubmit={handleSubmit}
          onCancel={handleCancel}
          isLoading={isSubmitting}
        />
      </div>
    </div>
  )
}
