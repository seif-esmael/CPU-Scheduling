package Scheduler;

import Scheduler.Scheduler;
import Process.Process;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SRTF implements Scheduler {

    @Override
    public ScheduleData schedule(List<Process> processes, int csTime) {
        int currentTime = 0;
        int completedProcesses = 0;
        List<Process> readyQueue = new ArrayList<>();
        ScheduleData scheduleData = new ScheduleData();

        while (completedProcesses != processes.size())
        {
            System.out.println("Completed Processes: " + completedProcesses + "Process size: " + processes.size());

//            System.out.println("While loop and process number: " + completedProcesses);
//            System.out.println("Flag 1");

            for (Process p : processes)
            {
                if (p.getArrivalTime() <= currentTime && p.getRemainingTime() > 0)
                {
                    readyQueue.add(p);
                    System.out.println("Flag 2");

                }
            }

            readyQueue.sort(Comparator.comparingInt(p -> p.getRemainingTime()));

//            System.out.println("Flag 3");


            if (!readyQueue.isEmpty())
            {
                Process shortest = readyQueue.get(0);
                System.out.println("Executing" + shortest.getName());
                shortest.setRemainingTime(shortest.getRemainingTime() - 1);
                currentTime++;
                System.out.println("Flag 4");
                if (shortest.getRemainingTime() == 0)
                {
                    shortest.setTurnaroundTime(currentTime - shortest.getArrivalTime());
                    shortest.setWaitingTime(shortest.getTurnaroundTime() - shortest.getBrustTime());
                    completedProcesses++;
                    readyQueue.remove(shortest);

                }
            }
//            System.out.println("Flag 5");

            currentTime++;
//            System.out.println(currentTime);
        }
        for (Process p : processes) {
            scheduleData.totalWait += p.getWaitingTime();
            scheduleData.totalTurnaround += p.getTurnaroundTime();
            System.out.println("Flag 6");

        }
        scheduleData.avgWait = (double) scheduleData.totalWait / processes.size();
        scheduleData.avgTurnaround = (double) scheduleData.totalTurnaround / processes.size();

        System.out.println("\nAverage Waiting Time: " + scheduleData.avgWait);
        System.out.println("Average Turnaround Time: " + scheduleData.totalTurnaround);
        System.out.println("Flag 7");
        return scheduleData;
    }
}