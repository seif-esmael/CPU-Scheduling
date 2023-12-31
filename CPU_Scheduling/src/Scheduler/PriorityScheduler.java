package Scheduler;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import Process.Process;

public class PriorityScheduler implements Scheduler {
    // used to create a process priority Comparator object for priorty queue
    private int processComparatorByPriority(Process first, Process second) {
        return first.getPriority() - second.getPriority();
    }

    // returns a priorty queue with processes' priorty reduced by one
    public void increasePriority(AbstractQueue<Process> pq) {
        pq.forEach(process -> process.setPriority(Math.max(process.getPriority() - 1, 0)));

    }

    @Override
    public ScheduleData schedule(List<Process> processes, int csTime) {
        // instantiates a pq with the priorty comparator
        processes.sort((p1, p2) -> p1.getArrivalTime() - p2.getArrivalTime());
        AbstractQueue<Process> pq = new PriorityQueue<>(this::processComparatorByPriority);
        List<Integer> processesPriority = new ArrayList<>();
        for (Process process : processes)
            processesPriority.add(process.getPriority());

        // timer to simulate processes arrival
        int timer = 0;

        // stores data related to the scheduling process
        ScheduleData scheduleData = new ScheduleData();

        Map<Process, List<List<Integer>>> executionMap = new HashMap<>();
        // checks to see if we covered all the arrived processes and processed all of
        // them
        for (int i = 0; i < processes.size() || !pq.isEmpty();) {
            for (int j = i; j < processes.size(); j++) {
                // adds processes that have arrived to the pq
                // if no proccesses have arrived it breaks out of the loop
                if (processes.get(j).getArrivalTime() <= timer) {
                    increasePriority(pq);
                    pq.add(processes.get(j));
                    i++;
                } else
                    break;
            }

            if (!pq.isEmpty()) {
                Process currentProcess = pq.remove();
                // prints content of the pq
                Iterator<Process> it = pq.iterator();
                it.forEachRemaining(p -> System.out.println(p.getName() + " : " +
                        p.getPriority()));
                System.out.println("------------------------------");
                System.out.println("------------------------------");
                System.out.println(currentProcess.getName() + " Done!");
                System.out.println("------------------------------");

                executionMap.put(currentProcess, new ArrayList<>());
                executionMap.get(currentProcess).add(new ArrayList<>());
                executionMap.get(currentProcess).get(executionMap.get(currentProcess).size() - 1).add(timer);
                executionMap.get(currentProcess).get(executionMap.get(currentProcess).size() - 1)
                        .add(timer + currentProcess.getBrustTime());
                timer += currentProcess.getBrustTime();
                currentProcess.setTurnaroundTime(timer - currentProcess.getArrivalTime());
                currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBrustTime());

                scheduleData.totalWait += currentProcess.getWaitingTime();
                scheduleData.totalTurnaround += currentProcess.getTurnaroundTime();
            } else
                timer++;

        }

        for (int i = 0; i < processes.size(); i++)
            processes.get(i).setPriority(processesPriority.get(i));

        System.out.println("Waiting time for:");
        for (Process p : processes) {
            System.out.println(p.getName() + ":" + p.getWaitingTime());
        }

        System.out.println("TurnAround time for:");
        for (Process p : processes) {
            System.out.println(p.getName() + ":" + p.getTurnaroundTime());
        }

        scheduleData.avgWait = (double) scheduleData.totalWait / processes.size();
        scheduleData.avgTurnaround = (double) scheduleData.totalTurnaround / processes.size();
        System.out.println("Average Waiting Time: " + scheduleData.avgWait);
        System.out.println("Average Turnaround Time: " + scheduleData.avgTurnaround);

        scheduleData.executionMap = executionMap;
        return scheduleData;
    }
}