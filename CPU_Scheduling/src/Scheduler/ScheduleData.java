package Scheduler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import Process.Process;

public class ScheduleData {
    public int totalWait = 0;
    public int totalTurnaround = 0;
    public double avgWait = 0;
    public double avgTurnaround = 0;
    public Map<Process, List<List<Integer>>> executionMap;

    public String parse() {
        String result = "[";
        for (Map.Entry<Process, List<List<Integer>>> entry : executionMap.entrySet()) {
            result += "{";
            result += "\"processName\":" + '"' + entry.getKey().getName() + "\",";
            result += "\"priority\":" + '"' + entry.getKey().getPriority() + "\",";
            result += "\"color\":" + '"' + entry.getKey().getColor() + "\",";
            result += "\"executionMap\":";
            result += "[";
            for (List<Integer> exec : entry.getValue()) {
                result += "[" + exec.stream().map(Object::toString).collect(Collectors.joining(",")) + "],";
            }
            result = result.substring(0, result.length() - 1);
            result += "]";
            result += "},";
        }
        result = result.substring(0, result.length() - 1);
        result += "]";
        return result;
    }
}
