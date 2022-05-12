package com.company;

import java.util.*;

// 초기 정보를 입력 받고 알고리즘을 실행시키는 Main 클래스
public class Main {
    int processNum; // process 개수
    Algorithm algorithm; // 알고리즘

    // process 정보 입력받기
    public void input(){
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.print("process 개수 입력 >> ");
            processNum = sc.nextInt(); // 프로세스 개수
            if(processNum >= 4) {
                break;
            } else {
                System.out.println("프로세스 개수는 최소 4개 이상입니다. 다시 입력하세요.");
            }
        }
        // 알고리즘 클래스의 객체 생성(프로세스 개수 전달)
        algorithm = new Algorithm(processNum);

        algorithm.processList = new ArrayList<Process>(); // process들을 담는 리스트

        for(int i=0; i<processNum; i++){
            Process newProcess = new Process();
            newProcess.id = i+1; // 프로세스 id
            System.out.println("프로세스 P" + newProcess.id + " arrival time, burst time, priority 입력");
            newProcess.arrivalTime = sc.nextInt(); // 도착시간
            newProcess.burstTime = sc.nextInt(); // burst time
            newProcess.priority = sc.nextInt(); // 우선순위

            algorithm.processList.add(newProcess); // process 리스트에 새 프로세스를 넣어줌

            // 전체(모든 프로세스) 실행시간
            algorithm.totalProcessingTime += newProcess.burstTime;
        }

        System.out.print("time quantum 입력 >> ");
        algorithm.timeQuantum = sc.nextInt(); // time quantum
    }

    // 입력받은 초기 프로세스 정보 출력
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

    public static void main(String[] args) {
	    Main mainObject = new Main();

        mainObject.input(); // 프로세스들 정보 입력 받기
        mainObject.printInitialAllProcess(); // 입력받은 정보 출력해주기

        mainObject.algorithm.FCFS(); // FCFS 알고리즘 실행
        mainObject.algorithm.SJF(); // SJF 알고리즘 실행
        mainObject.algorithm.Priority(); // Priority 알고리즘 실행
        mainObject.algorithm.RR(); // RR 알고리즘 실행
    }
}
