package Scheduler;

import java.util.Comparator;
import java.util.List;
import Process.Process;

public class SJF implements Scheduler {

    @Override
    public ScheduleData schedule(List<Process> processes, int csTime) {
        processes.sort(Comparator.comparingLong(p -> p.getBrustTime()));

        int curr = 0;
        ScheduleData scheduleData = new ScheduleData();

        System.out.println("\n(1)Process Execution Order:");

        for (Process p : processes) {
            if (p.getArrivalTime() > curr) {
                curr = p.getArrivalTime();
            }

            System.out.print(p.getName() + " ");

            p.setWaitingTime(curr);
            curr += p.getBrustTime();
            p.setTurnaroundTime(curr - p.getArrivalTime());

            curr += csTime;

            scheduleData.totalWait += p.getWaitingTime();
            scheduleData.totalTurnaround += p.getTurnaroundTime();
        }
        System.out.println("\n(2)Waiting Time for Each Process:");
        for (Process p : processes) {
            System.out.println(p.getName() + ": " + p.getWaitingTime());
        }
        System.out.println("\n(3)Turnaround Time for Each Process:");
        for (Process p : processes) {
            System.out.println(p.getName() + ": " + p.getTurnaroundTime());
        }

        scheduleData.avgWait = (double) scheduleData.totalWait / processes.size();
        scheduleData.avgTurnaround = (double) scheduleData.totalTurnaround / processes.size();

        System.out.println("Average Turnaround: " + scheduleData.avgTurnaround);
        System.out.println("Average Waiting Time: " + scheduleData.avgWait);

        scheduleData.processes = processes;
        return scheduleData;
    }

}
