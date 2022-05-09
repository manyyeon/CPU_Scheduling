package com.company;

import java.util.*;

public class Main {
    int processNum; // process 개수
    Algorithm algorithm; // 알고리즘

    // 초기 프로세스들 출력
    public void printInitialAllProcess() {
        Iterator<Process> it = algorithm.processList.iterator();
        System.out.println("-----------------------------------------");
        System.out.println("time quantum : " + algorithm.timeQuantum);
        System.out.println("\tarrival time\tburst time\tpriority");
        while(it.hasNext()){
            Process tmpProcess = it.next();
            System.out.println("P" + tmpProcess.id + "\t\t\t" + tmpProcess.arrivalTime + "\t\t\t" + tmpProcess.burstTime + "\t\t\t" + tmpProcess.priority);
        }
        System.out.println("-----------------------------------------");
    }

    // 입력
    public void input(){
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.print("process 개수 입력 >> ");
            processNum = sc.nextInt();
            if(processNum >= 4) {
                break;
            } else {
                System.out.println("프로세스 개수는 최소 4개 이상입니다. 다시 입력하세요.");
            }
        }
        algorithm = new Algorithm(processNum);

        algorithm.processList = new ArrayList<Process>(); // process들을 담는 리스트

        for(int i=0; i<processNum; i++){
            Process newProcess = new Process();
            newProcess.id = i+1;
            System.out.println("프로세스 P" + newProcess.id + " arrival time, burst time, priority 입력");
            newProcess.arrivalTime = sc.nextInt();
            newProcess.burstTime = sc.nextInt();
            newProcess.priority = sc.nextInt();

            algorithm.processList.add(newProcess);

            // 전체 실행시간
            algorithm.totalProcessingTime += newProcess.burstTime;
        }

        System.out.print("time quantum 입력 >> ");
        algorithm.timeQuantum = sc.nextInt();
    }
    public static void main(String[] args) {
	    Main mainClass = new Main();

        mainClass.input();
        mainClass.printInitialAllProcess();

        mainClass.algorithm.FCFS();
        mainClass.algorithm.SJF();
        mainClass.algorithm.Priority();
    }
}
