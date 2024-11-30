import React from "react";
import PropTypes from 'prop-types';

const CustomInput = ({ type = 'text', value, onChange, placeholder, style, disabled }) => {
    return <input type={type} value={value} onChange={onChange} placeholder={placeholder} style={style} disabled={disabled} />;
};

const InputWithButton = ({ value, onChange, placeholder, onClick, buttonText, buttonDisabled=true, inputStyle, buttonStyle, inputDisabled }) => {
    return (
        <div>
            <CustomInput 
                value={value} 
                onChange={onChange} 
                placeholder={placeholder} 
                style={inputStyle} 
                disabled={inputDisabled}
            />
            <button 
                onClick={onClick} 
                style={buttonStyle} 
                disabled={buttonDisabled}
            >
                {buttonText}
            </button>
        </div>
    );
};

InputWithButton.propTypes = {
    value: PropTypes.string.isRequired,
    onChange: PropTypes.func.isRequired,
    placeholder: PropTypes.string,
    onButtonClick: PropTypes.func.isRequired,
    buttonText: PropTypes.string.isRequired,
    buttonDisabled: PropTypes.bool,
    inputStyle: PropTypes.object,
    buttonStyle: PropTypes.object,
    disabled: PropTypes.bool
};

export default InputWithButton;