import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext.jsx';
import { ProtectedRoute, AdminRoute } from './components/ProtectedRoute.jsx';
import Layout from './components/Layout.jsx';

// Public pages
import Home from './pages/Home.jsx';
import Login from './pages/Login.jsx';
import Register from './pages/Register.jsx';

// Protected pages
// import Profile from './pages/Profile';
import MySkills from './pages/MySkills.jsx';
import Search from './pages/Search.jsx';
// import SwapRequests from './pages/SwapRequests';
// import SwapRequestDetail from './pages/SwapRequestDetail';
// import UserProfile from './pages/UserProfile';

// Admin pages
// import AdminDashboard from './pages/admin/Dashboard';
// import AdminUsers from './pages/admin/Users';
// import AdminSkills from './pages/admin/Skills';
// import AdminSwapRequests from './pages/admin/SwapRequests';

function App() {
  return (
    <Router>
      <AuthProvider>
        <Routes>
          {/* Public Routes */}
          <Route path="/" element={<Layout />}>
            <Route index element={<Home />} />
            <Route path="login" element={<Login />} />
            <Route path="register" element={<Register />} />

            {/* Protected Routes */}
            <Route element={<ProtectedRoute />}>
              {/* Routes with implemented components */}
              <Route path="my-skills" element={<MySkills />} />
              <Route path="search" element={<Search />} />

              {/* Placeholder routes until components are created */}
              <Route path="profile" element={<div className="p-8 text-center">Profile Page (Coming Soon)</div>} />
              <Route path="swap-requests" element={<div className="p-8 text-center">Swap Requests Page (Coming Soon)</div>} />
              <Route path="swap-requests/:id" element={<div className="p-8 text-center">Swap Request Detail Page (Coming Soon)</div>} />
              <Route path="users/:id" element={<div className="p-8 text-center">User Profile Page (Coming Soon)</div>} />
            </Route>

            {/* Admin Routes */}
            <Route element={<AdminRoute />}>
              {/* These routes would be implemented next */}
              {/* <Route path="admin" element={<AdminDashboard />} />
              <Route path="admin/users" element={<AdminUsers />} />
              <Route path="admin/skills" element={<AdminSkills />} />
              <Route path="admin/swap-requests" element={<AdminSwapRequests />} /> */}

              {/* Placeholder routes until components are created */}
              <Route path="admin" element={<div className="p-8 text-center">Admin Dashboard (Coming Soon)</div>} />
              <Route path="admin/users" element={<div className="p-8 text-center">Admin Users Page (Coming Soon)</div>} />
              <Route path="admin/skills" element={<div className="p-8 text-center">Admin Skills Page (Coming Soon)</div>} />
              <Route path="admin/swap-requests" element={<div className="p-8 text-center">Admin Swap Requests Page (Coming Soon)</div>} />
            </Route>

            {/* Catch all route */}
            <Route path="*" element={<Navigate to="/" replace />} />
          </Route>
        </Routes>
      </AuthProvider>
    </Router>
  );
}

export default App;
