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
        int startTime;

        Process(int id, int arrival, int burst) {
            processID = id;
            arrivalTime = arrival;
            burstTime = burst;
            waitingTime = 0;
            turnaroundTime = 0;
            remainingTime = burst;
            startTime = -1;
        }

        void start(int time) {
            startTime = time;
        }

        void complete(int time) {
            int tempWaitingTime = turnaroundTime - burstTime;
            turnaroundTime = waitingTime + burstTime;
            waitingTime = startTime - arrivalTime;
            waitingTime = tempWaitingTime >= 0 ? tempWaitingTime : 0;
        }
    }

    static class Scheduler {
        int timelimit;
        int quantum;
        Queue<Process> queue;

        Scheduler(int q, int t) {
            quantum = q;
            timelimit = t;
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
            List<Process> unfinishedProcesses = new ArrayList<>();
            while (!isEmpty() && currentTime < timelimit) {
                Process p = removeProcess();
                int timeSlice = Math.min(quantum, p.remainingTime);
                p.remainingTime -= timeSlice;
                currentTime += timeSlice;
                if (p.startTime == -1) {
                    p.start(currentTime - timeSlice);
                }
                if (p.remainingTime == 0) {
                    p.complete(currentTime);
                } else {
                    unfinishedProcesses.add(p);
                }
                if (!isEmpty() && queue.peek().arrivalTime <= currentTime) {
                    queue.add(p);
                }
            }
            // Print any unfinished processes with their remaining time
            for (Process p : unfinishedProcesses) {
                System.out.printf("Process %d was not completed. Remaining time: %d\n", p.processID, p.remainingTime);
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
        int timelimit = scanner.nextInt();

        // Generate processes with random arrival times and CPU burst times
        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < numProcesses; i++) {
            int arrivalTime = random.nextInt(10);
            int burstTime = random.nextInt(10);
            processes.add(new Process(i, arrivalTime, burstTime));
        }

        Scheduler scheduler = new Scheduler(quantum, timelimit);
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
