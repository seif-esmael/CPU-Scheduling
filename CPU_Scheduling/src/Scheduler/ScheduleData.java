package Scheduler;

import java.util.List;
import Process.Process;

public class ScheduleData {
    public int totalWait = 0;
    public int totalTurnaround = 0;
    public double avgWait = 0;
    public double avgTurnaround = 0;
    public List<Process> processes;
}
