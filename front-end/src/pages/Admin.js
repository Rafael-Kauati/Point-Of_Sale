

import React, { useState } from 'react';
import { Link, useParams } from 'react-router-dom';

import { BsFillPersonFill } from 'react-icons/bs';
import { FiAlignJustify } from 'react-icons/fi';
import { ImCart, ImExit } from 'react-icons/im';
import { FcStatistics } from 'react-icons/fc';
import { MdOutlinePayment } from 'react-icons/md';

import Popup from 'reactjs-popup';
import InfoBox from '../components/InfoBox';
import SearchBar from '../components/SearchBar';
import '../assets/css/Admin.css';
import Fatura from '../components/Fatura';
import BaseUrl from '../components/BaseUrl';

import axios from 'axios';



const Admin = () => {
  const [isExitPopupOpen, setIsExitPopupOpen] = useState(false);
  const [isPayPopupOpen, setIsPayPopupOpen] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  //const userId = localStorage.getItem('userId');
  const { userId } = useParams();
  // if (userId) {
  //   localStorage.setItem('userId', userId);
  // }
  if (userId) {
    localStorage.setItem('userId', userId);
  }
  
  const handleExitClick = () => {
    setIsExitPopupOpen(true);
    setIsPayPopupOpen(false); 
  };
  
  const handlePayClick = () => {
    setIsPayPopupOpen(true);
    setIsExitPopupOpen(false); 
  };

  const handlePayment = async () => {
    //const userId = localStorage.getItem('userId');
    
    if (!userId) {
      console.error('Erro: ID do usuário não disponível');
      return;
    }
 
    if (!selectedProducts || selectedProducts.length === 0) {
      console.log('Nenhum produto selecionado para pagamento');
      return;
    }
    
    const cart = selectedProducts.reduce((acc, product) => {
      acc[product.id] = product.curr_quantity;
      return acc;
    }, {});
    
    try {
      const token = localStorage.getItem('token');
      const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      };
  
      const response = await axios.post(`${BaseUrl()}api/payment/${userId}`, cart, { headers });
        
      console.log('Pagamento realizado com sucesso', response.data);
    
      setSelectedProducts([]);
      setIsPayPopupOpen(false);
    } catch (error) {
      console.error('Erro ao processar o pagamento:', error);
    }
  };
  
  const exitPopupContent = (
    <div className="popup">
      <div className="popup-inner fade-in">
        <h3 className="text-lg font-semibold text-black">Confirm Exit</h3>
        <div className="flex justify-center mt-5">
          <button onClick={() => setIsExitPopupOpen(false)} className="popup-button">Cancel</button>
          <Link to="/login" className="popup-button confirm">Confirm</Link>
        </div>
      </div>
    </div>
  );
  
  const payPopupContent = (
    <div className="popup">
      <div className="popup-inner fade-in">
        <h3 className="text-lg font-semibold text-black">Payment Confirmation</h3>
        <div className="flex justify-center mt-5">
          <button onClick={() => setIsPayPopupOpen(false)} style={{ backgroundColor: 'red'}}className="popup-button popup-button-cancel">Cancel</button>
       
          <button onClick={handlePayment} className="popup-button popup-button-confirm">Confirm </button>
   
        </div>
      </div>
    </div>
  );

  const [selectedProducts, setSelectedProducts] = useState([]);

  const handleRemoveProduct = productId => {
    console.log(`Removendo produto com ID: ${productId}`);
    setSelectedProducts(prevProducts => prevProducts.filter(product => product.id !== productId));
  };


  const handleSearch = async () => {
    if (!searchTerm.startsWith('/')) {
      try {
        const response = await axios.get(`${BaseUrl()}api/stock/product?id=${searchTerm}`);
        const newProduct = response.data.object || null;

        if (newProduct) {
          const isDuplicate = selectedProducts.some(product => product.id === newProduct.id);

          if (!isDuplicate) {
            initializeProductQuantity(newProduct);
            setSelectedProducts(prevProducts => [...prevProducts, newProduct]);
            console.log(`Produto adicionado: ${newProduct.product}`);
            
          } else {
            console.log(`Produto já existe na lista: ${newProduct.product}`);
          }
        } else {
          console.log("Nenhum produto encontrado com este ID");
        }
      } catch (error) {
        if (error.response) {
          console.error(`Erro na pesquisa: ${error.response.status} ${error.response.statusText}`);
        } else {
          console.error('Erro na requisição:', error.message);
        }
      }
    }
  };

  



  const initializeProductQuantity = (product) => {

   // product.curr_quantity = 1;
    product.max_quantity = product.curr_quantity || Infinity;
  };

  const increaseQuantity = (productId) => {
    setSelectedProducts(prevProducts => prevProducts.map(product => {
      if (product.id === productId) {
        if (product.curr_quantity < product.max_quantity) {
          console.log(`Aumentando a quantidade do produto ID ${productId}, Quantidade atual: ${product.curr_quantity}, Quantidade máxima: ${product.max_quantity}`);
          return { ...product, curr_quantity: product.curr_quantity + 1 };
        } else {
          console.log(`Quantidade máxima atingida para o produto ID ${productId}`);
        }
      }
      return product;
    }));
  };

  const decreaseQuantity = (productId) => {
    setSelectedProducts(prevProducts => prevProducts.map(product => {
      if (product.id === productId) {
        if (product.curr_quantity > 0) {
          console.log(`Diminuindo a quantidade do produto ID ${productId}, Quantidade atual: ${product.curr_quantity}`);
          return { ...product, curr_quantity: product.curr_quantity - 1 };
        } else {
          console.log(`Quantidade mínima atingida para o produto ID ${productId}`);
        }
      }
      return product;
    }));
  };

  return (
    <div className="admin-container">
    <div className="admin-header">
      <h2 className="admin-title">Admin Page</h2>
    </div>
    
    
    <div className="admin-content-container">
        <div className="Fatura">
        <Fatura
                selectedProducts={selectedProducts}
                handleRemoveProduct={handleRemoveProduct}
                increaseQuantity={increaseQuantity}
                decreaseQuantity={decreaseQuantity}
                searchTerm={searchTerm}
                setSearchTerm={setSearchTerm}
                handleSearch={handleSearch}
              />

        </div>
        
        <div className="admin-content">
          <h2>Administration</h2>
         {/* Primeira fila de boxes */}
         <div className="row">
      
      
          <div className="col-lg-4 col-12">
              <InfoBox
                title="Customers"
                Icon={BsFillPersonFill}
                color="info"
                link="/manage-customers"
                footerText="More info"
              />
          </div>
            {/* InfoBox para Stocks */}
            <div className="col-lg-4 col-12">
                <InfoBox
                  title="Stocks"
                  Icon={FiAlignJustify}
                  color="success"
                  link="/stocks"
                  footerText="More info"
                />
              </div>

              {/* InfoBox para Orders */}
              <div className="col-lg-4 col-12">
                <InfoBox
                  title="Orders"
                  Icon={ImCart}
                  color="warning"

                  footerText="More info"
                />
              </div>
             </div>

          {/* Segunda fila de boxes */}
          <div className="row">
            {/* InfoBox para Charts */}
            <div className="col-lg-4 col-12">
              <InfoBox
                title="Charts"
                Icon={FcStatistics}
                color="info"
                link="/admin-graf"
                footerText="More info"
              />
            </div>

            {/* InfoBox para Pay */}
            <div className="col-lg-4 col-12">
              <InfoBox 
                title="Pay"
                Icon={MdOutlinePayment}
                color="primary"
                onClick={handlePayClick}
                footerText="Pay now"
              />
            </div>
            
            
            {/* InfoBox para Exit */}
            <div className="col-lg-4 col-12">
              <InfoBox 
                title="Exit"
                Icon={ImExit}
                color="danger"
                onClick={handleExitClick}
                footerText="Exit now"
              />
            </div>
      

            <Popup open={isExitPopupOpen} closeOnDocumentClick onClose={() => setIsExitPopupOpen(false)}>
              {exitPopupContent}
            </Popup>

            <Popup open={isPayPopupOpen} closeOnDocumentClick onClose={() => setIsPayPopupOpen(false)}>
              {payPopupContent}
            </Popup>
                  

          </div>
        </div>
      </div> 
    </div>
  );
};

export default Admin;

