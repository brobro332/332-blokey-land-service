import React, { useState } from "react";
import CustomCheckbox from "../tags/CustomCheckbox";
import CustomDropdown from "../tags/CustomDropdown";

function ValidAgreeCheckbox({ onCheckboxChange }) {
  const [checkboxes, setCheckboxes] = useState({
    all: false,
    termsOfUse: false,
    privacyPolicy: false,
  });

  const handleCheckboxChange = (name, isChecked) => {
    setCheckboxes((prevState) => {
      const updatedCheckboxes = { ...prevState, [name]: isChecked };

      if (name === "all") {
        updatedCheckboxes.termsOfUse = isChecked;
        updatedCheckboxes.privacyPolicy = isChecked;
      }

      if (name !== "all") {
        if (updatedCheckboxes.termsOfUse && updatedCheckboxes.privacyPolicy) {
          updatedCheckboxes.all = true;
        } else {
          updatedCheckboxes.all = false;
        }
      }

      onCheckboxChange(updatedCheckboxes);

      return updatedCheckboxes;
    });
  };

  return (
    <div>
      <CustomCheckbox
        text="전체 약관에 동의합니다."
        isChecked={checkboxes.all}
        onChange={(isChecked) => handleCheckboxChange("all", isChecked)}
      />
      
      <hr style={styles.hr} />
      
      <CustomCheckbox
        text="이용약관에 동의합니다. (필수)"
        isChecked={checkboxes.termsOfUse}
        onChange={(isChecked) => handleCheckboxChange("termsOfUse", isChecked)}
      />
      <CustomDropdown
        title="자세히 보기" 
        content={termsOfUseContent} 
      />

      <CustomCheckbox
        text="개인정보 수집에 동의합니다. (필수)"
        isChecked={checkboxes.privacyPolicy}
        onChange={(isChecked) => handleCheckboxChange("consentToInformation", isChecked)}
      />
      <CustomDropdown 
        title="자세히 보기" 
        content={privacyPolicyContent}
      />
    </div>
  );
}

const styles = {
  hr: {
    backgroundColor: "#ddd",
    width: "100%",
    height: "1px",
    border: "0",
  },
};

const termsOfUseContent = `
<h3>이용 약관</h3>

<h4>제1조 (목적)</h4>
<p>
  본 약관은 <strong>[서비스명]</strong>(이하 "서비스")이 제공하는 서비스의 이용과 관련하여 
  서비스와 사용자 간의 권리, 의무 및 책임 사항을 규정하는 것을 목적으로 합니다.
</p>

<h4>제2조 (용어의 정의)</h4>
<ul>
  <li>
    "서비스"란 회사가 제공하는 웹사이트 및 애플리케이션 등을 통해 이용할 수 있는 모든 서비스를 의미합니다.
  </li>
  <li>"회원"이란 본 약관에 동의하고 서비스가 제공하는 서비스를 이용하는 자를 말합니다.</li>
  <li>"콘텐츠"란 회원이 서비스를 통해 작성, 게시, 전송한 텍스트, 이미지, 영상 등을 포함합니다.</li>
</ul>

<h4>제3조 (약관의 효력과 변경)</h4>
<ol>
  <li>본 약관은 회원이 동의함으로써 효력을 발생합니다.</li>
  <li>
    서비스는 관련 법령에 따라 약관을 변경할 수 있으며, 변경된 약관은 사전에 공지합니다.
  </li>
  <li>
    변경된 약관에 동의하지 않을 경우 회원은 서비스 이용을 중단할 수 있으며, 계속 이용할 경우 변경된 약관에 동의한 것으로 간주됩니다.
  </li>
</ol>

<h4>제4조 (회원 가입)</h4>
<ol>
  <li>
    회원 가입은 서비스가 제공하는 가입 양식에 따라 신청하며, 서비스가 이를 승인함으로써 성립됩니다.
  </li>
  <li>
    서비스는 다음과 같은 경우 회원 가입 신청을 승인하지 않을 수 있습니다:
    <ul>
      <li>가입 신청자가 이전에 약관을 위반하여 회원 자격을 상실한 경우</li>
      <li>허위 정보를 제공한 경우</li>
    </ul>
  </li>
</ol>

<h4>제5조 (회원의 의무)</h4>
<ul>
  <li>회원은 서비스 이용 시 관련 법령 및 본 약관을 준수해야 합니다.</li>
  <li>
    회원은 다음 행위를 해서는 안 됩니다:
    <ul>
      <li>타인의 개인정보를 무단으로 수집, 이용하는 행위</li>
      <li>서비스의 정상적인 운영을 방해하는 행위</li>
      <li>저작권 등 타인의 권리를 침해하는 행위</li>
    </ul>
  </li>
</ul>

<h4>제6조 (서비스의 의무)</h4>
<ol>
  <li>서비스는 회원이 안정적으로 서비스를 이용할 수 있도록 최선을 다합니다.</li>
  <li>
    서비스는 회원의 개인정보를 보호하기 위해 노력하며, 관련 법령에 따라 이를 관리합니다.
  </li>
</ol>

<h4>제7조 (서비스의 이용 제한)</h4>
<ol>
  <li>
    서비스는 다음의 경우 회원의 서비스 이용을 제한하거나 중단할 수 있습니다:
    <ul>
      <li>회원이 약관을 위반한 경우</li>
      <li>법령에 따라 서비스 제공이 제한되거나 중단된 경우</li>
    </ul>
  </li>
  <li>회원은 서비스 이용 제한에 대해 이의를 제기할 수 있습니다.</li>
</ol>

<h4>제8조 (책임의 한계)</h4>
<ol>
  <li>서비스는 회원이 제공한 정보의 신뢰성과 정확성을 보증하지 않습니다.</li>
  <li>
    서비스는 회원이 서비스를 통해 얻은 정보에 대해 발생한 손해에 대해 책임을 지지 않습니다.
  </li>
</ol>

<h4>제9조 (약관 외 준칙)</h4>
<p>
  본 약관에 명시되지 않은 사항에 대해서는 관계 법령 및 관례에 따릅니다.
</p>
`;

const privacyPolicyContent = `
<h3>개인정보 수집 및 이용 동의</h3>

<h4>1. 수집하는 개인정보 항목</h4>
<ul>
  <li>이름</li>
  <li>이메일 주소</li>
  <li>전화번호</li>
</ul>

<h4>2. 개인정보의 수집 및 이용 목적</h4>
<p>
  수집한 개인정보는 다음의 목적으로 이용됩니다:
</p>
<ul>
  <li>서비스 이용자 확인 및 본인 인증</li>
  <li>서비스 관련 공지사항 전달</li>
  <li>고객 문의에 대한 응답 및 상담</li>
  <li>마케팅 및 프로모션 정보 제공 (별도의 동의 시)</li>
</ul>

<h4>3. 개인정보의 보유 및 이용 기간</h4>
<p>
  개인정보는 수집 및 이용 목적이 달성된 후 지체 없이 파기됩니다. 다만, 관련 법령에 따라 일정 기간 보관이 필요한 경우에는 해당 기간 동안 보관됩니다:
</p>
<ul>
  <li>전자상거래 등에서의 소비자 보호에 관한 법률: 5년</li>
  <li>통신비밀보호법: 3개월</li>
</ul>

<h4>4. 개인정보 제공 동의 거부의 권리</h4>
<p>
  귀하는 개인정보 제공에 대한 동의를 거부할 권리가 있습니다. 다만, 동의를 거부할 경우 서비스 이용에 제한이 있을 수 있습니다.
</p>

<h4>5. 개인정보 수집 및 이용 동의</h4>
<p>
  위 내용에 동의하며, 서비스 이용에 필요한 개인정보를 제공하는 데 동의합니다.
</p>
`;

export default ValidAgreeCheckbox;