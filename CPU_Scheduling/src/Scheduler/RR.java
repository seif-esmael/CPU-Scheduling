package Scheduler;

import Process.Process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RR implements Scheduler {
    private int quantum;
    private int contextSwitchTime = 2; // Set your context switch time
    private ScheduleData scheduleData = new ScheduleData();

    public RR(int quantum) {
        this.quantum = quantum;
    }

    public int getQuantum() {
        return quantum;
    }

    @Override
    public ScheduleData schedule(List<Process> processes, int csTime) {
        List<Process> processesCopy = new ArrayList<>(processes);
        List<Integer> burstTimes = new ArrayList<>();
        List<Integer> waitTimes = new ArrayList<>();
        Map<Process, List<List<Integer>>> executionMap = new HashMap<>();
        int timer = 0;
        List<Integer> turnaround = new ArrayList<>();

        // Initialize wait times
        for (int i = 0; i < processes.size(); i++) {
            waitTimes.add(0);
            turnaround.add(0);
        }
        // ---------------------------------------------------------------------------------------------------
        // Calculate burst times
        for (Process process : processesCopy) {
            burstTimes.add(process.getBrustTime());
        }
        // Print burst times
        System.out.println("Burst Times for Processes:");
        for (int i = 0; i < processes.size(); i++) {
            System.out.println(burstTimes.get(i));
        }
       //--------------------------------------------------------------------------------------------------------
        for (int i = 0; i < processes.size(); i++) {
            int agFactor = calculateAGFactor(processes.get(i));
            System.out.println("AG-Factor for " + processes.get(i).getName() + ": " + agFactor);
        }
      //--------------------------------------------------------------------------------------------------------
        // Schedule processes using Round Robin
        do {
            for (int i = 0; i < processes.size(); i++) {
                if (burstTimes.get(i) > 0) {
                    int executionTime;
                    if (timer % (quantum / 2) == 0) {
                        // Switch to preemptive AG after 50% of quantum time
                        executionTime = 1;  // Execute for 1 unit of time (preemptive AG)
                    } else {
                        // Non-preemptive AG for the first 50% of quantum time
                        executionTime = Math.min(quantum / 2, burstTimes.get(i));
                    }

                    executionMap.putIfAbsent(processes.get(i), new ArrayList<>());
                    executionMap.get(processes.get(i)).add(new ArrayList<>());
                    executionMap.get(processes.get(i)).get(executionMap.get(processes.get(i)).size()-1).add(timer);
                    executionMap.get(processes.get(i)).get(executionMap.get(processes.get(i)).size()-1).add(timer + executionTime);
                    timer += executionTime;

                    System.out.println("Context Switch:");
                    saveState(processes.get(i));

                    // Execute process for the given time
                    for (int j = 0; j < processes.size(); j++) {
                        if (j != i && burstTimes.get(j) > 0) {
                            waitTimes.set(j, waitTimes.get(j) + executionTime);
                        }
                    }

                    burstTimes.set(i, burstTimes.get(i) - executionTime);

                    System.out.println("Context Switch: ");
                    restoreState(processes.get(i));
                }
            }
            timer++;
        } while (isAnyProcessRemaining(burstTimes));
        // ----------------------------------------------------------------------------------------------
        System.out.println("\n waiting Time and Turnaround Time for Each Process:");
        // Calculate the total and average waiting time
        scheduleData.totalWait = 0;
        scheduleData.totalTurnaround = 0;
        for (int i = 0; i < processes.size(); i++) {
            System.out.println("p" + (i + 1) + "wait " + waitTimes.get(i)
                    + " Turnaround: " + (waitTimes.get(i) + processes.get(i).getBrustTime()));
            scheduleData.totalWait += waitTimes.get(i);
            turnaround.set(i, waitTimes.get(i) + processes.get(i).getBrustTime());
            scheduleData.totalTurnaround += turnaround.get(i);
        }

        scheduleData.avgWait = (double) scheduleData.totalWait / processes.size();
        scheduleData.avgTurnaround = (double) scheduleData.totalTurnaround / processes.size();
        System.out.println(" Average Waiting Time: " + scheduleData.avgWait);
        System.out.println(" Average Turnaround Time: " + scheduleData.avgTurnaround);

        scheduleData.executionMap = executionMap;
        return scheduleData;
    }

    private void saveState(Process process) {
        System.out.println("Saving state of process " + process.getName());
    }

    private void restoreState(Process process) {
        System.out.println("Restoring state of process " + process.getName());
    }

    private boolean isAnyProcessRemaining(List<Integer> burstTimes) {
        for (int burstTime : burstTimes) {
            if (burstTime > 0) {
                return true;
            }
        }
        return false;
    }

    private int calculateAGFactor(Process process) {
        int priority = process.getPriority();
        int randomValue = (int) (Math.random() * 20);

        if (randomValue < 10) {
            return randomValue + process.getArrivalTime() + process.getBrustTime();
        } else if (randomValue > 10) {
            return 10 + process.getArrivalTime() + process.getBrustTime();
        } else {
            return priority + process.getArrivalTime() + process.getBrustTime();
        }
    }
}