import java.util.*;

public class Main {
    
    // Define the Process class
    static class Process {
        int processID;
        int arrivalTime;
        int burstTime;
        int waitingTime;
        int turnaroundTime;
        int remainingTime;
        
        Process(int id, int arrival, int burst) {
            processID = id;
            arrivalTime = arrival;
            burstTime = burst;
            waitingTime = 0;
            turnaroundTime = 0;
            remainingTime = burst;
        }
    }
    
    static class Scheduler {
        int timeLimit;
        int quantum;
        Queue<Process> queue;
        
        Scheduler(int q, int t) {
            quantum = q;
            timeLimit = t;
            queue = new LinkedList<>();
        }
        
        void addProcess(Process p) {
            queue.add(p);
        }
        
        Process removeProcess() {
            return queue.remove();
        }
        
        boolean isEmpty() {
            return queue.isEmpty();
        }
        
        void roundRobin() {
            int currentTime = 0;
            while (!isEmpty() && currentTime < timeLimit) {
                Process p = removeProcess();
                int timeSlice = Math.min(quantum, p.remainingTime);
                p.remainingTime -= timeSlice;
                currentTime += timeSlice;
                if (p.remainingTime == 0) {
                    p.turnaroundTime = currentTime - p.arrivalTime;
                    p.waitingTime = p.turnaroundTime - p.burstTime;
                } else {
                    addProcess(p);
                }
            }
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
       
        // read input from user
        System.out.print("Enter number of processes: ");
        int numProcesses = scanner.nextInt();
        System.out.print("Enter time quantum: ");
        int quantum = scanner.nextInt();
        System.out.print("Enter the time limit: ");
        int timeLimit = scanner.nextInt();
        
        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < numProcesses; i++) {
            int arrivalTime = random.nextInt(100);
            int burstTime = random.nextInt(numProcesses) + 1;
            processes.add(new Process(i, arrivalTime, burstTime));
        }
        
        Scheduler scheduler = new Scheduler(quantum, timeLimit);
        for (Process p : processes) {
            scheduler.addProcess(p);
        }
        scheduler.roundRobin();
        
        // Print results
        double totalWaitingTime = 0.0;
        double totalTurnaroundTime = 0.0;
        for (Process p : processes) {
            System.out.printf("Process %d: arrivalTime=%d, burstTime=%d, waitingTime=%d, turnaroundTime=%d\n",
                    p.processID, p.arrivalTime, p.burstTime, p.waitingTime, p.turnaroundTime);
            totalWaitingTime += p.waitingTime;
            totalTurnaroundTime += p.turnaroundTime;
        }
        double avgWaitingTime = totalWaitingTime / numProcesses;
        double avgTurnaroundTime = totalTurnaroundTime / numProcesses;
        System.out.printf("Average waiting time: %.2f\n", avgWaitingTime);
        System.out.printf("Average turnaround time: %.2f\n", avgTurnaroundTime);
    }
}
