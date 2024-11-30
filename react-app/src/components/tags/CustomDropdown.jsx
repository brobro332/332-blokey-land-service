import React, { useState } from 'react';

const Dropdown = ({ title, content }) => {
  const [isOpen, setIsOpen] = useState(false);

  const toggleDropdown = () => {
    setIsOpen(!isOpen);
  };

  return (
    
    <div style={styles.dropdown}>
      <div style={styles.header} onClick={toggleDropdown}>
        <span>{title}</span>
        <button style={styles.toggleButton}>
          {isOpen ? '▲' : '▼'}
        </button>
      </div>
      {
        isOpen && <div dangerouslySetInnerHTML={
            { __html: content }
        } />
      }
    </div>
  );
};

const styles = {
  dropdown: {
    marginBottom: '10px',
    border: '1px solid #ddd',
    borderRadius: '8px',
    overflow: 'hidden',
  },
  header: {
    padding: '10px',
    backgroundColor: '#f9f9f9',
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    cursor: 'pointer',
  },
  toggleButton: {
    border: 'none',
    backgroundColor: 'transparent',
    fontSize: '14px',
    cursor: 'pointer',
  },
  content: {
    padding: '10px',
    backgroundColor: '#fff',
    color: '#333',
  },
};

export default Dropdown;