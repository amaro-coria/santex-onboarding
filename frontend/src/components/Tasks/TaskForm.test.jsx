import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import { describe, it, expect, vi } from 'vitest'
import TaskForm from './TaskForm'

describe('TaskForm', () => {
  it('renders all form fields', () => {
    const onSubmit = vi.fn()
    const onCancel = vi.fn()

    render(<TaskForm onSubmit={onSubmit} onCancel={onCancel} />)

    expect(screen.getByLabelText(/Title/i)).toBeInTheDocument()
    expect(screen.getByLabelText(/Description/i)).toBeInTheDocument()
    expect(screen.getByLabelText(/Status/i)).toBeInTheDocument()
    expect(screen.getByLabelText(/Due Date/i)).toBeInTheDocument()
  })

  it('displays Create Task button when no initial data', () => {
    const onSubmit = vi.fn()
    const onCancel = vi.fn()

    render(<TaskForm onSubmit={onSubmit} onCancel={onCancel} />)

    expect(screen.getByText('Create Task')).toBeInTheDocument()
  })

  it('displays Update Task button when initial data provided', () => {
    const onSubmit = vi.fn()
    const onCancel = vi.fn()
    const initialData = {
      title: 'Test Task',
      description: 'Test Description',
      status: 'TODO',
      dueDate: '2025-12-31'
    }

    render(<TaskForm initialData={initialData} onSubmit={onSubmit} onCancel={onCancel} />)

    expect(screen.getByText('Update Task')).toBeInTheDocument()
  })

  it('pre-fills form with initial data', () => {
    const onSubmit = vi.fn()
    const onCancel = vi.fn()
    const initialData = {
      title: 'Test Task',
      description: 'Test Description',
      status: 'IN_PROGRESS',
      dueDate: '2025-12-31'
    }

    render(<TaskForm initialData={initialData} onSubmit={onSubmit} onCancel={onCancel} />)

    expect(screen.getByDisplayValue('Test Task')).toBeInTheDocument()
    expect(screen.getByDisplayValue('Test Description')).toBeInTheDocument()

    const statusSelect = screen.getByLabelText(/Status/i)
    expect(statusSelect.value).toBe('IN_PROGRESS')
  })

  it('calls onCancel when cancel button is clicked', () => {
    const onSubmit = vi.fn()
    const onCancel = vi.fn()

    render(<TaskForm onSubmit={onSubmit} onCancel={onCancel} />)

    const cancelButton = screen.getByText('Cancel')
    fireEvent.click(cancelButton)

    expect(onCancel).toHaveBeenCalled()
  })

  it('shows validation error when title is too short', async () => {
    const onSubmit = vi.fn()
    const onCancel = vi.fn()

    render(<TaskForm onSubmit={onSubmit} onCancel={onCancel} />)

    const titleInput = screen.getByLabelText(/Title/i)
    const submitButton = screen.getByText('Create Task')

    fireEvent.change(titleInput, { target: { value: 'AB' } })
    fireEvent.click(submitButton)

    await waitFor(() => {
      expect(screen.getByText(/Title must be at least 3 characters/i)).toBeInTheDocument()
    })

    expect(onSubmit).not.toHaveBeenCalled()
  })

  it('shows validation error when title is missing', async () => {
    const onSubmit = vi.fn()
    const onCancel = vi.fn()

    render(<TaskForm onSubmit={onSubmit} onCancel={onCancel} />)

    const submitButton = screen.getByText('Create Task')
    fireEvent.click(submitButton)

    await waitFor(() => {
      expect(screen.getByText(/Title is required/i)).toBeInTheDocument()
    })

    expect(onSubmit).not.toHaveBeenCalled()
  })

  it('renders all status options', () => {
    const onSubmit = vi.fn()
    const onCancel = vi.fn()

    render(<TaskForm onSubmit={onSubmit} onCancel={onCancel} />)

    const statusSelect = screen.getByLabelText(/Status/i)
    expect(statusSelect.querySelectorAll('option')).toHaveLength(3)
    expect(screen.getByRole('option', { name: 'To Do' })).toBeInTheDocument()
    expect(screen.getByRole('option', { name: 'In Progress' })).toBeInTheDocument()
    expect(screen.getByRole('option', { name: 'Done' })).toBeInTheDocument()
  })

  it('disables form fields when loading', () => {
    const onSubmit = vi.fn()
    const onCancel = vi.fn()

    render(<TaskForm onSubmit={onSubmit} onCancel={onCancel} isLoading={true} />)

    expect(screen.getByLabelText(/Title/i)).toBeDisabled()
    expect(screen.getByLabelText(/Description/i)).toBeDisabled()
    expect(screen.getByLabelText(/Status/i)).toBeDisabled()
    expect(screen.getByLabelText(/Due Date/i)).toBeDisabled()
  })

  it('shows Saving... text when loading', () => {
    const onSubmit = vi.fn()
    const onCancel = vi.fn()

    render(<TaskForm onSubmit={onSubmit} onCancel={onCancel} isLoading={true} />)

    expect(screen.getByText('Saving...')).toBeInTheDocument()
  })
})
