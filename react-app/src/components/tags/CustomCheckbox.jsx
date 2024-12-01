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
        &nbsp;{text}
      </label>
    </div>
  );
}

export default CustomCheckbox;