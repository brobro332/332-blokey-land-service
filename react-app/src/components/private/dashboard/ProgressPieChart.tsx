import React from "react";
import { PieChart, Pie, Cell, Legend, Tooltip } from "recharts";

interface ProgressPieChartProps {
  data: {
    name: string;
    value: number;
  }[];
}

const COLORS = ["#4CAF50", "#FFC107", "#F44336"];

const ProgressPieChart: React.FC<ProgressPieChartProps> = ({ data }) => {
  const hasData = data.length > 0;
  const displayData = hasData ? data : [{ name: "데이터 없음", value: 1 }];

  const displayColors = hasData ? COLORS : ["#ccc"];

  return (
    <PieChart width={450} height={300}>
      <Pie
        data={displayData}
        dataKey="value"
        nameKey="name"
        cx="50%"
        cy="50%"
        outerRadius={100}
        label={
          hasData
            ? ({ name, percent = 0 }) =>
                `${name}: ${(percent * 100).toFixed(0)}%`
            : undefined
        }
      >
        {displayData.map((entry, index) => (
          <Cell
            key={`cell-${index}`}
            fill={displayColors[index % displayColors.length]}
            stroke="#ffffff"
            strokeWidth={6}
          />
        ))}
      </Pie>

      {hasData ? (
        <>
          <Tooltip />
          <Legend verticalAlign="bottom" height={36} />
        </>
      ) : (
        <text x={150} y={280} textAnchor="middle" fill="#999" fontSize={14}>
          데이터 없음
        </text>
      )}
    </PieChart>
  );
};

export default ProgressPieChart;
