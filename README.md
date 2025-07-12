# 🔁 Skill Swap Platform

A full-stack web application that enables users to **offer and request skills** in exchange — creating a community of learners and doers.

Built with **React**, **Spring Boot**, **Tailwind CSS**, and **Vite**.

---

## 🚀 Features

### 👤 User Profile
- Name, location (optional), and profile photo (optional)
- List of **skills offered** and **skills wanted**
- Set availability (weekends, evenings, etc.)
- Choose to make profile **public or private**

### 🔍 Browse & Discover
- Browse and search users by skill (e.g., "Photoshop", "Excel")
- View skill match recommendations

### 🤝 Swap Requests
- Send and receive swap offers
- Accept or reject swap requests
- View current and pending swaps
- Delete a pending swap request if not accepted
- Leave feedback or ratings after a swap

### 🛡️ Admin Panel
- Review and reject inappropriate skill descriptions
- Ban users violating platform policies
- Monitor pending, accepted, or cancelled swaps
- Send platform-wide messages (feature updates, alerts)
- Download reports of:
  - User activity
  - Feedback logs
  - Swap statistics

---

## 🛠️ Tech Stack

| Frontend      | Backend       | Styling        | Tooling       |
|---------------|---------------|----------------|----------------|
| React (with Vite) | Spring Boot (Java) | Tailwind CSS | Axios, JWT Auth |
| React Router  | REST API      | DaisyUI (optional) | ESLint + Prettier |

---

## 📂 Project Structure


---

## ⚙️ Setup Instructions

### ✅ Prerequisites
- Node.js (v18+)
- Java 17+
- Maven or Gradle
- MySQL or PostgreSQL

### 🔧 Frontend Setup

```bash
cd frontend
npm install
npm run dev

cd backend
# Update application.properties for DB credentials
./mvnw spring-boot:run
🙌 Contributors
Built with ❤️ by Astha Jaiswal


