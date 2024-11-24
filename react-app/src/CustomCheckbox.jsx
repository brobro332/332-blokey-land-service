import React from "react";

function CustomCheckbox({ text, isChecked, onChange }) {
  return (
    <div>
      <label>
        <input
          type="checkbox"
          checked={isChecked}
          onChange={(e) => onChange(e.target.checked)}
        />
      </label>
      &nbsp;{text}
    </div>
  );
}

export default CustomCheckbox;