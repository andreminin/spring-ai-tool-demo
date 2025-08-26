// src/api.js
export async function askAI(message) {
  const res = await fetch('/api/ai/ask', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ message })
  })

  if (!res.ok) throw new Error(`Request failed: ${res.status}`)

  return res.text()  // backend returns plain text
}


