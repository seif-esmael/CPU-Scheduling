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
        Map<String, Integer> waitingTimeMap = new HashMap<>();
        Map<Process, List<List<Integer>>> executionMap = new HashMap<>();
        while (completedProcesses < processes.size()) {
            System.out.println("Current Time: " + currentTime);
            System.out.println("Completed Processes: " + completedProcesses);

            for (Process p : processes) {
                if (p.getArrivalTime() <= currentTime && p.getRemainingTime() > 0) {
                    if (!readyQueue.contains(p)) {
                        readyQueue.add(p);
                        waitingTimeMap.put(p.getName(), 0);
                    }
                }
            }

            readyQueue.sort(
                    Comparator.comparingInt(p -> p.getRemainingTime() + waitingTimeMap.getOrDefault(p.getName(), 0)));

            if (!readyQueue.isEmpty()) {
                Process shortest = readyQueue.get(0);
                System.out.println("Executing process " + shortest.getName());

                if (shortest.getRemainingTime() > 0) {
                    shortest.setRemainingTime(shortest.getRemainingTime() - 1);
                    executionMap.putIfAbsent(shortest, new ArrayList<>());
                    executionMap.get(shortest).add(new ArrayList<>());
                    executionMap.get(shortest).getLast().add(currentTime);
                    executionMap.get(shortest).getLast().add(currentTime + 1);

                    currentTime++;
                    System.out.println("Updated remaining time of process " + shortest.getName() + " to "
                            + shortest.getRemainingTime());
                    // Increment waiting time for other processes in the queue
                    for (Process p : readyQueue) {
                        if (p != shortest) {
                            waitingTimeMap.put(p.getName(), waitingTimeMap.getOrDefault(p.getName(), 0) + 1);
                        }
                    }
                }

                if (shortest.getRemainingTime() == 0) {
                    shortest.setTurnaroundTime(currentTime - shortest.getArrivalTime());
                    shortest.setWaitingTime(shortest.getTurnaroundTime() - shortest.getBrustTime());
                    completedProcesses++;
                    readyQueue.remove(shortest);
                    waitingTimeMap.remove(shortest.getName()); // Remove waiting time for completed process
                    System.out.println("Process " + shortest.getName() + " completed execution");
                    System.out
                            .println("Turnaround Time for " + shortest.getName() + ": " + shortest.getTurnaroundTime());
                    System.out.println("Waiting Time for " + shortest.getName() + ": " + shortest.getWaitingTime());
                }
            } else {
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

        scheduleData.executionMap = executionMap;

        return scheduleData;
    }
}
