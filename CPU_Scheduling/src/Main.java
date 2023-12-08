import java.util.ArrayList;
import java.util.List;

import Process.Process;
import Scheduler.PriorityScheduler;
import Scheduler.SJF;
import Scheduler.ScheduleData;
import Scheduler.Scheduler;

public class Main {

    public static void main(String[] args) {
        Scheduler scheduler = new PriorityScheduler();
        ScheduleData data = scheduler.schedule(getTestProcesses(), 1);

        System.out.println("\n(2)Waiting Time for Each Process:");
        for (Process p : data.processes) {
            System.out.println(p.getName() + ": " + p.getWaitingTime());
        }
        System.out.println("\n(3)Turnaround Time for Each Process:");
        for (Process p : data.processes) {
            System.out.println(p.getName() + ": " + p.getTurnaroundTime());
        }

        System.out.println("Average Turnaround: " + data.avgTurnaround);
        System.out.println("Average Waiting Time: " + data.avgWait);

    }

    private static int processComparatorByArrival(Process first, Process second) {
        return first.getArrivalTime() - second.getArrivalTime();
    }

    private static List<Process> getTestProcesses() {
        List<Process> processes = new ArrayList<>();

        int amount = 20;
        for (int i = 0; i < amount; i++) {
            processes.add(
                    new Process("Process " + i, (float) i / amount, 5 * (i), 10,
                            amount - i));
        }
        processes.sort(Main::processComparatorByArrival);
        return processes;
    }
}

// public static void main(String[] args) {
// // TODO code application logic here
// Scanner input = new Scanner(System.in);
// // ______________________________________________________________________
// // Testing variables
// int number_of_processes;
// int switching_time;
// int choice_of_algo;
// // ______________________________________________________________________
// // Process variables
// String p_name;
// int p_arrivalTime;
// int p_brustTime;
// int p_priority;
// // ______________________________________________________________________
// System.out.println("What is the number of processes?");
// number_of_processes = input.nextInt();

// System.out.println("What is the context switching time?");
// switching_time = input.nextInt();

// ArrayList<Process> processes = new ArrayList<>();

// for (int i = 0; i < number_of_processes; i++) {
// System.out.println("\nFor process number " + (i + 1) + ", enter the following
// data:");

// System.out.print("process name: ");
// p_name = input.next();

// System.out.print("arrival time: ");
// p_arrivalTime = input.nextInt();

// System.out.print("burst time: ");
// p_brustTime = input.nextInt();

// System.out.print("priority: ");
// p_priority = input.nextInt();

// processes.add(new Process(p_name, p_arrivalTime, p_brustTime, p_priority));
// System.out.println("-------------------------------------------");
// }
// while (true) {
// System.out.println("Enter which alogorithm you want to apply:\n");
// System.out.println("1- SJF (Shortest-Job_First)\n");
// System.out.println("5- Exit\n");
// choice_of_algo = input.nextInt();
// switch (choice_of_algo) {
// case 1:
// SJF(processes, switching_time);
// break;

// case 5:
// exit(0);
// default:
// System.out.println("Invalid choice try again!");
// }
// }

// // ______________________________________________________________________
// }
