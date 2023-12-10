package Scheduler;

import Process.Process;

import java.util.ArrayList;
import java.util.List;

public class RR implements Scheduler {
    private int quantum ;
    private int contextSwitchTime = 2; // Set your context switch time
    private ScheduleData scheduleData = new ScheduleData();

    public void setQuantum(int quantum) {
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

        // Initialize wait times
        for (int i = 0; i < processes.size(); i++) {
            waitTimes.add(0);
        }
        //----------------------------------------------------------------------------------------------
        // Calculate burst times
        for (Process process : processesCopy) {
            burstTimes.add(process.getBrustTime());
        }
        // Print burst times
        System.out.println("Burst Times for Processes:");
        for (int i = 0; i < processes.size(); i++) {
            System.out.println(burstTimes.get(i));
        }



        // Schedule processes using Round Robin
        do {
            for (int i = 0; i < processes.size(); i++) {
                if (burstTimes.get(i) > 0) {
                    int executionTime = Math.min(quantum, burstTimes.get(i));


                    System.out.println("Context Switch:");
                    saveState(processes.get(i));
                    // Execute process for the given quantum time
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
        } while (isAnyProcessRemaining(burstTimes));
        //----------------------------------------------------------------------------------------------
        System.out.println("\n(2) waiting Time for Each Process:");
        // Calculate the total and average waiting time
        scheduleData.totalWait = 0;
        for (int i = 0; i < processes.size(); i++) {
            System.out.println("p" + (i + 1) + " " + waitTimes.get(i));
            scheduleData.totalWait += waitTimes.get(i);
        }

        scheduleData.avgWait = (double) scheduleData.totalWait / processes.size();
        System.out.println("(4) Average Waiting Time: " + scheduleData.avgWait);

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
}
