
import React, { useState } from 'react';
import '../assets/css/Add_Product.css';
import { Link } from 'react-router-dom';
import axios from 'axios'; 

const AddProduct = () => {
  const [productDescription, setProductDescription] = useState('');
  const [productPrice, setProductPrice] = useState('');
  const [productQuantity, setProductQuantity] = useState('');
  const [productCategory, setProductCategory] = useState('');
  const [productImage, setProductImage] = useState(null);

  const handleAddProduct = () => {
    console.log('Produto adicionado:', {
      description: productDescription,
      price: productPrice,
      quantity: productQuantity,
      category: productCategory,
      image: productImage,
    });
    setProductDescription('');
    setProductPrice('');
    setProductQuantity('');
    setProductCategory('');
    setProductImage(null);
  };

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    setProductImage(file);
  };

  const addProductToAPI = () => {
    const formData = new FormData();
    formData.append('description', productDescription);
    formData.append('price', productPrice);
    formData.append('quantity', productQuantity);
    formData.append('category', productCategory);
    formData.append('image', productImage);

    axios.post('URL_DA_API_PARA_ADICIONAR_PRODUTO', formData)
      .then((response) => {
        console.log('Produto adicionado com sucesso:', response.data);
        setProductDescription('');
        setProductPrice('');
        setProductQuantity('');
        setProductCategory('');
        setProductImage(null);
      })
      .catch((error) => {
        console.error('Erro ao adicionar o produto:', error);
      });
  };

  return (
    <div className="add-product-container">
      <h2>Adicionar Produto</h2>

      <label htmlFor="productPrice">Preço:</label>
      <input
        type="text"
        id="productPrice"
        value={productPrice}
        onChange={(e) => setProductPrice(e.target.value)}
        placeholder="Insira o preço do produto..."
      />

      <label htmlFor="productQuantity">Quantidade:</label>
      <input
        type="text"
        id="productQuantity"
        value={productQuantity}
        onChange={(e) => setProductQuantity(e.target.value)}
        placeholder="Insira a quantidade do produto..."
      />

      <label htmlFor="productCategory">Categoria:</label>
      <input
        type="text"
        id="productCategory"
        value={productCategory}
        onChange={(e) => setProductCategory(e.target.value)}
        placeholder="Insira a categoria do produto..."
      />

      <label htmlFor="productImage">Imagem:</label>
      <input
        type="file"
        id="productImage"
        onChange={handleImageChange}
      />

      <label htmlFor="productDescription">Descrição do Produto:</label>
      <textarea
        id="productDescription"
        value={productDescription}
        onChange={(e) => setProductDescription(e.target.value)}
        placeholder="Insira a descrição do produto..."
      ></textarea>

      <div className="button-container">
        <button onClick={addProductToAPI}>Adicionar Produto</button>
        <button className='btn-canc' onClick={() => console.log('Cancelar')}>Cancelar</button>
      </div>
    </div>
  );
};

export default AddProduct;
