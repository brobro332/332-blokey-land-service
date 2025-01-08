import React from "react";
import PropTypes from 'prop-types';
import {
    Input,
    ShortInput
} from '../../styles/styles';

const CustomInput = ({ type = 'text', value, onChange, placeholder, disabled, length }) => {
  if (length === 'short') {
    return (
        <ShortInput
          type={type}
          value={value}
          onChange={onChange}
          placeholder={placeholder}
          disabled={disabled}
        />
      );
  } else {
    return <Input
      type={type}
      value={value}
      onChange={onChange}
      placeholder={placeholder}
      disabled={disabled}
    />
  }
};

CustomInput.propTypes = {
  type: PropTypes.string,
  value: PropTypes.string.isRequired,
  onChange: PropTypes.func.isRequired,
  placeholder: PropTypes.string,
  disabled: PropTypes.bool,
  length: PropTypes.object,
};

export default CustomInput;
