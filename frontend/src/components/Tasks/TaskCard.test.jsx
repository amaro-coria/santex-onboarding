import { render, screen, fireEvent } from '@testing-library/react'
import { describe, it, expect, vi } from 'vitest'
import { BrowserRouter } from 'react-router-dom'
import TaskCard from './TaskCard'

const mockTask = {
  id: 1,
  title: 'Test Task',
  description: 'This is a test task description',
  status: 'TODO',
  dueDate: '2025-12-31',
  createdAt: '2025-01-01',
  updatedAt: '2025-01-01'
}

const renderWithRouter = (component) => {
  return render(<BrowserRouter>{component}</BrowserRouter>)
}

describe('TaskCard', () => {
  it('renders task information correctly', () => {
    const onDelete = vi.fn()
    const onStatusChange = vi.fn()

    renderWithRouter(
      <TaskCard task={mockTask} onDelete={onDelete} onStatusChange={onStatusChange} />
    )

    expect(screen.getByText('Test Task')).toBeInTheDocument()
    expect(screen.getByText('This is a test task description')).toBeInTheDocument()
  })

  it('displays task status badge', () => {
    const onDelete = vi.fn()
    const onStatusChange = vi.fn()

    renderWithRouter(
      <TaskCard task={mockTask} onDelete={onDelete} onStatusChange={onStatusChange} />
    )

    expect(screen.getByText('To Do')).toBeInTheDocument()
  })

  it('displays due date', () => {
    const onDelete = vi.fn()
    const onStatusChange = vi.fn()

    renderWithRouter(
      <TaskCard task={mockTask} onDelete={onDelete} onStatusChange={onStatusChange} />
    )

    expect(screen.getByText(/2025/)).toBeInTheDocument()
  })

  it('shows overdue warning for past due tasks', () => {
    const overdueTask = {
      ...mockTask,
      dueDate: '2020-01-01',
      status: 'TODO'
    }
    const onDelete = vi.fn()
    const onStatusChange = vi.fn()

    renderWithRouter(
      <TaskCard task={overdueTask} onDelete={onDelete} onStatusChange={onStatusChange} />
    )

    expect(screen.getByText(/Overdue/)).toBeInTheDocument()
  })

  it('does not show overdue warning for completed tasks', () => {
    const completedTask = {
      ...mockTask,
      dueDate: '2020-01-01',
      status: 'DONE'
    }
    const onDelete = vi.fn()
    const onStatusChange = vi.fn()

    renderWithRouter(
      <TaskCard task={completedTask} onDelete={onDelete} onStatusChange={onStatusChange} />
    )

    expect(screen.queryByText(/Overdue/)).not.toBeInTheDocument()
  })

  it('renders Edit and Delete buttons', () => {
    const onDelete = vi.fn()
    const onStatusChange = vi.fn()

    renderWithRouter(
      <TaskCard task={mockTask} onDelete={onDelete} onStatusChange={onStatusChange} />
    )

    expect(screen.getByText('Edit')).toBeInTheDocument()
    expect(screen.getByText('Delete')).toBeInTheDocument()
  })

  it('calls onDelete when delete button is clicked', () => {
    const onDelete = vi.fn()
    const onStatusChange = vi.fn()

    renderWithRouter(
      <TaskCard task={mockTask} onDelete={onDelete} onStatusChange={onStatusChange} />
    )

    const deleteButton = screen.getByText('Delete')
    fireEvent.click(deleteButton)

    expect(onDelete).toHaveBeenCalledWith(1)
  })

  it('displays "No due date" when dueDate is null', () => {
    const taskWithoutDueDate = {
      ...mockTask,
      dueDate: null
    }
    const onDelete = vi.fn()
    const onStatusChange = vi.fn()

    renderWithRouter(
      <TaskCard task={taskWithoutDueDate} onDelete={onDelete} onStatusChange={onStatusChange} />
    )

    expect(screen.getByText(/No due date/i)).toBeInTheDocument()
  })

  it('renders task title as a link to task detail page', () => {
    const onDelete = vi.fn()
    const onStatusChange = vi.fn()

    renderWithRouter(
      <TaskCard task={mockTask} onDelete={onDelete} onStatusChange={onStatusChange} />
    )

    const titleLink = screen.getByText('Test Task')
    expect(titleLink).toHaveAttribute('href', '/tasks/1')
  })
})
