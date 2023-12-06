/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cpu_scheduling;

import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

/**
 *
 * @author Seif
 */
public class CPU_Scheduling {
    public static class Process {
        private String name;
        private int arrivalTime;
        private int brustTime;
        private int priority;
        private String color;   // Hanesta3meloh ba3den lamma ne3mel el grphical representation
        //______________________________________________________________________
        public String getName() {
            return name;
        }

        public int getArrivalTime() {
            return arrivalTime;
        }

        public int getBrustTime() {
            return brustTime;
        }
        
        public int getPriority() {
            return priority;
        }

        public String getColor() {
            return color;
        }

        //______________________________________________________________________
        public Process(String name, int arrivalTime, int brustTime, int priority) {
            this.name = name;
            this.arrivalTime = arrivalTime;
            this.brustTime = brustTime;
            this.priority = priority;
        }        
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public static void SJF(ArrayList<Process> processes,int csTime)
    {
         processes.sort(Comparator.comparingInt(p -> p.brustTime));     
         // NOT DONE YET
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner input = new Scanner(System.in);        
    //______________________________________________________________________
        // Testing variables
        int number_of_processes;
        int switching_time;
        int choice_of_algo;
    //______________________________________________________________________
        // Process variables
        String p_name;
        int p_arrivalTime;
        int p_brustTime;
        int p_priority;
    //______________________________________________________________________
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
            
            processes.add(new Process(p_name,p_arrivalTime,p_brustTime,p_priority));
            System.out.println("-------------------------------------------");
        }
        while(true)
        {
            System.out.println("Enter which alogorithm you want to apply:\n");
            System.out.println("1- SJF (Shortest-Job_First)\n");
            System.out.println("5- Exit\n");
            choice_of_algo = input.nextInt();
            switch(choice_of_algo)
            {
                case 1:
                    SJF(processes,switching_time);
                    break;
                    
                case 5:
                    exit(0);
                default:
                    System.out.println("Invalid choice try again!");
            }
        }
        
    //______________________________________________________________________
    }   
}
