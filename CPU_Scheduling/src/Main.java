import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Process.Process;
import static java.lang.System.exit;
import java.util.Scanner;
import java.util.Map.Entry;

import FileManager.FileManager;
import Scheduler.*;

public class Main {
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

    // processes.add(new Process(p_name, 1, p_arrivalTime, p_brustTime,
    // p_priority));
    // System.out.println("-------------------------------------------");
    // }
    // while (true) {
    // for (Process p : processes) {
    // System.out.println("here: " + p.getName());
    // }
    // System.out.println("Enter which alogorithm you want to apply:\n");
    // System.out.println("1- SJF (Shortest-Job_First)\n");
    // System.out.println("3- Priority Scheduling\n");
    // System.out.println("5- Exit\n");
    // choice_of_algo = input.nextInt();

    // Scheduler scheduler;
    // ScheduleData data;
    // switch (choice_of_algo) {
    // case 1:
    // scheduler = new SJF();
    // data = scheduler.schedule(processes, switching_time);
    // FileManager.write("CPU_Scheduling\\GUI\\src\\DB\\schedule.json",
    // data.parse());
    // break;

    // case 3:
    // scheduler = new PriorityScheduler();
    // data = scheduler.schedule(processes, switching_time);
    // FileManager.write("CPU_Scheduling\\GUI\\src\\DB\\schedule.json",
    // data.parse());
    // break;

    // case 5:
    // exit(0);
    // break;
    // default:
    // System.out.println("Invalid choice try again!");
    // }
    // }
    // // ______________________________________________________________________
    // }
    // }

    public static void main(String[] args) {
        // TODO code application logic here
        Scanner input = new Scanner(System.in);
        // ______________________________________________________________________
        // Testing variables
        int number_of_processes;
        int switching_time;
        int choice_of_algo;
        // ______________________________________________________________________
        // Process variables
        String p_name;
        int p_arrivalTime;
        int p_brustTime;
        int p_priority;
        // ______________________________________________________________________
        System.out.println("What is the number of processes?");
        number_of_processes = input.nextInt();

        System.out.println("What is the context switching time?");
        switching_time = input.nextInt();

        ArrayList<Process> processes = new ArrayList<>();

        for (int i = 0; i < number_of_processes; i++) {
            System.out.println("\nFor process number " + (i + 1) + ", enter the following data:");

            System.out.print("process name: ");
            p_name = input.next();

            System.out.print("arrival time: ");
            p_arrivalTime = input.nextInt();

            System.out.print("burst time: ");
            p_brustTime = input.nextInt();

            System.out.print("priority: ");
            p_priority = input.nextInt();

            processes.add(new Process(p_name, 1, p_arrivalTime, p_brustTime,
                    p_priority));
            System.out.println("-------------------------------------------");
        }
        while (true) {
            for (Process p : processes) {
                System.out.println("here: " + p.getName());
            }
            System.out.println("Enter which alogorithm you want to apply:\n");
            System.out.println("1- SJF (Shortest-Job_First)\n");
            System.out.println("3- Priority Scheduling\n");
            System.out.println("5- Exit\n");
            choice_of_algo = input.nextInt();

            Scheduler scheduler;
            ScheduleData data;
            switch (choice_of_algo) {
                case 1:
                    scheduler = new SJF();
                    data = scheduler.schedule(processes, switching_time);
                    FileManager.write("CPU_Scheduling\\GUI\\src\\DB\\schedule.json",
                            data.parse());
                    break;

                case 3:
                    scheduler = new PriorityScheduler();
                    data = scheduler.schedule(processes, switching_time);
                    FileManager.write("CPU_Scheduling\\GUI\\src\\DB\\schedule.json",
                            data.parse());
                    break;

                case 4:
                    int q;
                    System.out.print("Enter Quantum Time: ");
                    q = input.nextInt();
                    scheduler = new RR();
                    ((RR) scheduler).setQuantum(q);
                    scheduler.schedule(processes, switching_time);
                    break;

                case 5:
                    exit(0);
                    break;
                default:
                    System.out.println("Invalid choice try again!");
            }
        }
        // ______________________________________________________________________
    }
}

// public static void main(String[] args) {
// Scheduler scheduler = new PriorityScheduler();
// ScheduleData data = scheduler.schedule(getTestProcesses(), 1);
//
// }
//
// private static int processComparatorByArrival(Process first, Process second)
// {
// return first.getArrivalTime() - second.getArrivalTime();
// }
//
// private static List<Process> getTestProcesses() {
// List<Process> processes = new ArrayList<>();
//
// int amount = 20;
// for (int i = 0; i < amount; i++) {
// processes.add(
// new Process("Process " + i, (float) i / amount, 5 * (i), 10,
// amount - i));
// }
// processes.sort(Main::processComparatorByArrival);
// return processes;
// }
