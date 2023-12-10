import "./App.css";
import Gantt from "./components/Gantt";
import Schedule from "./DB/schedule.json";

export interface Process {
  processName: string;
  priority: Number;
  color: string;
  executionMap: number[][];
}

function App() {
  const scheduleData: Process[] = Schedule as Process[];
  return (
    <>
      <Gantt
        data={scheduleData.sort((a, b) =>
          a.processName.localeCompare(b.processName)
        )}
      />
    </>
  );
}

export default App;
