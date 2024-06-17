
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../assets/css/Stocks.css';
import ProductForm from '../components/ProductForm';
import { LineController } from 'chart.js';

import { Link } from 'react-router-dom'; 
import BaseUrl from '../components/BaseUrl';

const Stocks_Employee = () => {
    const [employeeData, setEmployeeData] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [searchResults, setSearchResults] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [showPopup, setShowPopup] = useState(false);
    const [selectedProduct, setSelectedProduct] = useState(null);
    const [productToUpdate, setProductToUpdate] = useState(null);
    const [showUpdatePopup, setShowUpdatePopup] = useState(false);
    const [showAddProductForm, setShowAddProductForm] = useState(false);
    const [updateFormData, setUpdateFormData] = useState(
        {
            id: '',
            product: '',
            category: '',
            curr_quantity: '',
            price: '',
            provider: '',
        }
    );
    const itemsPerPage = 5;

  
        const [newProductFormData, setNewProductFormData] = useState({
            product: '',
            category: '',
            curr_quantity: '',
            price: '',
            provider: '',
        });

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get(`${BaseUrl()}api/stock/products`);                   //'http://localhost:9000/api/stock/products');
                setEmployeeData(response.data);
                setSearchResults(response.data); 
                localStorage.setItem('employeeData', JSON.stringify(response.data));
            } catch (error) {
                console.error('Erro ao buscar informações:', error);
            }
        };
        fetchData();
    }, []);
    
        const closePopup = () => {
        setShowPopup(false);
        setSelectedProduct(null);
    };
    const handleViewDetails = (id) => {
        const product = employeeData.find(item => item.id === id);
        setSelectedProduct(product);
        setShowPopup(true);
    };

    const handleUpdateFormSubmit = async (e) => {
                e.preventDefault();
        
                try {
                    await axios.put(`${BaseUrl()}api/stock/update?id=${updateFormData.id}`, updateFormData);//`http://localhost:9000/api/stock/update?id=${updateFormData.id}`, updateFormData);

                    const updatedEmployeeData = employeeData.map(item => {
                        if (item.id === updateFormData.id) {
                            return {
                                ...item,
                                product: updateFormData.product,
                                category: updateFormData.category,
                                curr_quantity: updateFormData.curr_quantity,
                                price: updateFormData.price,
                                provider: updateFormData.provider,
                            };
                        }
                        return item;
                    });
                    setEmployeeData(updatedEmployeeData);
                    setShowUpdatePopup(false);
        
                    console.log(`Product with ID: ${updateFormData.id} updated successfully.`);
                } catch (error) {
                    console.error('Error updating product:', error);
                }
            };

    const handleSearch = async () => {
        if (searchTerm === '') {
            setSearchResults(employeeData); 
        } else {
            const filteredData = employeeData.filter(item => 
                item.product.toLowerCase().includes(searchTerm.toLowerCase())
            );
            setSearchResults(filteredData); 
        }
    };

   

    const paginationRange = () => {
        const currentRangeStart = Math.floor((currentPage - 1) / itemsPerPage) * itemsPerPage;
        return range(currentRangeStart, Math.min(currentRangeStart + itemsPerPage, totalPages));
    };
        
        const paginate = (pageNumber) => {
        const newPageNumber = Math.min(Math.max(pageNumber, 1), totalPages);
        setCurrentPage(newPageNumber);
    };

        const range = (start, end) => {
        return Array.from({ length: (end - start) }, (_, i) => start + i);
    };

        const handleAddProduct = () => {
        setShowAddProductForm(true);
    };


    const handleAddProductFormSubmit = async (e) => {
        e.preventDefault();

        try {
    
            const response = await axios.post(`${BaseUrl()}api/stock/store/`, newProductFormData);//'http://localhost:9000/api/stock/store/', newProductFormData);

            const newProduct = response.data;
            setEmployeeData([...employeeData, newProduct]);

            // Clear the form data
            setNewProductFormData({
                product: '',
                category: '',
                curr_quantity: '',
                price: '',
                provider: '',
            });

            // Close the "Add Product" form
            setShowAddProductForm(false);

            console.log('Product added successfully.');
        } catch (error) {
            console.error('Error adding product:', error);
        }
    };

    

    const debounce = (func, delay) => {
        let inDebounce;
        return function() {
            const context = this;
            const args = arguments;
            clearTimeout(inDebounce);
            inDebounce = setTimeout(() => func.apply(context, args), delay);
        };
    };

    const debouncedSearch = debounce(handleSearch, 300);

    // Lógica de paginação
    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentItems = searchResults.slice(indexOfFirstItem, indexOfLastItem);
    const totalItems = searchResults.length;
    const totalPages = Math.ceil(totalItems / itemsPerPage);

    return (
        <div className="graficos-container">
            <h1 className='stocks-title'>Stocks Employee</h1>
            <div className="search-bar">
                <label htmlFor="search-input">Search:</label>
                <input
                    id="search-input"
                    type='text'
                    placeholder='Search...'
                    value={searchTerm}
                    onChange={(e) => {
                        setSearchTerm(e.target.value);
                        debouncedSearch();
                    }}
                    onKeyDown={(e) => {
                        if (e.key === 'Enter') {
                            handleSearch();
                        }
                    }}
                />
                <Link to="/employee" className="employee-page-button">
                    <button>Ir para página do empregado</button>
                </Link>

            </div>

            <div className="stocks-container">
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Category</th>
                            <th>Current Quantity</th>
                            <th>Price</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {currentItems.map((item) => (
                            <tr key={item.id}>
                                <td>{item.id}</td>
                                <td>{item.product}</td>
                                <td>{item.category}</td>
                                <td>{item.curr_quantity}</td>
                                <td>{item.price}</td>
                                <td>
                                <div className='actions'>
                                         <button className="view-details-btn" onClick={() => handleViewDetails(item.id)}>See details</button>
      
                                    
                                     </div>


                                    {showAddProductForm && (
                                    <div className="popup">
                                        <div className="popup-inner">
                                            <h1>Add Product</h1>
                                            <form onSubmit={handleAddProductFormSubmit}>
                                            
                                                <label>Name:</label>
                                                <input
                                                    type="text"
                                                    value={newProductFormData.product}
                                                    onChange={(e) => setNewProductFormData({ ...newProductFormData, product: e.target.value })}
                                                    required
                                                />
                                                <label>Category:</label>
                                                <input
                                                    type="text"
                                                    value={newProductFormData.category}
                                                    onChange={(e) => setNewProductFormData({ ...newProductFormData, category: e.target.value })}
                                                    required
                                                />
                                                <label>Current Quantity:</label>
                                                <input
                                                    type="text"
                                                    value={newProductFormData.curr_quantity}
                                                    onChange={(e) => setNewProductFormData({ ...newProductFormData, curr_quantity: e.target.value })}
                                                    required
                                                />
                                                <label>Price:</label>
                                                <input
                                                    type="text"
                                                    value={newProductFormData.price}
                                                    onChange={(e) => setNewProductFormData({ ...newProductFormData, price: e.target.value })}
                                                    required
                                                />
                                                <label>Provider:</label>
                                                <input
                                                    type="text"
                                                    value={newProductFormData.provider}
                                                    onChange={(e) => setNewProductFormData({ ...newProductFormData, provider: e.target.value })}
                                                    required
                                                />

                                                <button type="submit">Add</button>
                                            </form>
                                            <button className="popup__close" onClick={() => setShowAddProductForm(false)}>Close</button>
                                        </div>
                                        
                                    </div>
                                    
                                )}
                                {showUpdatePopup && productToUpdate && (
                                    <div className="popup">
                                        <div className="popup-inner">
                                            <h1>Update Product</h1>
                                            <ProductForm
                                                formData={updateFormData}
                                                setFormData={setUpdateFormData}
                                                handleSubmit={handleUpdateFormSubmit}
                                                isUpdate={true}
                                            />
                                            <button className="popup__close" onClick={() => setShowUpdatePopup(false)}>Close</button>
                                        </div>
                                    </div>
                                )}

                            {showPopup && selectedProduct && (
                                <div className="popup">
                                    <div className="popup-inner">
                                        <h1>Detalhes do Produto</h1>
                                        <p><strong>ID:</strong> {selectedProduct.id}</p>
                                        <p><strong>Nome:</strong> {selectedProduct.product}</p>
                                        <p><strong>Categoria:</strong> {selectedProduct.category}</p>
                                        <p><strong>Quantidade:</strong> {selectedProduct.curr_quantity}</p>
                                        <p><strong>Preço:</strong> ${selectedProduct.price.toFixed(2)}</p>
                                        <p><strong>Fornecedor:</strong> {selectedProduct.provider}</p>
                                        <button className="popup__close" onClick={closePopup}>Fechar</button>
                                    </div>
                                </div>
                            )}





                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
                <nav>
                    <ul className='pagination'>
                        <li className={`page-item ${currentPage === 1 ? 'disabled' : ''}`}>
                            <a onClick={() => paginate(Math.max(currentPage - 1, 1))} className='page-link'>
                                &#171;
                            </a>
                        </li>
                        
                        {paginationRange().map(number => (
                            <li key={number} className={`page-item ${currentPage === number ? 'active' : ''}`}>
                                <a onClick={() => paginate(number)} className='page-link'>
                                    {number}
                                </a>
                            </li>
                        ))}
                        <li className={`page-item ${currentPage === totalPages ? 'disabled' : ''}`}>
                            <a onClick={() => paginate(Math.min(currentPage + 1, totalPages))} className='page-link'>
                                &#187;
                            </a>
                        </li>
                       
                    </ul>
                    
                </nav>
                
            </div>
        </div>
    );
};

export default Stocks_Employee;
