import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import {
  FancyText,
  Container,
  BorderlessContent,
  ButtonsDiv,
  ActiveButton,
  MintColorText
} from '../../styles/styles';

const JoinCompleteForm = () => {
  const navigate = useNavigate();

  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const name = queryParams.get("name");

  const handleButtonClick = () => {
    navigate('/');
  };
    
  return (
    <div>
      <Container>
        <FancyText>CO-WORKING</FancyText>
        <MintColorText>
          <h4>회원가입 완료</h4>
        </MintColorText>
        <BorderlessContent>
          <MintColorText>
            <span>{name}</span>님의 회원가입이 성공적으로 완료되었습니다. <br/>
            사용자 정보 수정을 원하신다면 [사용자 정보] 메뉴를 이용해주세요.
          </MintColorText>
        </BorderlessContent>
        <br/>
        <ButtonsDiv>
          {/* 홈으로 돌아가기 버튼 */}
          <ActiveButton
            type="button" 
            onClick={handleButtonClick}
          >
            홈으로 돌아가기
          </ActiveButton>
        </ButtonsDiv>
      </Container>
    </div>
  );
};

export default JoinCompleteForm;