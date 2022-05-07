package com.company;

import java.util.*;

// 알고리즘 클래스
class Algorithm {
    int processNum; // process 개수
    ArrayList<Process> processList; // process 리스트
    // algorithmNum : FCFS = 0, SJF = 1, Priority = 2, RR = 3
    ResultTime [] waitingTime = new ResultTime[4]; // 알고리즘 별 대기시간
    ResultTime [] turnaroundTime = new ResultTime[4]; // 알고리즘 별 총처리시간
    GanttChartInformation [] ganttChartInformation = new GanttChartInformation[4]; // 알고리즘 별 간트차트 정보
    int timeQuantum; // time quantum

    // 생성자
    public Algorithm(int processNum){
        this.processNum = processNum; // 프로세스 개수 설정
        for(int i=0; i<4; i++) {
            // 알고리즘 별 대기시간(total, avg)
            waitingTime[i] = new ResultTime();
            // 알고리즘 별 총처리시간(total, avg)
            turnaroundTime[i] = new ResultTime();
            // 알고리즘 별 간트 차트 정보
            ganttChartInformation[i] = new GanttChartInformation(processNum);
        }
    }

    public void FCFS() {
        // arrival time이 기준인 우선순위 큐 생성
        // arrival time이 빠를 수록 우선순위가 높음
        // arrival time이 같으면 id 값이 작을 수록 우선순위가 높음
        PriorityQueue<Process> priorityQueue = new PriorityQueue<>(new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2){
                if(p1.arrivalTime > p2.arrivalTime){
                    return 1;
                } else if(p1.arrivalTime == p2.arrivalTime){
                    if(p1.id > p2.id){
                        return 1;
                    } else{
                        return -1;
                    }
                } else {
                    return -1;
                }
            }
        });

        // 우선순위 큐에 process들 넣기
        for(int i=0; i< processList.size(); i++){
            priorityQueue.add(processList.get(i));
        }

        int tmpComplete = 0;
        int tmpStart = 0;
        int tmpEnd = 0;
        ProcessingTime tmpProcessingTime;
        for(int i=0; i< processNum; i++){
            // ganttChartInformation[0]이 FCFS
            // 현재 실행 중인 프로세스
            Process presentProcess = priorityQueue.poll();
            // 이 프로세스의 complete time
            tmpComplete += presentProcess.burstTime;
            ganttChartInformation[0].completeTime[presentProcess.id-1] = tmpComplete;

            // 이 프로세스의 start, end
            tmpProcessingTime = new ProcessingTime();
            tmpEnd += presentProcess.burstTime;
            tmpProcessingTime.start = tmpStart;
            tmpProcessingTime.end = tmpEnd;
            tmpProcessingTime.burst = tmpProcessingTime.end - tmpProcessingTime.start;
            ganttChartInformation[0].processingTime[presentProcess.id-1].add(tmpProcessingTime);

            // 이 프로세스의 turnaround time
            // complete time - arrival time
            ganttChartInformation[0].turnaroundTime[presentProcess.id-1] = tmpComplete - presentProcess.arrivalTime;

            // 전체 turnaround time
            // turnaroundTime[0]이 FCFS
            // 현재 프로세스의 turnaround time을 전체에 더해주기
            turnaroundTime[0].total += ganttChartInformation[0].turnaroundTime[presentProcess.id-1];

            // 이 프로세스의 wait time
            // 프로세스 start time - arrival time
            ganttChartInformation[0].waitTime[presentProcess.id-1] = tmpProcessingTime.start - presentProcess.arrivalTime;

            // 전체 wait time
            // waitingTime[0]이 FCFS
            // 현재 프로세스의 wait time을 전체에 더해주기
            waitingTime[0].total += ganttChartInformation[0].waitTime[presentProcess.id-1];

            // 다음 프로세스의 시작 시간 = 현재 프로세스 완료시간
            tmpStart = tmpComplete;
        }

        turnaroundTime[0].avg = turnaroundTime[0].total / processNum;
        waitingTime[0].avg = waitingTime[0].total / processNum;

        // 출력
        // FCFS의 algorithmNum = 0
        printAlgorithmResult(0);
    }
    public void SJF() {

    }
    public void Priority() {

    }
    public void RR() {

    }

    public void printAlgorithmResult(int algorithmNum) {
        String name = "";
        switch(algorithmNum) {
            case 0:
                name = "FCFS"; break;
            case 1:
                name = "SJF"; break;
            case 2:
                name = "Priority"; break;
            case 3:
                name = "RR"; break;
        }

        System.out.println("**************" + name + "**************");
        int id; // 프로세스 id
        // 프로세스 별 processing time 출력
        System.out.println("[프로세스 별 processing time]");
        for(int i=0; i<processNum; i++){
            id = i+1;
            System.out.print("P" + id + " : ");
            Iterator<ProcessingTime> it = ganttChartInformation[algorithmNum].processingTime[i].iterator();
            while(it.hasNext()) {
                ProcessingTime eachProcessingTime = it.next();
                // 출력형식 : [start-end](burst)
                System.out.print("[" + eachProcessingTime.start + "-" + eachProcessingTime.end + "]" + "(" + eachProcessingTime.burst + ")\t");
            }
            System.out.println();
        }
        // 프로세스 별 wait time 출력
        System.out.println("[프로세스 별 wait time]");
        for(int i=0; i<processNum; i++){
            id = i+1;
            System.out.print("P" + id + " : ");
            System.out.print(ganttChartInformation[algorithmNum].waitTime[i] + "\t");
        }
        System.out.println();
        // 프로세스 별 complete time 출력
        System.out.println("[프로세스 별 complete time]");
        for(int i=0; i<processNum; i++){
            id = i+1;
            System.out.print("P" + id + " : ");
            System.out.print(ganttChartInformation[algorithmNum].completeTime[i] + "\t");
        }
        System.out.println();

        // average waiting time 출력
        System.out.println("[average waiting time] = " + waitingTime[algorithmNum].avg);
        // average turnaround time 출력
        System.out.println("[average turnaround time] = " + turnaroundTime[algorithmNum].avg);
    }
}
