import React, { useState } from "react";
import CustomCheckbox from "./CustomCheckbox";

function ValidAgreeCheckbox({ onCheckboxChange }) {
  const [checkboxes, setCheckboxes] = useState({
    all: false,
    termsOfUse: false,
    consentToInformation: false,
  });

  const handleCheckboxChange = (name, isChecked) => {
    setCheckboxes((prevState) => {
      const updatedCheckboxes = { ...prevState, [name]: isChecked };

      if (name === "all") {
        updatedCheckboxes.termsOfUse = isChecked;
        updatedCheckboxes.consentToInformation = isChecked;
      }

      if (name !== "all") {
        if (updatedCheckboxes.termsOfUse && updatedCheckboxes.consentToInformation) {
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
      <CustomCheckbox
        text="개인정보 수집에 동의합니다. (필수)"
        isChecked={checkboxes.consentToInformation}
        onChange={(isChecked) => handleCheckboxChange("consentToInformation", isChecked)}
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

export default ValidAgreeCheckbox;