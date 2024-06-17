// Fatura.js
import React from 'react';
import SearchBar from './SearchBar'; 

const Fatura = ({ selectedProducts, handleRemoveProduct, increaseQuantity, decreaseQuantity, searchTerm, setSearchTerm, handleSearch }) => {
  const calculateTotal = () => {
    return selectedProducts.reduce((total, product) => {
      return total + (product.price * product.curr_quantity);
    }, 0);
  };
  return (
    
    <div className="admin-content-container">
      <div className="Fatura">
        <h3>Invoice</h3>
        <SearchBar
          searchTerm={searchTerm}
          setSearchTerm={setSearchTerm}
          onSearch={handleSearch}
          placeholder="Nome do produto"
        />

        <div className="admin-page">
          <div className='cabecalho'>
            <p>Name</p>
            <p>Quantity</p>
            <p>Price</p>
            <p>Action</p>
          </div>
          <div className="selected-products">
            {selectedProducts.map((product) => (
              <div key={product.id} className="selected-product">
                <p>{product.product}</p>
                <p>{product.curr_quantity}</p>
                <p>{product.price.toFixed(2)}</p>
                <div className="product-actions">
                  <button className="remove-button" onClick={() => handleRemoveProduct(product.id)}>Remove</button>
                  <button className="quantity-modify plus" onClick={() => increaseQuantity(product.id)}>+</button>
                  <button className="quantity-modify minus" onClick={() => decreaseQuantity(product.id)}>-</button>
                </div>
              </div>
            ))}
          </div>
          <div className="total">
            <h4>Total:  {calculateTotal().toFixed(2)} $ </h4>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Fatura;
