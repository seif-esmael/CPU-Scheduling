package Scheduler;

import java.util.List;
import Process.Process;

public interface Scheduler {
    ScheduleData schedule(List<Process> processes, int csTime);
}
