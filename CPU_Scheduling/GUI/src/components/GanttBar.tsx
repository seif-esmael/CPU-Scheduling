import Style from "./GanttBar.module.css";

interface Props {
  hue: number;
  rowIndex: number;
  columnIndex: number;
  exec: number[];
}

const GanttBar = ({ hue, rowIndex, columnIndex, exec }: Props) => {
  const style = {
    borderColor: `hsl(${hue * 360}, 50%, 40%)`,
    backgroundColor: `hsl(${hue * 360}, 50%, 55%)`,
    gridRow: rowIndex + 1,
    gridColumn: columnIndex + 2,
  };
  return (
    <div
      className={Style.bar}
      style={style}
      data-toolTip={`Start Time: ${exec[0]} - End Time: ${exec[1]}`}
    ></div>
  );
};

export default GanttBar;
