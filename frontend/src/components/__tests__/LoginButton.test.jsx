import { render, screen } from '@testing-library/react'
import { describe, it, expect, vi } from 'vitest'
import LoginButton from '../LoginButton'

// Mock Auth0
vi.mock('@auth0/auth0-react', () => ({
  useAuth0: () => ({
    loginWithRedirect: vi.fn(),
  }),
}))

describe('LoginButton', () => {
  it('renders login button', () => {
    render(<LoginButton />)
    expect(screen.getByText('Log In')).toBeInTheDocument()
  })

  it('has correct CSS class', () => {
    render(<LoginButton />)
    const button = screen.getByText('Log In')
    expect(button).toHaveClass('button', 'login')
  })
})
