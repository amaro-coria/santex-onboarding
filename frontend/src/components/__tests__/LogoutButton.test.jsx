import { render, screen } from '@testing-library/react'
import { describe, it, expect, vi } from 'vitest'
import LogoutButton from '../LogoutButton'

// Mock Auth0
vi.mock('@auth0/auth0-react', () => ({
  useAuth0: () => ({
    logout: vi.fn(),
  }),
}))

describe('LogoutButton', () => {
  it('renders logout button', () => {
    render(<LogoutButton />)
    expect(screen.getByText('Log Out')).toBeInTheDocument()
  })

  it('has correct CSS class', () => {
    render(<LogoutButton />)
    const button = screen.getByText('Log Out')
    expect(button).toHaveClass('button', 'logout')
  })
})
