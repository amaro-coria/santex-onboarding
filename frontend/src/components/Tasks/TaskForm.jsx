import { useForm } from 'react-hook-form'
import { yupResolver } from '@hookform/resolvers/yup'
import { taskSchema } from '../../utils/validationSchemas'

/**
 * TaskForm component for creating and editing tasks
 * @param {Object} props - Component props
 * @param {Object} props.initialData - Initial task data for editing
 * @param {Function} props.onSubmit - Submit handler
 * @param {Function} props.onCancel - Cancel handler
 * @param {boolean} props.isLoading - Loading state
 */
export default function TaskForm({ initialData, onSubmit, onCancel, isLoading = false }) {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    defaultValues: initialData || {
      title: '',
      description: '',
      status: 'TODO',
      dueDate: '',
      assignedUserId: null,
    },
    resolver: yupResolver(taskSchema),
  })

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="task-form">
      <div className="form-group">
        <label htmlFor="title" className="form-label">
          Title <span className="required">*</span>
        </label>
        <input
          id="title"
          type="text"
          {...register('title')}
          className={`form-input ${errors.title ? 'error' : ''}`}
          placeholder="Enter task title"
          disabled={isLoading}
        />
        {errors.title && <span className="form-error">{errors.title.message}</span>}
      </div>

      <div className="form-group">
        <label htmlFor="description" className="form-label">
          Description
        </label>
        <textarea
          id="description"
          {...register('description')}
          className={`form-textarea ${errors.description ? 'error' : ''}`}
          placeholder="Enter task description (optional)"
          rows="4"
          disabled={isLoading}
        />
        {errors.description && <span className="form-error">{errors.description.message}</span>}
      </div>

      <div className="form-row">
        <div className="form-group">
          <label htmlFor="status" className="form-label">
            Status <span className="required">*</span>
          </label>
          <select
            id="status"
            {...register('status')}
            className={`form-select ${errors.status ? 'error' : ''}`}
            disabled={isLoading}
          >
            <option value="TODO">To Do</option>
            <option value="IN_PROGRESS">In Progress</option>
            <option value="DONE">Done</option>
          </select>
          {errors.status && <span className="form-error">{errors.status.message}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="dueDate" className="form-label">
            Due Date
          </label>
          <input
            id="dueDate"
            type="date"
            {...register('dueDate')}
            className={`form-input ${errors.dueDate ? 'error' : ''}`}
            disabled={isLoading}
          />
          {errors.dueDate && <span className="form-error">{errors.dueDate.message}</span>}
        </div>
      </div>

      <div className="form-actions">
        <button
          type="button"
          onClick={onCancel}
          className="button logout"
          disabled={isLoading}
        >
          Cancel
        </button>
        <button
          type="submit"
          className="button login"
          disabled={isLoading}
        >
          {isLoading ? 'Saving...' : initialData ? 'Update Task' : 'Create Task'}
        </button>
      </div>
    </form>
  )
}
