import Style from "./GanttBar.module.css";

interface Props {
  hue: number;
  rowIndex: number;
  columnIndex: number;
}

const GanttBar = ({ hue, rowIndex, columnIndex }: Props) => {
  const style = {
    borderColor: `hsl(${hue * 360}, 50%, 40%)`,
    backgroundColor: `hsl(${hue * 360}, 50%, 55%)`,
    gridRow: rowIndex + 1,
    gridColumn: columnIndex + 2,
  };
  return <div className={Style.bar} style={style}></div>;
};

export default GanttBar;
