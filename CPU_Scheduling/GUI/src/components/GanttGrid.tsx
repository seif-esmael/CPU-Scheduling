import React, { ReactNode } from "react";
import Style from "./GanttGrid.module.css";
interface Props {
  rows: number;
  columns: number;
  columnsRatio: string;
  labels: string[];
  children: ReactNode;
}

const GanttGrid = ({
  rows,
  columns,
  columnsRatio,
  labels,
  children,
}: Props) => {
  const style = {
    gridTemplateRows: `repeat(${rows}, 1fr)`,
    gridTemplateColumns: "10rem " + columnsRatio,
  };
  return (
    <article className={Style.mainGrid} style={style}>
      {labels.map((label, index) => (
        <span className={Style.labelContainer} style={{ gridRow: index + 1 }}>
          <p className={Style.label}>{label}</p>
        </span>
      ))}
      {children}
    </article>
  );
};

export default GanttGrid;
