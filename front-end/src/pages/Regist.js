import React, { useState } from 'react';
import '../assets/css/Login_Regist.css';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import BaseUrl from '../components/BaseUrl';

const Regist = () => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [passwordConf, setPasswordConf] = useState('');
  const navigate = useNavigate();

  const registerUser = async () => {
    if (password !== passwordConf) {
      console.log('As senhas não coincidem');
      return;
    }

    const userData = {

      name,
      email,
      password,
      role:"EMPLOYEE"

    };

    try {
      const response = await axios.post(`${BaseUrl()}auth/signin`, userData, {
        headers: {
          'Content-Type': 'application/json'
        }
      });
  
      if (response.data && response.data.Role) {
        console.log('Registro bem-sucedido:', response.data);
        navigate('/login');
      } else {
        console.log('Registro bem-sucedido, mas informações do utilizador não estão completas.');
        navigate('/login');
      }
    } catch (error) {
      console.error('Erro ao registrar o usuário:', error.response ? error.response.data : error);
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    registerUser();
  };

  return (
    <div className="container">
      <div className="login-container">
        <h2>Registo</h2>
        <form onSubmit={handleSubmit}>
          <label>
            <strong>Nome:</strong>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
          </label>
          <br />
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
            <strong>Senha:</strong>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </label>
          <br />
          <label>
            <strong>Confirmar Senha:</strong>
            <input
              type="password"
              value={passwordConf}
              onChange={(e) => setPasswordConf(e.target.value)}
              required
            />
          </label>
          <br />
          <div className="button-container">
            <button type="submit" className="register-button">Registrar</button>
            <Link to="/login">
              <button type="button" className="login-button">Login</button>
            </Link>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Regist;
