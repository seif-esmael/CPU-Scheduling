import GanttGrid from "./GanttGrid";
import GanttBar from "./GanttBar";
import { Process } from "../App";

interface Props {
  data: Process[];
}

const Gantt = ({ data }: Props) => {
  const calculateColumns = () =>
    data
      .map((process) => process.executionMap.length)
      .reduce((prev, current) => current + prev);

  const differenceMap = () =>
    data
      .map((process) => process.executionMap)
      .flat()
      .sort((a, b) => a[0] - b[0])
      .map((exec) => exec[1] - exec[0]);

  const getGridColumnsRatio = () =>
    differenceMap()
      .map((time) => time + "fr")
      .join(" ");

  return (
    <>
      <GanttGrid
        columns={calculateColumns()}
        columnsRatio={getGridColumnsRatio()}
        rows={data.length}
        labels={data.map((process) => process.processName).flat()}
      >
        {data
          .map((process, processID) =>
            process.executionMap.map((exec) => [exec, processID])
          )
          .flat()
          .sort((a, b) => a[0][0] - b[0][0])
          .map((exec, index) => (
            <GanttBar
              hue={data[exec[1]].color}
              rowIndex={exec[1]}
              columnIndex={index}
              exec={exec[0]}
            />
          ))}
      </GanttGrid>
    </>
  );
};

export default Gantt;
