import { describe, expect, it, vi } from 'vitest'
import { shortenUrl } from './urlService'

describe('urlService', () => {
  it('calls backend endpoint', async () => {
    const mockResponse = {
      alias: 'abc123',
      shortUrl: 'http://localhost:8080/abc123'
    }

    const fetchMock = vi.fn().mockResolvedValue({
      ok: true,
      json: vi.fn().mockResolvedValue(mockResponse)
    })

    globalThis.fetch = fetchMock as unknown as typeof fetch

    const result = await shortenUrl('https://example.com')

    expect(fetch).toHaveBeenCalledTimes(1)
    expect(result.alias).toBe('abc123')
  })
})