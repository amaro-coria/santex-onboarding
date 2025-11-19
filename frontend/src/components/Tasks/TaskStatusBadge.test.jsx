import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import TaskStatusBadge from './TaskStatusBadge'

describe('TaskStatusBadge', () => {
  it('renders TODO status correctly', () => {
    render(<TaskStatusBadge status="TODO" />)
    expect(screen.getByText('To Do')).toBeInTheDocument()
    expect(screen.getByText('📋')).toBeInTheDocument()
  })

  it('renders IN_PROGRESS status correctly', () => {
    render(<TaskStatusBadge status="IN_PROGRESS" />)
    expect(screen.getByText('In Progress')).toBeInTheDocument()
    expect(screen.getByText('⏳')).toBeInTheDocument()
  })

  it('renders DONE status correctly', () => {
    render(<TaskStatusBadge status="DONE" />)
    expect(screen.getByText('Done')).toBeInTheDocument()
    expect(screen.getByText('✅')).toBeInTheDocument()
  })

  it('applies correct CSS class for TODO status', () => {
    const { container } = render(<TaskStatusBadge status="TODO" />)
    const badge = container.querySelector('.task-status-badge')
    expect(badge).toHaveClass('status-todo')
  })

  it('applies correct CSS class for IN_PROGRESS status', () => {
    const { container } = render(<TaskStatusBadge status="IN_PROGRESS" />)
    const badge = container.querySelector('.task-status-badge')
    expect(badge).toHaveClass('status-in-progress')
  })

  it('applies correct CSS class for DONE status', () => {
    const { container } = render(<TaskStatusBadge status="DONE" />)
    const badge = container.querySelector('.task-status-badge')
    expect(badge).toHaveClass('status-done')
  })

  it('defaults to TODO for invalid status', () => {
    render(<TaskStatusBadge status="INVALID" />)
    expect(screen.getByText('To Do')).toBeInTheDocument()
  })
})
