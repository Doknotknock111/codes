import java.util.*;

class Process {
    int pid;
    int arrivalTime;
    int burstTime;
    int remainingTime;
    int completionTime;
    int turnaroundTime;
    int waitingTime;

    Process(int pid, int arrivalTime, int burstTime) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }
}

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Number of Processes: ");
        int n = sc.nextInt();

        Process[] processes = new Process[n];

        for (int i = 0; i < n; i++) {

            System.out.println("\nProcess P" + (i + 1));

            System.out.print("Arrival Time: ");
            int at = sc.nextInt();

            System.out.print("Burst Time: ");
            int bt = sc.nextInt();

            processes[i] = new Process(i + 1, at, bt);
        }

        System.out.print("\nEnter Time Quantum: ");
        int quantum = sc.nextInt();

        Arrays.sort(processes, Comparator.comparingInt(p -> p.arrivalTime));

        Queue<Process> readyQueue = new LinkedList<>();
        List<String> ganttChart = new ArrayList<>();

        int currentTime = 0;
        int completed = 0;
        int index = 0;

        while (completed < n) {

            while (index < n &&
                    processes[index].arrivalTime <= currentTime) {
                readyQueue.add(processes[index]);
                index++;
            }

            if (readyQueue.isEmpty()) {
                currentTime++;

                while (index < n &&
                        processes[index].arrivalTime <= currentTime) {
                    readyQueue.add(processes[index]);
                    index++;
                }

                continue;
            }

            Process current = readyQueue.poll();

            int executionTime =
                    Math.min(quantum, current.remainingTime);

            ganttChart.add(
                    "P" + current.pid +
                    "(" + currentTime +
                    "-" + (currentTime + executionTime) + ")"
            );

            currentTime += executionTime;
            current.remainingTime -= executionTime;

            while (index < n &&
                    processes[index].arrivalTime <= currentTime) {
                readyQueue.add(processes[index]);
                index++;
            }

            if (current.remainingTime > 0) {
                readyQueue.add(current);
            } else {
                completed++;

                current.completionTime = currentTime;

                current.turnaroundTime =
                        current.completionTime -
                        current.arrivalTime;

                current.waitingTime =
                        current.turnaroundTime -
                        current.burstTime;
            }
        }

        System.out.println("\n========== GANTT CHART ==========");

        for (String s : ganttChart) {
            System.out.print("| " + s + " ");
        }
        System.out.println("|");

        System.out.println("\n================ PROCESS TABLE ================");

        System.out.printf("%-8s%-8s%-8s%-8s%-8s%-8s%n",
                "PID", "AT", "BT", "CT", "TAT", "WT");

        double totalWT = 0;
        double totalTAT = 0;

        Arrays.sort(processes, Comparator.comparingInt(p -> p.pid));

        for (Process p : processes) {

            System.out.printf("%-8d%-8d%-8d%-8d%-8d%-8d%n",
                    p.pid,
                    p.arrivalTime,
                    p.burstTime,
                    p.completionTime,
                    p.turnaroundTime,
                    p.waitingTime);

            totalWT += p.waitingTime;
            totalTAT += p.turnaroundTime;
        }

        System.out.println("\n==============================================");

        System.out.printf("Average Waiting Time = %.2f%n",
                totalWT / n);

        System.out.printf("Average Turnaround Time = %.2f%n",
                totalTAT / n);

        sc.close();
    }
}
