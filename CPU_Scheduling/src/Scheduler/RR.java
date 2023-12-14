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
        List<Process> QuantumList = new ArrayList<>(processes);
        Queue<Process> readyQueue = new LinkedList<>();
        List<Integer> remainingTime = new ArrayList<>();
        List<Process> results = new ArrayList<>();
        List<String> exOrder = new ArrayList<>();
        Map<Process, List<List<Integer>>> executionMap = new HashMap<>();

        for (Process p : processesCopy) {
            remainingTime.add(p.getBrustTime());
        }

        int currentTime = 0;
        for (Process p : processesCopy) {
            int randomFunction = (int) (Math.random() * 20);
            int agFactor;
            if (randomFunction < 10) {
                agFactor = randomFunction + p.getArrivalTime() + p.getBrustTime();
            } else if (randomFunction > 10) {
                agFactor = 10 + p.getArrivalTime() + p.getBrustTime();
            } else {
                agFactor = p.getPriority() + p.getArrivalTime() + p.getBrustTime();
            }
            p.setAGFactor(agFactor);
        }

        for (int i=0; i<processes.size(); i++) {
            processesCopy.get(i).setQuantum(quantum);
            QuantumList.get(i).setQuantum(quantum);
        }
        System.out.println("Quantum History:");
        printQuantumList(QuantumList);

        currPro = processesCopy.get(0);
        currentTime = currPro.getArrivalTime();
        processesCopy.remove(currPro);
        boolean cont = false;
        while (!readyQueue.isEmpty() || !processesCopy.isEmpty() || cont) {


            // Add processes that have arrived at the current time to the ready queue
            isReady(processesCopy, currentTime, readyQueue);



            executionMap.putIfAbsent(currPro, new ArrayList<>());
            executionMap.get(currPro).add(new ArrayList<>());
            executionMap.get(currPro).get(executionMap.get(currPro).size() - 1).add(currentTime);
            exOrder.add(currPro.getName());
            int ceilQuantum = (int) Math.ceil(0.5 * currPro.getQuantum());
            if (currPro.getRemainingTime() - ceilQuantum > 0) {

                currPro.setRemainingTime(currPro.getRemainingTime() - ceilQuantum);
                currentTime += ceilQuantum;
            } else {
                currentTime += currPro.getRemainingTime();
                currPro.setRemainingTime(0);
            }
            executionMap.get(currPro).get(executionMap.get(currPro).size() - 1).add(currentTime);

            int ReaminingQuantum = 0;
            while (true) {
                isReady(processesCopy, currentTime, readyQueue);

                //scenario 3
                if(currPro.getRemainingTime() == 0)
                {
                    currPro.setQuantum(0);
                    QuantumList.get(QuantumList.indexOf(currPro)).setQuantum(0);
                    printQuantumList(QuantumList);
                    currPro.setTurnaroundTime(currentTime - currPro.getArrivalTime());
                    currPro.setWaitingTime(currPro.getTurnaroundTime() - currPro.getBrustTime());
                    results.add(currPro);
                    //exOrder.add(currPro.getName());
                    currPro = readyQueue.poll();
                    if (currPro != null) {
                        break;
                    }
                    else if (results.size() == processes.size()) {
                        break;
                    }
                    else{
                        currPro = processesCopy.get(0);
                        currentTime = currPro.getArrivalTime();
                        break;
                    }
                }
                //scenario 1
                if (ReaminingQuantum + ceilQuantum == currPro.getQuantum()) {
                    int mean = MeanQuantum(processesCopy, readyQueue, processes.size());
                    currPro.setQuantum(currPro.getQuantum() + mean);
                    QuantumList.get(QuantumList.indexOf(currPro)).setQuantum(currPro.getQuantum() + mean);
                    printQuantumList(QuantumList);
                    readyQueue.add(currPro);
                    currPro = readyQueue.poll();
                    //exOrder.add(currPro.getName());
                    break;
                }


                //scenario 2
                if (!readyQueue.isEmpty()) {
                    Process nextProcess = currPro;
                    for (Process p : readyQueue) {
                        if (p.getAGFactor() < nextProcess.getAGFactor()) {
                            nextProcess = p;
                        }

                    }
                    if (nextProcess != currPro) {
                        int unusedQuantum = currPro.getQuantum() - (ReaminingQuantum + ceilQuantum);
                        currPro.setQuantum(unusedQuantum + currPro.getQuantum());
                        QuantumList.get(QuantumList.indexOf(currPro)).setQuantum(unusedQuantum + currPro.getQuantum());
                        printQuantumList(QuantumList);
                        readyQueue.add(currPro);
                        readyQueue.remove(nextProcess);
                        currPro = nextProcess;
                        //exOrder.add(nextProcess.getName());
                        currentTime += csTime;
                        break;
                    }
                }
                currentTime ++;
                ReaminingQuantum++;
                currPro.setRemainingTime(currPro.getRemainingTime() - 1);
            }
            if (readyQueue.isEmpty() && processesCopy.isEmpty() && currPro != null) {
                if (currPro.getRemainingTime() > 0) {
                    cont = true;
                }
                else {
                    cont = false;
                    currPro.setTurnaroundTime(currentTime - currPro.getArrivalTime());
                    currPro.setWaitingTime(currPro.getTurnaroundTime() - currPro.getBrustTime());
                    results.add(currPro);
                    exOrder.add(currPro.getName());
                }
            } else if (currPro == null) {
                cont = false;
            }
        }
        // Display Execution Order
        System.out.println("Execution Order:");
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
        scheduleData.executionMap = executionMap;
        System.out.println("Average Waiting Time: " + scheduleData.avgWait);
        System.out.println("Average Turnaround Time: " + scheduleData.avgTurnaround);

        for (int i = 0; i < processes.size(); i++) {
            int agFactor = calculateAGFactor(processes.get(i));
            System.out.println("AG-Factor for " + processes.get(i).getName() + ": " + agFactor);
        }
        scheduleData.executionMap = executionMap;
        return scheduleData;
    }

    private void printQuantumList(List<Process> quantumList) {
        for (Process p: quantumList) {
            System.out.println(p.getName() + " " + p.getQuantum());
        }
        System.out.println("***************");
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

    private int MeanQuantum(List<Process> processesCopy ,Queue<Process> readyQueue,int number_of_processes)
    {
        int sum = 0;
        for (Process p : processesCopy) {
            sum += p.getQuantum();
        }
        for (Process p : readyQueue) {
            sum += p.getQuantum();
        }
        sum += currPro.getQuantum();
        return (int)Math.ceil((sum / number_of_processes) * 0.1) ;
    }
}