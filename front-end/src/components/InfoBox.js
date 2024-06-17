import React from 'react';
import { Link } from 'react-router-dom';
import '../assets/css/Admin.css'; 

const InfoBox = ({ title, Icon, color, link, footerText, onClick }) => {
  const handleClick = (e) => {
    if (onClick) {
      e.preventDefault(); 
      onClick(); 
    }
  };

  const boxStyle = {
    textDecoration: 'none',
  };

  const titleStyle = {
    fontSize: '22px', 
  };

  const footerStyle = {
    fontSize: '19px', 
  };

  return (
    <Link to={link} className={`small-box bg-${color}`} style={boxStyle} onClick={handleClick}>
      <div className="inner">
        <p style={titleStyle}>{title}</p> 
        <Icon size={30} />
      </div>
      <div className="small-box-footer" style={footerStyle}> 
        {footerText}
      </div>
    </Link>
  );
};

export default InfoBox;
