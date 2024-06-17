
import React, { useState } from 'react';
import '../assets/css/Login_Regist.css';
import { Link, useNavigate, useParams } from 'react-router-dom';import axios from 'axios';
import BaseUrl from '../components/BaseUrl';

const Login = ({ onLogin }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const loginUser = async () => {
    const userData = {
      email,
      password,
    };

    try {
      const response = await axios.post(`${BaseUrl()}auth/login`, userData);
      const { access_token, user_type,user_id , Role } = response.data;

      localStorage.setItem('token', access_token);
      localStorage.setItem('userType', user_type);

      
      console.log('Login bem-sucedido:', response.data);
      console.log('user_type:', user_type);
      console.log('role: ', Role);
      console.log('token:', access_token);
      
   
      if (Role === 'MANAGER') {
        navigate(`/admin/${user_id}`);
      }
      if (Role === 'EMPLOYEE') {
        navigate(`/employee/${user_id}`);
      }
      
      
    } catch (error) {
      console.error('Erro ao fazer login:', error.response ? error.response.data : error);
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    loginUser();
  };

  return (
    <div className="container">
      <div className="login-container">
        <h2>Login</h2>
        <form onSubmit={handleSubmit}>
          <label>
            <strong>Email:</strong>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </label>
          <br />
          <label>
            <strong>Password:</strong>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </label>
          <br />
          <div className="button-container">
            <button type="submit">Login</button>
            <Link to="/regist">
              <button type="button" className="register-button">
                Registrar
              </button>
            </Link>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Login;

