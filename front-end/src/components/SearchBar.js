// SearchBar.js
import React from 'react';
import '../assets/css/SearchBar.css';

const SearchBar = ({ searchTerm, setSearchTerm, onSearch, placeholder }) => {
  return (
    <div className="search-bar">
      <input
        type="text"
        placeholder={placeholder}
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        onKeyDown={(e) => e.key === 'Enter' && onSearch()}
      />
      <li  className="search-button" onClick={onSearch}>
        <img width="48" height="48" src="https://img.icons8.com/color/48/search--v1.png" alt="search"/>
      </li>
    </div>
  );
};

export default SearchBar;
