package Scheduler;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Process.Process;
import java.util.ArrayList;

public class SJF implements Scheduler {
    @Override
    public ScheduleData schedule(List<Process> processes, int csTime) {

        List<Process> currprocesses = new ArrayList<>();
        List<Process> terminated = new ArrayList<>();
        int curr = 0, n = 0;
        Process currentProcess;
        ScheduleData scheduleData = new ScheduleData();
        Map<Process, List<List<Integer>>> executionMap = new HashMap<>();

        while (n < processes.size()) {
            for (Process p : processes) {
                if (!p.getCompleted()) {
                    if (!currprocesses.contains(p)) {
                        if (p.getArrivalTime() <= curr) {
                            currprocesses.add(p);
                        }
                    }
                }
            }
            // _______________________________________________
            if (!currprocesses.isEmpty()) {
                currprocesses.sort(Comparator.comparingLong(p -> p.getBrustTime()));
                currentProcess = currprocesses.get(0);
                currprocesses.remove(currentProcess);
                currentProcess.setCompleted(true);

                executionMap.put(currentProcess, new ArrayList<>());
                executionMap.get(currentProcess).add(new ArrayList<>());
                executionMap.get(currentProcess).get(executionMap.get(currentProcess).size() - 1).add(curr);
                executionMap.get(currentProcess).get(executionMap.get(currentProcess).size() - 1)
                        .add(curr + currentProcess.getBrustTime());

                curr += currentProcess.getBrustTime();
                currentProcess.setTurnaroundTime(curr - currentProcess.getArrivalTime());
                currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBrustTime());
                terminated.add(currentProcess);
                n++;
                curr += csTime;
            } else {
                curr++;
            }
        }
        // ------------------------------------------------------------------
        System.out.println("(1)Process Execution Order:\n");
        for (Process p : terminated) {
            System.out.print(p.getName() + " ");
        }
        // ------------------------------------------------------------------
        System.out.println("\n(2)Waiting Time for Each Process:");
        for (Process p : terminated) {
            System.out.println(p.getName() + ": " + p.getWaitingTime());
            scheduleData.totalWait += p.getWaitingTime();
        }
        // ------------------------------------------------------------------
        System.out.println("\n(3)Turnaround Time for Each Process:");
        for (Process p : terminated) {
            System.out.println(p.getName() + ": " + p.getTurnaroundTime());
            scheduleData.totalTurnaround += p.getTurnaroundTime();
        }
        // ------------------------------------------------------------------
        scheduleData.avgWait = (double) scheduleData.totalWait / processes.size();
        scheduleData.avgTurnaround = (double) scheduleData.totalTurnaround / processes.size();
        System.out.println("(4)Average Waiting Time: " + scheduleData.avgWait);
        System.out.println("(5)Average Turnaround Time: " + scheduleData.avgTurnaround);

        scheduleData.executionMap = executionMap;
        return scheduleData;
    }
}