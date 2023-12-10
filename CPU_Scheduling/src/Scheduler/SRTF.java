package Scheduler;

import Scheduler.Scheduler;
import Process.Process;

import java.util.*;

public class SRTF implements Scheduler {

    @Override
    public ScheduleData schedule(List<Process> processes, int csTime) {
        int currentTime = 0;
        int completedProcesses = 0;
        List<Process> readyQueue = new ArrayList<>();
        ScheduleData scheduleData = new ScheduleData();
        Set<String> addedProcessIds = new HashSet<>();

        while (completedProcesses < processes.size()) {
            System.out.println("Current Time: " + currentTime);
            System.out.println("Completed Processes: " + completedProcesses);

            for (Process p : processes)
            {
                if (p.getArrivalTime() <= currentTime && p.getRemainingTime() > 0)
                {
                    if (!addedProcessIds.contains(p.getName()))
                    {
                        readyQueue.add(p);
                        addedProcessIds.add(p.getName());
                    }
                }
            }
            readyQueue.sort(Comparator.comparingInt(Process::getRemainingTime));
            for (Process p : readyQueue) {
                System.out.println("Name: " + p.getName() + ", Arrival Time: " + p.getArrivalTime() + ", Remaining Time: " + p.getRemainingTime());
            }

            if (!readyQueue.isEmpty()) {

                Process shortest = readyQueue.get(0);
                System.out.println("Executing process " + shortest.getName());

                if (shortest.getRemainingTime() > 0) {
                    shortest.setRemainingTime(shortest.getRemainingTime() - 1);
                    currentTime++;
                    System.out.println("Updated remaining time of process " + shortest.getName() + " to " + shortest.getRemainingTime());
                }

                if (shortest.getRemainingTime() == 0) {
                    shortest.setTurnaroundTime(currentTime - shortest.getArrivalTime());
                    shortest.setWaitingTime(shortest.getTurnaroundTime() - shortest.getBrustTime());
                    completedProcesses++;
                    readyQueue.remove(shortest);
                    System.out.println("Process " + shortest.getName() + " completed execution");
                    System.out.println("Turnaround Time for " + shortest.getName() + ": " + shortest.getTurnaroundTime());
                    System.out.println("Waiting Time for " + shortest.getName() + ": " + shortest.getWaitingTime());
                }

            }
            else {
                currentTime++;
            }
        }

        for (Process p : processes) {
            scheduleData.totalWait += p.getWaitingTime();
            scheduleData.totalTurnaround += p.getTurnaroundTime();
        }

        scheduleData.avgWait = (double) scheduleData.totalWait / processes.size();
        scheduleData.avgTurnaround = (double) scheduleData.totalTurnaround / processes.size();

        System.out.println("\nAverage Waiting Time: " + scheduleData.avgWait);
        System.out.println("Average Turnaround Time: " + scheduleData.avgTurnaround);

        return scheduleData;
    }
}
