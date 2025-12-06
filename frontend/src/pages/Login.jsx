import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'

export default function Login(){
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [message, setMessage] = useState(null)
  const navigate = useNavigate()

  const submit = async (e) => {
    e.preventDefault()
    setMessage(null)
    try {
      const res = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password })
      })
      const data = await res.json()
      if (res.ok && data.token) {
        localStorage.setItem('jwt', data.token)
        setMessage({ type:'success', text: 'Logged in' })
        navigate('/')
      } else {
        setMessage({ type:'error', text: data.message || 'Login failed' })
      }
    } catch (err) {
      setMessage({ type:'error', text: err.message })
    }
  }

  return (
    <div className="container">
      <h2>Login</h2>
      <form className="form" onSubmit={submit}>
        <input placeholder="Username" value={username} onChange={e=>setUsername(e.target.value)} />
        <input placeholder="Password" type="password" value={password} onChange={e=>setPassword(e.target.value)} />
        <button type="submit">Login</button>
      </form>
      {message && <div className={"card " + (message.type==='error' ? 'error' : 'success')}>{message.text}</div>}
    </div>
  )
}

