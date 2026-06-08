import { mount, flushPromises } from '@vue/test-utils'
import { beforeEach, afterEach, describe, expect, it, vi } from 'vitest'
import App from './App.vue'

describe('App.vue', () => {
  const mockFetch = vi.fn() as ReturnType<typeof vi.fn>

  beforeEach(() => {
    vi.stubGlobal('fetch', mockFetch)
  })

  afterEach(() => {
    vi.unstubAllGlobals()
    mockFetch.mockReset()
  })

  it('submits a URL and shows the shortened result', async () => {
    mockFetch
      .mockResolvedValueOnce({ ok: true, json: async () => [] })
      .mockResolvedValueOnce({
        ok: true,
        json: async () => ({ shortenedUrl: 'http://localhost:8080/abc1234' }),
      })
      .mockResolvedValueOnce({ ok: true, json: async () => [] })

    const wrapper = mount(App)

    await wrapper.find('input').setValue('https://example.com')
    await wrapper.find('form').trigger('submit.prevent')
    await flushPromises()

    expect(fetch).toHaveBeenCalledWith(
      'http://localhost:8080/shorten',
      expect.objectContaining({ method: 'POST' }),
    )

    expect(wrapper.text()).toContain('http://localhost:8080/abc1234')
  })

  it('shows an error when the API fails', async () => {
    mockFetch
      .mockResolvedValueOnce({ ok: true, json: async () => [] })
      .mockResolvedValueOnce({ ok: false, json: async () => ({ error: 'Invalid URL' }) })

    const wrapper = mount(App)

    await wrapper.find('input').setValue('bad-url')
    await wrapper.find('form').trigger('submit.prevent')
    await flushPromises()

    expect(wrapper.text()).toContain('Invalid URL')
  })
})
