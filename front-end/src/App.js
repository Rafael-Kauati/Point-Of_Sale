
// App.js

import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import axios from 'axios';
import Login from './pages/Login';
import Regist from './pages/Regist';
import Employee from './pages/Employee';
import Admin from './pages/Admin';
import Stocks from './pages/Stocks';
import AddProduct from './pages/Add_Product';
import Graficos from './pages/Graficos';
import Admin_Graf from './pages/Admin_Graf';
import './assets/css/Login_Regist.css';
import './assets/css/Add_Product.css';
import Stocks_Employee from './pages/Stocks_Employee';

// // Configure Axios globally
// axios.defaults.baseURL = 'http://localhost:9000/api';
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  config.headers.Authorization = token ? `Bearer ${token}` : '';
  return config;
});

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userType, setUserType] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('token');
    const storedUserType = localStorage.getItem('userType');
    if (token) {
      setIsLoggedIn(true);
      setUserType(storedUserType);
    }
  }, []);

  const handleLogin = (type) => {
    setIsLoggedIn(true);
    setUserType(type);
    localStorage.setItem('userType', type);
  };

  const handleLogout = () => {
    setIsLoggedIn(false);
    setUserType(null);
    localStorage.removeItem('token');
    localStorage.removeItem('userType');
  };

  const ProtectedRoute = ({ children }) => {
    if (isLoggedIn) {
      return children;
    }
    return <Navigate to="/login" />;
  };

  return (
    <Router>
      <Routes>
        <Route path="/login" element={<Login onLogin={handleLogin} />} />
        <Route path="/regist" element={<Regist />} />
        <Route path="/" element={<Navigate to={isLoggedIn ? (userType === 'ADMIN' ? '/admin' : '/employee') : '/login'} />} />
        <Route path="/admin/:userId" component={Stocks} element={
          <ProtectedRoute>
            <Admin />
          </ProtectedRoute>
        } />
        <Route path="/employee/:userId"  component={Graficos} element={
          <ProtectedRoute>
            <Employee />
          </ProtectedRoute>
        } />
        <Route path="/stocks" element={
          <ProtectedRoute>
            <Stocks />
          </ProtectedRoute>
        } />
        <Route path="/add_product" element={
          <ProtectedRoute>
            <AddProduct />
          </ProtectedRoute>
        } />
        <Route path="/graficos"  element={
          <ProtectedRoute>
            <Graficos />
          </ProtectedRoute>
        } />
        <Route path="/admin-graf" element={
          <ProtectedRoute>
            <Admin_Graf />
          </ProtectedRoute>
        } />
        <Route path="/stocks_employee" element={<Stocks_Employee />} />
      </Routes>
    </Router>
  );
}

export default App;
