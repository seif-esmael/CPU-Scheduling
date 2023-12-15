import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import static java.lang.System.exit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.stream.Collectors;


/* Authors: Seifeldeen Mohamed Ahmed Mohamed(20210168)
 *          Yousef Emadeldeen Ali(20210479)
 *          Yousef Karam(20210480)
 *          Noha Mohamed Abdelkader(20210452)
 *          Esraa Ahmed Saad Mubarak(20210062)
*/


public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        // ________________________
        // Testing variables
        int number_of_processes;
        int switching_time;
        int choice_of_algo;
        // ________________________
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

        ArrayList<Process> OriginalProcesses = new ArrayList<>();
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

            OriginalProcesses
                    .add(new Process(p_name, (float) i / number_of_processes, p_arrivalTime, p_brustTime, p_priority));
            System.out.println("-------------------------------------------");
        }
        while (true) {
            ArrayList<Process> processes = new ArrayList<>();
            for (Process p : OriginalProcesses) {
                processes.add(
                        new Process(p.getName(), p.getColor(), p.getArrivalTime(), p.getBrustTime(), p.getPriority()));
            }
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
                    data = scheduler.schedule(processes, switching_time);
                    break;

                case 2:
                    scheduler = new SRTF();
                    data = scheduler.schedule(processes, switching_time);
                    break;

                case 3:
                    scheduler = new PriorityScheduler();
                    data = scheduler.schedule(processes, switching_time);
                    break;

                case 4:
                    int q;
                    System.out.print("Enter Quantum Time: ");
                    q = input.nextInt();
                    scheduler = new RR(q);
                    data = scheduler.schedule(processes, switching_time);
                    break;

                case 5:
                    System.out.println("Good Bye :)");
                    exit(0);
                    break;
                default:
                    System.out.println("Invalid choice try again!");
            }
            //FileManager.write("GUI\\src\\DB\\schedule.json", data.parse());
        }
        // ________________________
    }
}

// ***********************
class Process {
    private String name;
    private float color;
    private int arrivalTime;
    private int brustTime;
    private int remainingTime;
    private int priority;
    private int quantum;
    private int waitingTime;
    private int turnaroundTime;
    private int completionTime;
    boolean completed = false;

    public int getAGFactor() {
        return AGFactor;
    }

    public void setAGFactor(int AGFactor) {
        this.AGFactor = AGFactor;
    }

    private int AGFactor = 0;
    // ________

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

    public void setPriority(int priority) {
        this.priority = (priority >= 0 ? priority : 0);
    }

    public float getColor() {
        return color;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public void setRemainingTime(int time) {
        this.remainingTime = time;
    }

    public int getRemainingTime() {
        return this.remainingTime;
    }

    public int getCompletionTime() {
        return this.completionTime;
    }

    public void setCompletionTime(int time) {
        this.completionTime = time;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean getCompleted() {
        return this.completed;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    public int getQuantum() {
        return this.quantum;
    }

    // ________
    public Process(String name, float color, int arrivalTime, int brustTime, int priority) {
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.brustTime = brustTime;
        this.remainingTime = brustTime;
        this.priority = priority;
    }
}

// ***********************
class ScheduleData {
    public int totalWait = 0;
    public int totalTurnaround = 0;
    public double avgWait = 0;
    public double avgTurnaround = 0;
    public Map<Process, List<List<Integer>>> executionMap;

    public String parse() {
        String result = "[";
        for (Map.Entry<Process, List<List<Integer>>> entry : executionMap.entrySet()) {
            result += "{";
            result += "\"processName\":" + '"' + entry.getKey().getName() + "\",";
            result += "\"priority\":" + '"' + entry.getKey().getPriority() + "\",";
            result += "\"color\":" + '"' + entry.getKey().getColor() + "\",";
            result += "\"executionMap\":";
            result += "[";
            for (List<Integer> exec : entry.getValue()) {
                result += "[" + exec.stream().map(Object::toString).collect(Collectors.joining(",")) + "],";
            }
            result = result.substring(0, result.length() - 1);
            result += "]";
            result += "},";
        }
        result = result.substring(0, result.length() - 1);
        result += "]";
        return result;
    }
}

// ***********************
interface Scheduler {
    ScheduleData schedule(List<Process> processes, int csTime);
}

// ***********************
class SJF implements Scheduler {
    @Override
    public ScheduleData schedule(List<Process> processes, int csTime) {

        List<Process> currprocesses = new ArrayList<>();
        List<Process> terminated = new ArrayList<>();
        int curr = 0, n = 0;
        Process currentProcess;
        ScheduleData scheduleData = new ScheduleData();
        Map<Process, List<List<Integer>>> executionMap = new HashMap<>();

        while (n < processes.size()) {
            for (Process p : processes) {
                if (!p.getCompleted()) {
                    if (!currprocesses.contains(p)) {
                        if (p.getArrivalTime() <= curr) {
                            currprocesses.add(p);
                        }
                    }
                }
            }
            // _________________
            if (!currprocesses.isEmpty()) {
                currprocesses.sort(Comparator.comparingLong(p -> p.getBrustTime()));
                currentProcess = currprocesses.get(0);
                currprocesses.remove(currentProcess);
                currentProcess.setCompleted(true);

                executionMap.put(currentProcess, new ArrayList<>());
                executionMap.get(currentProcess).add(new ArrayList<>());
                executionMap.get(currentProcess).get(executionMap.get(currentProcess).size() - 1).add(curr);
                executionMap.get(currentProcess).get(executionMap.get(currentProcess).size() - 1)
                        .add(curr + currentProcess.getBrustTime());

                curr += currentProcess.getBrustTime();
                currentProcess.setTurnaroundTime(curr - currentProcess.getArrivalTime());
                currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBrustTime());
                terminated.add(currentProcess);
                n++;
                curr += csTime;
            } else {
                curr++;
            }
        }
        // ------------------------------------------------------------------
        System.out.println("(1)Process Execution Order:\n");
        for (Process p : terminated) {
            System.out.print(p.getName() + " ");
        }
        // ------------------------------------------------------------------
        System.out.println("\n(2)Waiting Time for Each Process:");
        for (Process p : terminated) {
            System.out.println(p.getName() + ": " + p.getWaitingTime());
            scheduleData.totalWait += p.getWaitingTime();
        }
        // ------------------------------------------------------------------
        System.out.println("\n(3)Turnaround Time for Each Process:");
        for (Process p : terminated) {
            System.out.println(p.getName() + ": " + p.getTurnaroundTime());
            scheduleData.totalTurnaround += p.getTurnaroundTime();
        }
        // ------------------------------------------------------------------
        scheduleData.avgWait = (double) scheduleData.totalWait / processes.size();
        scheduleData.avgTurnaround = (double) scheduleData.totalTurnaround / processes.size();
        System.out.println("(4)Average Waiting Time: " + scheduleData.avgWait);
        System.out.println("(5)Average Turnaround Time: " + scheduleData.avgTurnaround);

        scheduleData.executionMap = executionMap;
        return scheduleData;
    }
}

// ***********************
class SRTF implements Scheduler {

    @Override
    public ScheduleData schedule(List<Process> processes, int csTime) {
        int currentTime = 0;
        int completedProcesses = 0;
        List<Process> readyQueue = new ArrayList<>();
        ScheduleData scheduleData = new ScheduleData();
        Map<String, Integer> waitingTimeMap = new HashMap<>();
        Map<Process, List<List<Integer>>> executionMap = new HashMap<>();
        while (completedProcesses < processes.size()) {
            System.out.println("Current Time: " + currentTime);
            System.out.println("Completed Processes: " + completedProcesses);

            for (Process p : processes) {
                if (p.getArrivalTime() <= currentTime && p.getRemainingTime() > 0) {
                    if (!readyQueue.contains(p)) {
                        readyQueue.add(p);
                        waitingTimeMap.put(p.getName(), 0);
                    }
                }
            }

            readyQueue.sort(
                    Comparator.comparingInt(p -> p.getRemainingTime() + waitingTimeMap.getOrDefault(p.getName(), 0)));

            if (!readyQueue.isEmpty()) {
                Process shortest = readyQueue.get(0);
                System.out.println("Executing process " + shortest.getName());

                if (shortest.getRemainingTime() > 0) {
                    shortest.setRemainingTime(shortest.getRemainingTime() - 1);
                    executionMap.putIfAbsent(shortest, new ArrayList<>());
                    executionMap.get(shortest).add(new ArrayList<>());
                    executionMap.get(shortest).get(executionMap.get(shortest).size() - 1).add(currentTime);
                    executionMap.get(shortest).get(executionMap.get(shortest).size() - 1).add(currentTime + 1);

                    currentTime++;
                    System.out.println("Updated remaining time of process " + shortest.getName() + " to "
                            + shortest.getRemainingTime());
                    // Increment waiting time for other processes in the queue
                    for (Process p : readyQueue) {
                        if (p != shortest) {
                            waitingTimeMap.put(p.getName(), waitingTimeMap.getOrDefault(p.getName(), 0) + 1);
                        }
                    }
                }

                if (shortest.getRemainingTime() == 0) {
                    shortest.setTurnaroundTime(currentTime - shortest.getArrivalTime());
                    shortest.setWaitingTime(shortest.getTurnaroundTime() - shortest.getBrustTime());
                    completedProcesses++;
                    readyQueue.remove(shortest);
                    waitingTimeMap.remove(shortest.getName()); // Remove waiting time for completed process
                    System.out.println("Process " + shortest.getName() + " completed execution");
                    System.out
                            .println("Turnaround Time for " + shortest.getName() + ": " + shortest.getTurnaroundTime());
                    System.out.println("Waiting Time for " + shortest.getName() + ": " + shortest.getWaitingTime());
                }
            } else {
                currentTime++;
            }
        }

        for (Process p : processes) {
            scheduleData.totalWait += p.getWaitingTime();
            scheduleData.totalTurnaround += p.getTurnaroundTime();
        }

        scheduleData.avgWait = (double) scheduleData.totalWait / processes.size();
        scheduleData.avgTurnaround = (double) scheduleData.totalTurnaround / processes.size();

        System.out.println("\nAverage Waiting Time: " + scheduleData.avgWait);
        System.out.println("Average Turnaround Time: " + scheduleData.avgTurnaround);

        scheduleData.executionMap = executionMap;

        return scheduleData;
    }
}

// ***********************
class RR implements Scheduler {
    private int quantum;
    private Process currPro;
    private ScheduleData scheduleData = new ScheduleData();

    public RR(int quantum) {
        this.quantum = quantum;
    }

    public int getQuantum() {
        return quantum;
    }

    private List<Process> makeCopy(List<Process> toBeCopied){
        List<Process> copy = new ArrayList<>();
        for (Process p:toBeCopied){
            Process temp = new Process(p.getName(), p.getColor(), p.getArrivalTime(), p.getBrustTime(), p.getPriority());
            copy.add(temp);
        }
        return copy;
    }

    private int search(Process p, List<Process> list){
        for (int i = 0; i < list.size(); i++) {
            if (p.getName().equals(list.get(i).getName())){
                return i;
            }
        }
        return -1;
    }

    @Override
    public ScheduleData schedule(List<Process> processes, int csTime) {
        List<Process> processesCopy = makeCopy(processes);
        List<Process> QuantumList = makeCopy(processesCopy);
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

        printQuantumList(QuantumList);

        currPro = processesCopy.get(0);
        currentTime = currPro.getArrivalTime();
        processesCopy.remove(currPro);
        boolean cont = false;
        System.out.println("Quantum History:");
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
                    int updateQuantum = 0;
                    int indexOfCurr = search(currPro, QuantumList);
                    currPro.setQuantum(updateQuantum);
                    QuantumList.get(indexOfCurr).setQuantum(updateQuantum);
                    printQuantumList(QuantumList);
                    currPro.setTurnaroundTime(currentTime - currPro.getArrivalTime());
                    currPro.setWaitingTime(currPro.getTurnaroundTime() - currPro.getBrustTime());
                    results.add(currPro);
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
                    int updateQuantum = mean + currPro.getQuantum();
                    int indexOfCurr = search(currPro, QuantumList);
                    currPro.setQuantum(updateQuantum);
                    QuantumList.get(indexOfCurr).setQuantum(updateQuantum);
                    printQuantumList(QuantumList);
                    readyQueue.add(currPro);
                    currPro = readyQueue.poll();
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
                        int updateQuantum = unusedQuantum + currPro.getQuantum();
                        int indexOfCurr = search(currPro, QuantumList);
                        currPro.setQuantum(updateQuantum);
                        QuantumList.get(indexOfCurr).setQuantum(updateQuantum);
                        printQuantumList(QuantumList);
                        readyQueue.add(currPro);
                        readyQueue.remove(nextProcess);
                        currPro = nextProcess;
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
    
        System.out.println("Execution Order:");
        for (String string : exOrder) {
            System.out.print(string + " ");
        }

        System.out.println("\nWaiting Times for Each Process:");
        for (Process p : results) {
            System.out.println(p.getName() + ": " + p.getWaitingTime());
            scheduleData.totalWait += p.getWaitingTime();
        }


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

        for (int i = 0; i < results.size(); i++) {
            int agFactor = results.get(i).getAGFactor();
            System.out.println("AG-Factor for " + results.get(i).getName() + ": " + agFactor);
        }
        return scheduleData;
    }

    private void printQuantumList(List<Process> quantumList) {
        for (Process p: quantumList) {
            System.out.println(p.getName() + " " + p.getQuantum());
        }
        System.out.println("*****");
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

// ***********************
class PriorityScheduler implements Scheduler {
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
        List<Integer> processesPriority = new ArrayList<>();
        for (Process process : processes)
            processesPriority.add(process.getPriority());

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
                executionMap.get(currentProcess).get(executionMap.get(currentProcess).size() - 1).add(timer);
                executionMap.get(currentProcess).get(executionMap.get(currentProcess).size() - 1)
                        .add(timer + currentProcess.getBrustTime());
                timer += currentProcess.getBrustTime();
                currentProcess.setTurnaroundTime(timer - currentProcess.getArrivalTime());
                currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBrustTime());
                pq = reducePriority(pq);

                scheduleData.totalWait += currentProcess.getWaitingTime();
                scheduleData.totalTurnaround += currentProcess.getTurnaroundTime();
            } else
                timer++;

        }
        for (int i = 0; i < processes.size(); i++)
            processes.get(i).setPriority(processesPriority.get(i));

        System.out.println("Average Waiting time for:");
        for (Process p : processes) {
            System.out.println(p.getName() + ": " + p.getWaitingTime());
        }

        System.out.println("Average Turnaround time for:");
        for (Process p : processes) {
            System.out.println(p.getName() + ": " + p.getTurnaroundTime());
        }

        scheduleData.avgWait = (double) scheduleData.totalWait / processes.size();
        scheduleData.avgTurnaround = (double) scheduleData.totalTurnaround / processes.size();
        System.out.println("Average Waiting Time: " + scheduleData.avgWait);
        System.out.println("Average Turnaround Time: " + scheduleData.avgTurnaround);

        scheduleData.executionMap = executionMap;
        return scheduleData;
    }
}

// ***********************
class FileManager {
    public static String read(String fileName) {
        String data = "";
        try {
            File file = new File(fileName);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                data += line + (reader.hasNextLine() ? "\n" : "");
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return data;
    }

    private static boolean createFile(String fileName) {
        try {
            File file = new File(fileName);
            return file.createNewFile();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return false;
    }

    public static void write(String fileName, String data) {
        createFile(fileName);
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}