import java.util.ArrayList;
import Process.Process;
import static java.lang.System.exit;
import java.util.Scanner;

import FileManager.FileManager;
import Scheduler.*;

public class Main {
    public static void main(String[] args) {
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
        // // ______________________________________________________________________
        // System.out.println("What is the number of processes?");
        // number_of_processes = input.nextInt();

        // System.out.println("What is the context switching time?");
        // switching_time = input.nextInt();

        ArrayList<Process> processes = new ArrayList<>();

        // for (int i = 0; i < number_of_processes; i++) {
        //     System.out.println("\nFor process number " + (i + 1) + ", enter the following data:");

        //     System.out.print("process name: ");
        //     p_name = input.next();

        //     System.out.print("arrival time: ");
        //     p_arrivalTime = input.nextInt();

        //     System.out.print("burst time: ");
        //     p_brustTime = input.nextInt();

        //     System.out.print("priority: ");
        //     p_priority = input.nextInt();

        //     processes.add(new Process(p_name, (float) i / number_of_processes, p_arrivalTime, p_brustTime, p_priority));
        //     System.out.println("-------------------------------------------");
        // }
        processes.add(new Process("p0", 1, 3, 7, 1));
        processes.add(new Process("p1", 1, 5, 10, 1));
        processes.add(new Process("p2", 1, 10, 2, 1));
        processes.add(new Process("p3", 1, 6, 9, 1));
        processes.add(new Process("p4", 1, 1, 5, 1));
        processes.add(new Process("p5", 1, 4, 6, 1));
        while (true) {
            System.out.println("Enter which alogorithm you want to apply:\n");
            System.out.println("1- SJF (Shortest-Job_First)\n");
            System.out.println("2- SRTF\n");
            System.out.println("3- Priority Scheduling\n");
            System.out.println("4- RR (Round Robin)\n");
            System.out.println("5- Exit\n");
            choice_of_algo = input.nextInt();
            Scheduler scheduler = null;
            ScheduleData data = null;
            switch (choice_of_algo) {
                case 1:
                    scheduler = new SJF();
                    data = scheduler.schedule(processes, 0);
                    break;

                case 2:
                    scheduler = new SRTF();
                    data = scheduler.schedule(processes, 0);
                    break;

                case 3:
                    scheduler = new PriorityScheduler();
                    data = scheduler.schedule(processes, 0);
                    break;

                case 4:
                    int q;
                    System.out.print("Enter Quantum Time: ");
                    q = input.nextInt();
                    scheduler = new RR(q);
                    data = scheduler.schedule(processes, 0);
                    break;

                case 5:
                    exit(0);
                    break;
                default:
                    System.out.println("Invalid choice try again!");
            }
            FileManager.write("CPU_Scheduling\\GUI\\src\\DB\\schedule.json", data.parse());
        }
        // ______________________________________________________________________
    }
}