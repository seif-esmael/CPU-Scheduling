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
    public AbstractQueue<Process> reducePriority(AbstractQueue<Process> oldPriorityQueue) {
        AbstractQueue<Process> pq = new PriorityQueue<>(this::processComparatorByPriority);
        while (!oldPriorityQueue.isEmpty()) {
            Process topProcess = oldPriorityQueue.remove();
            topProcess.setPriority(topProcess.getPriority() - 1);
            pq.add(topProcess);
        }
        return pq;

    }

    @Override
    public ScheduleData schedule(List<Process> processes, int csTime) {
        // instantiates a pq with the priorty comparator
        AbstractQueue<Process> pq = new PriorityQueue<>(this::processComparatorByPriority);

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
                executionMap.get(currentProcess).get(executionMap.get(currentProcess).size()-1).add(timer);
                executionMap.get(currentProcess).get(executionMap.get(currentProcess).size()-1).add(timer + currentProcess.getBrustTime());
                currentProcess.setWaitingTime(timer - currentProcess.getArrivalTime());
                timer += currentProcess.getBrustTime();
                currentProcess.setTurnaroundTime(timer - currentProcess.getArrivalTime());
                pq = reducePriority(pq);

                scheduleData.totalWait += currentProcess.getWaitingTime();
                scheduleData.totalTurnaround += currentProcess.getTurnaroundTime();
            }
            timer += csTime;

        }
        scheduleData.avgWait = (double) scheduleData.totalWait / processes.size();
        scheduleData.avgTurnaround = (double) scheduleData.totalTurnaround / processes.size();
        System.out.println("Average Waiting Time: " + scheduleData.avgWait);
        System.out.println("Average Turnaround Time: " + scheduleData.avgTurnaround);

        scheduleData.executionMap = executionMap;
        return scheduleData;
    }
}