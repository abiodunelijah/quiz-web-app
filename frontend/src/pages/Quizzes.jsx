import React, { useEffect, useState } from 'react'

export default function Quizzes() {
    const [quizzes, setQuizzes] = useState([])
    const [error, setError] = useState(null)
    const [loading, setLoading] = useState(true)

    useEffect(() => {
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
            } finally {
                setLoading(false)
            }
        }
        load()
    }, [])

    if (loading) {
        return (
            <div className="container">
                <div className="loading">
                    <div className="spinner"></div>
                </div>
            </div>
        )
    }

    return (
        <div className="container">
            <div className="page-header">
                <h1 className="page-title">Available Quizzes</h1>
                <p className="page-subtitle">Test your knowledge with our collection of quizzes</p>
            </div>

            {error && (
                <div className="message error">
                    <span>⚠</span>
                    <span>{error}</span>
                </div>
            )}

            {!error && quizzes.length === 0 && (
                <div className="empty-state">
                    <div className="empty-state-icon">📝</div>
                    <h3>No quizzes available</h3>
                    <p>Check back later for new quizzes!</p>
                </div>
            )}

            {quizzes.length > 0 && (
                <div className="quiz-grid">
                    {quizzes.map(q => (
                        <div key={q.id} className="card">
                            <h3>{q.title}</h3>
                            <p>{q.description}</p>
                            {(q.questionCount || q.difficulty || q.category) && (
                                <div className="card-meta">
                                    {q.questionCount && <span>📊 {q.questionCount} questions</span>}
                                    {q.difficulty && <span>⚡ {q.difficulty}</span>}
                                    {q.category && <span>🏷️ {q.category}</span>}
                                </div>
                            )}
                        </div>
                    ))}
                </div>
            )}
        </div>
    )
}