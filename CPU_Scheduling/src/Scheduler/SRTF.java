package Scheduler;
import Process.Process;

import java.util.ArrayList;
import java.util.List;

public class SRTF implements Scheduler {

    @Override
    public ScheduleData schedule(List<Process> processes, int csTime) {
        int currentTime = 0;
        int completedProcesses = 0;
        int minProcessIndex = 0;

        ScheduleData scheduleData = new ScheduleData();
        List<Process> Completed = new ArrayList<>();

        while (completedProcesses != processes.size()) {
            minProcessIndex = -1;

            for (int i = 0; i < processes.size(); i++) {
                Process p = processes.get(i);

                if (p.getArrivalTime() <= currentTime && p.getRemainingTime() > 0) {
                    if (minProcessIndex == -1 || p.getRemainingTime() < processes.get(minProcessIndex).getRemainingTime()) {
                        minProcessIndex = i;
                    }
                }
            }

            if (minProcessIndex == -1) {
                currentTime++;
                continue;
            }

            Process currentP = processes.get(minProcessIndex);
            currentP.setRemainingTime(currentP.getRemainingTime() - 1);
            currentTime++;

            if (currentP.getRemainingTime() == 0)
            {
                completedProcesses++;
                Completed.add(currentP);
                currentP.setCompletionTime(currentTime);
                minProcessIndex = -1;
                currentP.setTurnaroundTime(currentP.getCompletionTime() - currentP.getArrivalTime());
                currentP.setWaitingTime(currentP.getCompletionTime() - currentP.getArrivalTime() - currentP.getBrustTime());
                scheduleData.totalTurnaround += currentP.getTurnaroundTime();
                scheduleData.totalWait += currentP.getWaitingTime();

            }
        }
        scheduleData.processes = Completed;
        scheduleData.avgWait = (double) scheduleData.totalWait / Completed.size();
        scheduleData.avgTurnaround = (double) scheduleData.totalTurnaround / Completed.size();
        return scheduleData;
    }
}
