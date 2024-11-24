import React from 'react';

const CustomInput = ({ type = 'text', value, onChange, placeholder, style }) => {
    return <input type={type} value={value} onChange={onChange} placeholder={placeholder} style={style}/>;
};

CustomInput.propTypes = {
    type: (props, propName, componentName) => {
        if (props[propName] !== undefined && typeof props[propName] !== 'string') {
            return new Error(`${componentName}: 컴포넌트 속성 \`${propName}\`은(는) string 타입이어야 합니다.`);
        }
    },
    value: (props, propName, componentName) => {
        if (typeof props[propName] !== 'string') {
        return new Error(`${componentName}: 컴포넌트 속성 \`${propName}\`은(는) string 타입이어야 합니다.`);
        }
    },
    onChange: (props, propName, componentName) => {
        if (typeof props[propName] !== 'function') {
            return new Error(`${componentName}: 컴포넌트 속성 \`${propName}\`은(는) 함수여야 합니다.`);
        }
    }
};

export default CustomInput;