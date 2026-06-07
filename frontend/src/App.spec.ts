import { mount, flushPromises } from '@vue/test-utils'
import { beforeEach, afterEach, describe, expect, it, vi, Mock } from 'vitest'
import App from './App.vue'

describe('App.vue', () => {
  beforeEach(() => {
    vi.stubGlobal('fetch', vi.fn())
  })

  afterEach(() => {
    vi.unstubAllGlobals()
  })

  it('submits a URL and shows the shortened result', async () => {
    (fetch as Mock).mockResolvedValueOnce({
      ok: true,
      json: async () => ({
        alias: 'abc123',
        inputUrl: 'https://example.com',
        shortUrl: 'http://localhost:8080/abc1234'
      })
    })

    const wrapper = mount(App)

    await wrapper.find('input').setValue('https://example.com')
    await wrapper.find('form').trigger('submit.prevent')
    await flushPromises()

    expect(fetch).toHaveBeenCalledWith(
      'http://localhost:8080/api/urls',
      expect.objectContaining({ method: 'POST' })
    )

    expect(wrapper.text()).toContain('abc123')
    expect(wrapper.text()).toContain('http://localhost:8080/abc1234')
    expect(wrapper.text()).toContain('https://example.com')
  })

  it('shows an error when the API fails', async () => {
    (fetch as Mock).mockResolvedValueOnce({
      ok: false,
      json: async () => ({ error: 'Invalid URL' })
    })

    const wrapper = mount(App)

    await wrapper.find('input').setValue('bad-url')
    await wrapper.find('form').trigger('submit.prevent')
    await flushPromises()

    expect(wrapper.text()).toContain('Invalid URL')
  })
})
