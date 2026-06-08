export async function shortenUrl(fullUrl: string) {
  const response = await fetch('http://localhost:8080/shorten', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ fullUrl }),
  })

  const data = await response.json()

  if (!response.ok) {
    throw new Error(data.error || 'Failed to shorten URL')
  }

  return data
}

export async function listUrls() {
  const response = await fetch('http://localhost:8080/urls')
  return response.json()
}
