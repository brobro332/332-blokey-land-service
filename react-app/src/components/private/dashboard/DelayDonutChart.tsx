import React, { useMemo } from "react";
import { PieChart, Pie, Cell, Tooltip, ResponsiveContainer } from "recharts";
import { Task } from "../../../types/task";

interface DelayDonutChartProps {
  tasks: Task[];
}

const COLORS = ["#ff4d4f", "#40c057"];
const EMPTY_COLOR = "#d1d5db";

const DelayDonutChart: React.FC<DelayDonutChartProps> = ({ tasks }) => {
  const { data, delayedPercent, isEmpty } = useMemo(() => {
    if (!tasks || tasks.length === 0) {
      return {
        isEmpty: true,
        data: [{ name: "없음", value: 1 }],
        delayedPercent: 0,
      };
    }

    const now = new Date();

    const delayed = tasks.filter((task) => {
      if (!task.estimatedEndDate) return false;

      const estimatedEnd = new Date(task.estimatedEndDate);

      if (task.actualEndDate) {
        const actualEnd = new Date(task.actualEndDate);
        return actualEnd > estimatedEnd;
      }

      return (task.progress ?? 0) < 100 && estimatedEnd < now;
    }).length;

    const notDelayed = tasks.length - delayed;

    const percent =
      delayed + notDelayed > 0
        ? Math.round((delayed / (delayed + notDelayed)) * 100)
        : 0;

    return {
      isEmpty: false,
      data: [
        { name: "지연", value: delayed },
        { name: "정상", value: notDelayed },
      ],
      delayedPercent: percent,
    };
  }, [tasks]);

  return (
    <div
      style={{
        position: "relative",
        overflow: "hidden",
        width: "100%",
        height: 300,
        color: "#444",
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      <ResponsiveContainer width="100%" height={250}>
        <PieChart>
          <Pie
            data={data}
            dataKey="value"
            nameKey="name"
            cx="50%"
            cy="50%"
            innerRadius={60}
            outerRadius={110}
            paddingAngle={4}
            stroke="#fff"
            strokeWidth={2}
            isAnimationActive={true}
            animationDuration={800}
          >
            {data.map((entry, index) => (
              <Cell
                key={`cell-${index}`}
                fill={isEmpty ? EMPTY_COLOR : COLORS[index % COLORS.length]}
                style={{ cursor: isEmpty ? "default" : "pointer" }}
              />
            ))}
          </Pie>

          {!isEmpty && (
            <Tooltip
              formatter={(value: number, name: string) => [
                value,
                name === "지연" ? "지연 태스크" : "정상 태스크",
              ]}
              wrapperStyle={{
                fontSize: 14,
                backgroundColor: "rgba(255, 255, 255, 0.9)",
                borderRadius: 8,
                boxShadow: "0 2px 8px rgba(0,0,0,0.15)",
                padding: "6px 12px",
              }}
            />
          )}

          <text
            x="50%"
            y="50%"
            textAnchor="middle"
            dominantBaseline="middle"
            fontSize={28}
            fontWeight="bold"
            fill={isEmpty ? "#aaa" : delayedPercent > 0 ? COLORS[0] : COLORS[1]}
            pointerEvents="none"
          >
            {delayedPercent}%
          </text>
          <text
            x="50%"
            y="60%"
            textAnchor="middle"
            dominantBaseline="middle"
            fontSize={14}
            fill="#666"
            pointerEvents="none"
          >
            지연 비율
          </text>
        </PieChart>
      </ResponsiveContainer>

      {isEmpty ? (
        <div style={{ marginTop: 8, fontSize: 14, color: "#888" }}>
          데이터 없음
        </div>
      ) : (
        <div
          style={{
            marginTop: 4,
            display: "flex",
            gap: 24,
            fontSize: 16,
            fontWeight: "600",
          }}
        >
          <div
            style={{
              color: COLORS[1],
              display: "flex",
              alignItems: "center",
            }}
          >
            <span
              style={{
                display: "inline-block",
                width: 14,
                height: 14,
                backgroundColor: COLORS[1],
                borderRadius: "50%",
                marginRight: 6,
              }}
            />
            정상: {data[1]?.value ?? 0}개
          </div>
          <div
            style={{
              color: COLORS[0],
              display: "flex",
              alignItems: "center",
            }}
          >
            <span
              style={{
                display: "inline-block",
                width: 14,
                height: 14,
                backgroundColor: COLORS[0],
                borderRadius: "50%",
                marginRight: 6,
              }}
            />
            지연: {data[0]?.value ?? 0}개
          </div>
        </div>
      )}
    </div>
  );
};

export default DelayDonutChart;
