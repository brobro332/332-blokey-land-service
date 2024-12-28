import React, { useState } from 'react';
import ValidAgreeCheckbox from '../contents/ValidAgreeCheckbox';
import { useNavigate } from 'react-router-dom';
import {
  FancyText,
  Container,
  Content,
  Hr,
  ButtonsDiv,
  CancelButton,
  ActiveButton
} from '../../styles/styles';

const PolicyForm = () => {
  const navigate = useNavigate();
  const [checkboxes, setCheckboxes] = useState({
    all: false,
    termsOfUse: false,
    consentToInformation: false,
  });

  const isAllChecked = Object.values(checkboxes).every(value => value === true);

  const handleCheckboxChange = (updatedCheckboxes) => {
    setCheckboxes(updatedCheckboxes);
  };

  const handleCancelButtonClick = () => {
    navigate('/');
  };

  const handleButtonClick = () => {
    navigate('/join-form');
  };
  
  return (
    <div>
      <Container>
        <FancyText>CO-WORKING</FancyText>
        <Content>
            <div>
              CO-WORKING 회원가입을 위하여 아래의 개인정보 수집이용에 대한 내용을 자세히 읽어 보신 후 동의 여부를 결정하여 주시기 바랍니다.
            </div>
            <Hr/>
            <ValidAgreeCheckbox onCheckboxChange={handleCheckboxChange} />
        </Content>
        <ButtonsDiv>
          {/* 취소 버튼 */}
          <CancelButton 
            type="submit"
            onClick={handleCancelButtonClick}
          >
            취소
          </CancelButton>

          {/* 동의 버튼 */}
          <ActiveButton
            type="button"
            disabled={!isAllChecked}
            onClick={handleButtonClick}
          >
            동의
          </ActiveButton>
        </ButtonsDiv>
      </Container>
    </div>
  );
};

export default PolicyForm;