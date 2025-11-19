import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import Footer from '../Footer'

describe('Footer', () => {
  it('renders copyright text with current year', () => {
    render(<Footer />)
    const currentYear = new Date().getFullYear()
    expect(screen.getByText(new RegExp(currentYear.toString()))).toBeInTheDocument()
  })

  it('renders GitHub link', () => {
    render(<Footer />)
    const githubLink = screen.getByText('GitHub')
    expect(githubLink).toBeInTheDocument()
    expect(githubLink).toHaveAttribute('href', 'https://github.com')
  })

  it('renders API Docs link', () => {
    render(<Footer />)
    const apiDocsLink = screen.getByText('API Docs')
    expect(apiDocsLink).toBeInTheDocument()
    expect(apiDocsLink).toHaveAttribute('href', 'http://localhost:8080/swagger-ui.html')
  })

  it('opens external links in new tab', () => {
    render(<Footer />)
    const links = screen.getAllByRole('link')
    links.forEach(link => {
      expect(link).toHaveAttribute('target', '_blank')
      expect(link).toHaveAttribute('rel', 'noopener noreferrer')
    })
  })
})
