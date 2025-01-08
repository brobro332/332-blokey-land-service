import React from "react";
import PropTypes from 'prop-types';
import {
    ActiveButton,
    ConfirmInputContainer
} from '../../styles/styles';
import CustomInput from "../tags/CustomInput";

const ConfirmInput = ({ value, onChange, placeholder, onClick, buttonText, buttonDisabled=true, inputDisabled }) => {
    return (
        <ConfirmInputContainer>
            <CustomInput 
                value={value} 
                onChange={onChange} 
                placeholder={placeholder} 
                length="short"
                disabled={inputDisabled}
            />

            &nbsp;

            <ActiveButton 
                onClick={onClick} 
                disabled={buttonDisabled}
            >
                {buttonText}
            </ActiveButton>
        </ConfirmInputContainer>
    );
};

ConfirmInput.propTypes = {
    value: PropTypes.string.isRequired,
    onChange: PropTypes.func.isRequired,
    placeholder: PropTypes.string,
    onButtonClick: PropTypes.func.isRequired,
    buttonText: PropTypes.string.isRequired,
    buttonDisabled: PropTypes.bool,
    buttonStyle: PropTypes.object,
    disabled: PropTypes.bool
};

export default ConfirmInput;