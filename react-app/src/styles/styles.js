import styled, { css } from "styled-components";
import { Link } from "react-router-dom";

export const FancyText = styled.div`
  font-size: 3rem;
  font-weight: bold;
  color: rgba(128, 128, 128, 0.2);
  position: relative;
  text-shadow: 
  3.5px 3.5px 0 rgba(0, 255, 200, 0.4),
  3px 3px 0 rgba(255, 255, 255, 0.3),
  2.5px 2.5px 0 rgba(255, 255, 255, 0.3),
  2px 2px 0 rgba(255, 255, 255, 0.2),
  6px 6px 5px rgba(128, 128, 128, 1);
  background: linear-gradient(135deg, rgba(0, 255, 200, 0.7), rgba(0, 200, 255, 0.2));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
`;

export const Container = styled.div`
  max-width: 400px;
  margin: 50px auto;
  margin-bottom: 10px;
  padding: 20px;
  border: 1px solid rgba(255, 255, 255, 1);;
  border-radius: 8px;
  text-align: center;
  box-shadow: 0 0 10px rgba(0, 255, 200, 0.7);
`;

export const ContainerJoin = styled.div`
  font-size: 15px;
  max-width: 400px;
  margin: auto;
  padding: 20px;
  border: 1px solid rgba(255, 255, 255, 1);
  border-radius: 8px;
  text-align: center;
  box-shadow: 0 0 10px rgba(0, 255, 200, 0.7);
`;

export const Content = styled.div`
  max-width: 400px;
  margin: 50px auto;
  margin-bottom: 10px;
  padding: 20px;
  border: 1px solid rgba(255, 255, 255, 1);;
  border-radius: 8px;
  text-align: left;
  box-shadow: 0 0 10px rgba(0, 255, 200, 0.7);
`;

export const StyledLink = styled(Link)`
  text-decoration: none;
  color: #007bff;
`;

export const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 15px;
`;

export const Hr = styled.hr`
  background-color: #ddd;
  width: 100%;
  height: 1px;
  border: 0;
`;

export const ActiveButton = styled.button`
  padding: 12px 16px;
  font-family: 'Black Han Sans', sans-serif;
  font-size: 18px;
  color: rgba(128, 128, 128, 0.6);
  background-color: rgba(0, 255, 200, 0.1);
  border: 1px solid rgba(255, 255, 255, 1);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  box-shadow: 
    2px 2px 0 rgba(255, 255, 255, 0.1),
    2.5px 2.5px 0 rgba(255, 255, 255, 0.1),
    3px 3px 0 rgba(0, 255, 200, 0.1),
    3.5px 3.5px 0 rgba(0, 255, 200, 0.1),
    -1px -1px 1px rgba(255, 255, 255, 0.1),
    6px 6px 5px rgba(255, 255, 255, 0.6),
    8px 8px 4px rgba(128, 128, 128, 0.5);
  
  text-shadow: 
    2px 2px 0 rgba(255, 255, 255, 0.1),
    2.1px 2.1px 0 rgba(0, 255, 200, 0.3),
    2.2px 2.2px 0 rgba(0, 255, 200, 0.3),
    2.3px 2.3px 0 rgba(0, 255, 200, 0.3),
    -1px -1px 1px rgba(255, 255, 255, 0.5),
    6px 6px 5px rgba(0, 255, 200, 0);
  
  &:hover {
    background-color: rgba(0, 255, 200, 0.2);
    box-shadow: 4px 4px 12px rgba(0, 255, 200, 0.8), 8px 8px 15px rgba(0, 255, 200, 0.6);
  }

  &:active {
    box-shadow: 3px 3px 8px rgba(0, 255, 200, 0.6), 6px 6px 10px rgba(0, 255, 200, 0.4);
  }

  ${({ disabled }) =>
    disabled &&
    css`
      background-color: #e0e0e0;
      color: #bdbdbd;
      cursor: not-allowed;

      text-shadow: 
        2px 2px 0 rgba(255, 255, 255, 0.1),
        2.1px 2.1px 0 rgba(128, 128, 128, 0.3),
        2.2px 2.2px 0 rgba(128, 128, 128, 0.3),
        2.3px 2.3px 0 rgba(128, 128, 128, 0.3),
        -1px -1px 1px rgba(255, 255, 255, 0.5),
        6px 6px 5px rgba(255, 204, 0, 0);

      box-shadow: 
        2px 2px 0 rgba(255, 255, 255, 0.1),
        2.5px 2.5px 0 rgba(255, 255, 255, 0.1),
        3px 3px 0 rgba(128, 128, 128, 0.1),
        3.5px 3.5px 0 rgba(128, 128, 128, 0.1),
        -1px -1px 1px rgba(255, 255, 255, 0.1),
        6px 6px 5px rgba(255, 255, 255, 0.6),
        8px 8px 4px rgba(128, 128, 128, 0.5);

       &:hover {
        background-color: #e0e0e0;
        box-shadow: 
          2px 2px 0 rgba(255, 255, 255, 0.1),
          2.5px 2.5px 0 rgba(255, 255, 255, 0.1),
          3px 3px 0 rgba(128, 128, 128, 0.1),
          3.5px 3.5px 0 rgba(128, 128, 128, 0.1),
          -1px -1px 1px rgba(255, 255, 255, 0.1),
          6px 6px 5px rgba(255, 255, 255, 0.6),
          8px 8px 4px rgba(128, 128, 128, 0.5);
      }
  `}
`;

export const CancelButton = styled.button`
  padding: 12px 16px;
  font-family: 'Black Han Sans', sans-serif;
  font-size: 18px;
  color: rgba(128, 128, 128, 0.6);
  background-color: rgba(128, 128, 128, 0.1);
  border: 1px solid rgba(255, 255, 255, 1);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  box-shadow: 
    2px 2px 0 rgba(255, 255, 255, 0.1),
    2.5px 2.5px 0 rgba(255, 255, 255, 0.1),
    3px 3px 0 rgba(128, 128, 128, 0.1),
    3.5px 3.5px 0 rgba(128, 128, 128, 0.1),
    -1px -1px 1px rgba(255, 255, 255, 0.1),
    6px 6px 5px rgba(255, 255, 255, 0.6),
    8px 8px 4px rgba(128, 128, 128, 0.5);
  
  text-shadow: 
    2px 2px 0 rgba(255, 255, 255, 0.1),
    2.1px 2.1px 0 rgba(128, 128, 128, 0.3),
    2.2px 2.2px 0 rgba(128, 128, 128, 0.3),
    2.3px 2.3px 0 rgba(128, 128, 128, 0.3),
    -1px -1px 1px rgba(255, 255, 255, 0.5),
    6px 6px 5px rgba(255, 204, 0, 0);
  
  &:hover {
    background-color: rgba(128, 128, 128, 0.2);
    box-shadow: 4px 4px 12px rgba(128, 128, 128, 0.8), 8px 8px 15px rgba(128, 128, 128, 0.6);
  }

  &:active {
    box-shadow: 3px 3px 8px rgba(128, 128, 128, 0.6), 6px 6px 10px rgba(128, 128, 128, 0.4);
  }
`;

export const GoogleLoginButton = styled.button`
  padding: 12px 16px;
  font-family: 'Black Han Sans', sans-serif;
  font-size: 18px;
  color: rgba(128, 128, 128, 0.6);
  background-color: rgba(255, 77, 77, 0.1);
  border: 1px solid rgba(255, 255, 255, 1);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  box-shadow: 
    2px 2px 0 rgba(255, 255, 255, 0.1),
    2.5px 2.5px 0 rgba(255, 255, 255, 0.1),
    3px 3px 0 rgba(255, 77, 77, 0.1),
    3.5px 3.5px 0 rgba(255, 77, 77, 0.1),
    -1px -1px 1px rgba(255, 255, 255, 0.1),
    6px 6px 5px rgba(255, 255, 255, 0.6),
    8px 8px 4px rgba(128, 128, 128, 0.5);
  
  text-shadow: 
    2px 2px 0 rgba(255, 255, 255, 0.1),
    2.1px 2.1px 0 rgba(255, 77, 77, 0.3),
    2.2px 2.2px 0 rgba(255, 77, 77, 0.3),
    2.3px 2.3px 0 rgba(255, 77, 77, 0.3),
    -1px -1px 1px rgba(255, 255, 255, 0.5),
    6px 6px 5px rgba(255, 77, 77, 0);
  
  &:hover {
    background-color: rgba(255, 77, 77, 0.2);
    box-shadow: 4px 4px 12px rgba(255, 77, 77, 0.8), 8px 8px 15px rgba(255, 77, 77, 0.6);
  }

  &:active {
    box-shadow: 3px 3px 8px rgba(255, 77, 77, 0.6), 6px 6px 10px rgba(255, 77, 77, 0.4);
  }
`;

export const KakaoLoginButton = styled.button`
  padding: 12px 16px;
  font-family: 'Black Han Sans', sans-serif;
  font-size: 18px;
  color: rgba(128, 128, 128, 0.6);
  background-color: rgba(255, 204, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 1);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  box-shadow: 
    2px 2px 0 rgba(255, 255, 255, 0.1),
    2.5px 2.5px 0 rgba(255, 255, 255, 0.1),
    3px 3px 0 rgba(255, 204, 0, 0.1),
    3.5px 3.5px 0 rgba(255, 204, 0, 0.1),
    -1px -1px 1px rgba(255, 255, 255, 0.1),
    6px 6px 5px rgba(255, 255, 255, 0.6),
    8px 8px 4px rgba(128, 128, 128, 0.5);
  
  text-shadow: 
    2px 2px 0 rgba(255, 255, 255, 0.1),
    2.1px 2.1px 0 rgba(255, 204, 0, 0.3),
    2.2px 2.2px 0 rgba(255, 204, 0, 0.3),
    2.3px 2.3px 0 rgba(255, 204, 0, 0.3),
    -1px -1px 1px rgba(255, 255, 255, 0.5),
    6px 6px 5px rgba(255, 204, 0, 0);
  
  &:hover {
    background-color: rgba(255, 204, 0, 0.2);
    box-shadow: 4px 4px 12px rgba(255, 204, 0, 0.8), 8px 8px 15px rgba(255, 204, 0, 0.6);
  }

  &:active {
    box-shadow: 3px 3px 8px rgba(255, 204, 0, 0.6), 6px 6px 10px rgba(255, 204, 0, 0.4);
  }
`;

export const ButtonsDiv = styled.div`
  display: 'flex';     
  justifyContent: 'space-between';
  width: '100%';
`;

export const Input = styled.input`
  padding: 10px;
  font-size: 13px;
  border: 1px solid #ccc;
  border-radius: 4px;
  outline: none;

  box-shadow: 
    4px 4px 4px rgba(128, 128, 128, 0.5);
  
  &:focus {
    border-color: #00ffd4;
    box-shadow: 
      0 0 5px rgba(0, 255, 212, 0.5),
      4px 4px 4px rgba(128, 128, 128, 0.5);
  }

  &:disabled {
    background-color: #f0f0f0;
    cursor: not-allowed;
  }
`;

export const ConfirmInputContainer = styled.div`
  display: flex;
  justify-content: flex-start;
`;

export const ShortInput = styled.input`
  width: 77%;
  left: 0;
  padding: 10px;
  font-size: 13px;
  border: 1px solid #ccc;
  border-radius: 4px;
  outline: none;

  box-shadow: 
    4px 4px 4px rgba(128, 128, 128, 0.5);
  
  &:focus {
    border-color: #00ffd4;
    box-shadow: 
      0 0 5px rgba(0, 255, 212, 0.5),
      4px 4px 4px rgba(128, 128, 128, 0.5);
  }

  &:disabled {
    background-color: #f0f0f0;
    cursor: not-allowed;
  }
`;

export const BorderlessContent = styled.div`
  max-width: 400px;
  margin: 0;
  margin-bottom: 10px;
  padding: 20px;
  border: "none";
  text-align: left;
`;

export const MintColorText = styled.div`
  h4, span {
    color: rgb(0, 255, 200);
    marginBottom: "0";
    fontWeight: "bold";
  }
`;