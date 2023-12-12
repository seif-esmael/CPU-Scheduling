package Scheduler;

import Process.Process;

import java.util.*;

public class RR implements Scheduler {
    private int quantum;
    private Process currPro;
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
        Queue<Process> readyQueue = new LinkedList<>();
        List<Integer> remainingTime = new ArrayList<>();
        List<Process> results = new ArrayList<>();
        List<String> exOrder = new ArrayList<>();


        for (Process p : processesCopy) {
            remainingTime.add(p.getBrustTime());
        }

        int currentTime = 0;
     
        processesCopy.sort(Comparator.comparingLong(p -> p.getArrivalTime()));
        currPro = processesCopy.get(0);
        currentTime = currPro.getArrivalTime();
        processesCopy.remove(currPro);
        boolean cont = false;
        System.out.println("Execution Order:");
        while (!readyQueue.isEmpty() || !processesCopy.isEmpty() || cont) {
            // Add processes that have arrived at the current time to the ready queue
            isReady(processesCopy, currentTime, readyQueue);
            if (currPro.getRemainingTime() - quantum > 0) {
                currPro.setRemainingTime(currPro.getRemainingTime() - quantum);
                currentTime += quantum;
            } else {
                currentTime += currPro.getRemainingTime();
                currPro.setRemainingTime(0);
            }
            isReady(processesCopy, currentTime, readyQueue);
            if (!readyQueue.isEmpty()) {
                Process oldPro = currPro;
                currPro = readyQueue.poll(); //p1
                if (oldPro.getRemainingTime() > 0) {
                    readyQueue.add(oldPro);

                } else {
                    oldPro.setTurnaroundTime(currentTime - oldPro.getArrivalTime());
                    oldPro.setWaitingTime(oldPro.getTurnaroundTime() - oldPro.getBrustTime());
                    results.add(oldPro);
                }
                exOrder.add(oldPro.getName());
                currentTime += csTime;
            }
            if (readyQueue.isEmpty()) {
                if (currPro.getRemainingTime() > 0) {
                    cont = true;
                } else {
                    cont = false;
                    currPro.setTurnaroundTime(currentTime - currPro.getArrivalTime());
                    currPro.setWaitingTime(currPro.getTurnaroundTime() - currPro.getBrustTime());
                    results.add(currPro);
                    exOrder.add(currPro.getName());
                }
            }
        }
        // Display Execution Order
        for (String string : exOrder) {
            System.out.print(string + " ");
        }
        // Display waiting times for each process
        System.out.println("\nWaiting Times for Each Process:");
        for (Process p : results) {
            System.out.println(p.getName() + ": " + p.getWaitingTime());
            scheduleData.totalWait += p.getWaitingTime();
        }

        // Calculate turnaround time for each process
        System.out.println("Turnaround Time for Each Process:");
        for (Process p : results) {
            System.out.println(p.getName() + ": " + p.getTurnaroundTime());
            scheduleData.totalTurnaround += p.getTurnaroundTime();
        }

        scheduleData.avgWait = (double) scheduleData.totalWait / processes.size();
        scheduleData.avgTurnaround = (double) scheduleData.totalTurnaround / processes.size();

        System.out.println("Total Waiting Time: " + scheduleData.totalWait);
        System.out.println("Total Turnaround Time: " + scheduleData.totalTurnaround);
        System.out.println("Average Waiting Time: " + scheduleData.avgWait);
        System.out.println("Average Turnaround Time: " + scheduleData.avgTurnaround);
        
        for (int i = 0; i < processes.size(); i++) {
            int agFactor = calculateAGFactor(processes.get(i));
            System.out.println("AG-Factor for " + processes.get(i).getName() + ": " + agFactor);
        }
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


    public void isReady(List<Process> processesCopy, int currentTime, Queue<Process> readyQueue) {
        List<Process> temp = new ArrayList<>();
        for (Process p : processesCopy) {
            if (p.getArrivalTime() <= currentTime) {
                readyQueue.add(p);
                temp.add(p);
            }
        }
        for (Process p : temp) {
            processesCopy.remove(p);
        }
    }
    private int calculateAGFactor(Process process) {
        int priority = process.getPriority();
        int randomFunction = (int) (Math.random() * 20);
        int agFactor;
        if (randomFunction < 10) {
            agFactor = randomFunction + process.getArrivalTime() + process.getBrustTime();
        } else if (randomFunction > 10) {
            agFactor = 10 + process.getArrivalTime() + process.getBrustTime();
        } else {
            agFactor = priority + process.getArrivalTime() + process.getBrustTime();
        }
        return agFactor;
    }
}
