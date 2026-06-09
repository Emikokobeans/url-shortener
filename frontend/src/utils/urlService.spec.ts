import { describe, expect, it, vi } from 'vitest'
import { deleteUrl, listUrls, shortenUrl } from './urlService'

describe('urlService', () => {
  it('posts to /shorten', async () => {
    globalThis.fetch = vi.fn(() =>
      Promise.resolve({
        ok: true,
        json: () => Promise.resolve({ shortUrl: 'http://localhost:8080/x' }),
      } as Response),
    )

    await shortenUrl('https://example.com')

    expect(fetch).toHaveBeenCalledWith(
      'http://localhost:8080/shorten',
      expect.objectContaining({ method: 'POST' }),
    )
  })

  it('calls list endpoint', async () => {
    globalThis.fetch = vi.fn(() =>
      Promise.resolve({ ok: true, json: () => Promise.resolve([]) } as Response),
    )

    await listUrls()

    expect(fetch).toHaveBeenCalledWith('http://localhost:8080/urls')
  })

  it('deletes an alias', async () => {
    globalThis.fetch = vi.fn(() => Promise.resolve({ ok: true } as Response))

    await deleteUrl('dummy-alias')

    expect(fetch).toHaveBeenCalledWith('http://localhost:8080/dummy-alias', { method: 'DELETE' })
  })
})