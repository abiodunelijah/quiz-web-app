# Quiz Web App - Frontend

This is a minimal React + Vite frontend for the Quiz Web App. It provides registration, login, and a protected quizzes list that consumes the backend's API.

Prerequisites
- Node.js 18+ and npm installed

Install and run (development)

```bash
cd frontend
npm install
npm run dev
```

Notes
- The dev server runs on http://localhost:3000 and proxies `/api` requests to the backend at http://localhost:8080 (configure `vite.config.js` if your backend runs elsewhere).
- JWT is stored in `localStorage` under the `jwt` key after login.

