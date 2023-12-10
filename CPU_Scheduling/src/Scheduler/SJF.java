package Scheduler;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Process.Process;
import java.util.ArrayList;
import java.util.Iterator;

public class SJF implements Scheduler {

    @Override
    public ScheduleData schedule(List<Process> processes, int csTime) {

        List<Process> processesCopy = new ArrayList<>(processes);
        List<Process> processesWithArrivalZero = new ArrayList<>();

        Iterator<Process> iterator = processesCopy.iterator();
        while (iterator.hasNext()) {
            Process p = iterator.next();
            if (p.getArrivalTime() == 0) {
                processesWithArrivalZero.add(p);
                iterator.remove();
            }
        }
        processesWithArrivalZero.sort(Comparator.comparingLong(p -> p.getBrustTime()));
        processesCopy.sort(Comparator.comparingLong(p -> p.getBrustTime()));

        int curr = 0;
        ScheduleData scheduleData = new ScheduleData();

        System.out.println("\n(1)Process Execution Order:");

        Map<Process, List<List<Integer>>> executionMap = new HashMap<>();
        for (Process p : processesWithArrivalZero) {

            executionMap.put(p, new ArrayList<>());
            executionMap.get(p).add(new ArrayList<>());
            executionMap.get(p).get(executionMap.get(p).size()-1).add(curr);
            executionMap.get(p).get(executionMap.get(p).size()-1).add(curr + p.getBrustTime());

            System.out.print(p.getName() + " ");

            p.setWaitingTime(curr - p.getArrivalTime());
            curr += p.getBrustTime();
            p.setTurnaroundTime(p.getBrustTime());

            curr += csTime;

            scheduleData.totalWait += p.getWaitingTime();
            scheduleData.totalTurnaround += p.getTurnaroundTime();
        }
        // _________________________________________________________
        for (Process p : processesCopy) {
            if (p.getArrivalTime() > curr) {
                curr = p.getArrivalTime();
            }

            executionMap.put(p, new ArrayList<>());
            executionMap.get(p).add(new ArrayList<>());
            executionMap.get(p).get(executionMap.get(p).size()-1).add(curr);
            executionMap.get(p).get(executionMap.get(p).size()-1).add(curr + p.getBrustTime());

            System.out.print(p.getName() + " ");

            p.setWaitingTime(curr - p.getArrivalTime());
            curr += p.getBrustTime();
            p.setTurnaroundTime(p.getBrustTime());

            curr += csTime;

            scheduleData.totalWait += p.getWaitingTime();
            scheduleData.totalTurnaround += p.getTurnaroundTime();
        }
        // ------------------------------------------------------------------
        System.out.println("\n(2)Waiting Time for Each Process:");
        for (Process p : processesWithArrivalZero) {
            System.out.println(p.getName() + ": " + p.getWaitingTime());
        }
        for (Process p : processesCopy) {
            System.out.println(p.getName() + ": " + p.getWaitingTime());
        }
        // ------------------------------------------------------------------
        System.out.println("\n(3)Turnaround Time for Each Process:");
        for (Process p : processesWithArrivalZero) {
            System.out.println(p.getName() + ": " + p.getTurnaroundTime());
        }
        for (Process p : processesCopy) {
            System.out.println(p.getName() + ": " + p.getTurnaroundTime());
        }
        // ------------------------------------------------------------------
        scheduleData.avgWait = (double) scheduleData.totalWait
                / (processesCopy.size() + processesWithArrivalZero.size());
        scheduleData.avgTurnaround = (double) scheduleData.totalTurnaround
                / (processesCopy.size() + processesWithArrivalZero.size());

        System.out.println("(4)Average Waiting Time: " + scheduleData.avgWait);
        System.out.println("(5)Average Turnaround: " + scheduleData.avgTurnaround);

        scheduleData.executionMap = executionMap;
        return scheduleData;
    }

}