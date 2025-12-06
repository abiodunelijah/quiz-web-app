import React, { useEffect, useState } from 'react'

export default function Quizzes(){
  const [quizzes, setQuizzes] = useState([])
  const [error, setError] = useState(null)

  useEffect(()=>{
    const load = async () => {
      try {
        const token = localStorage.getItem('jwt')
        const res = await fetch('http://localhost:8080/api/quizzes', {
          headers: token ? { 'Authorization': 'Bearer ' + token } : {}
        })
        if (!res.ok) {
          setError('Failed to load quizzes: ' + res.status)
          return
        }
        const data = await res.json()
        setQuizzes(data)
      } catch (err) {
        setError(err.message)
      }
    }
    load()
  }, [])

  return (
    <div className="container">
      <h2>Quizzes</h2>
      {error && <div className="card">{error}</div>}
      {quizzes.length===0 && <div className="card">No quizzes</div>}
      {quizzes.map(q=> (
        <div key={q.id} className="card">
          <h3>{q.title}</h3>
          <p>{q.description}</p>
        </div>
      ))}
    </div>
  )
}

