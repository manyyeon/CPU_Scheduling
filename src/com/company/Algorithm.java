package com.company;

import java.util.*;

// 알고리즘 클래스
class Algorithm {
    ArrayList<Process> processList; // process 리스트
    int processNum = 0; // process 개수
    int totalProcessingTime; // 모든 프로세스 실행이 끝나는 시간
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

    /////////////////////////////////////////////////////////////////////////
    // FCFS
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

        Present present = new Present(); // 현재 값들
        for(int i=0; i< processNum; i++){
            // ganttChartInformation[0]이 FCFS
            // 현재 실행 중인 프로세스
            present.process = priorityQueue.poll();
            // 이 프로세스의 complete time
            present.complete += present.process.burstTime;
            ganttChartInformation[0].completeTime[present.process.id-1] = present.complete;

            // 이 프로세스의 start, end
            present.processingTime = new ProcessingTime();
            present.end += present.process.burstTime;
            present.processingTime.start = present.start;
            present.processingTime.end = present.end;
            present.processingTime.burst = present.processingTime.end - present.processingTime.start;
            ganttChartInformation[0].processingTime[present.process.id-1].add(present.processingTime);

            // 이 프로세스의 turnaround time
            // complete time - arrival time
            ganttChartInformation[0].turnaroundTime[present.process.id-1] = present.complete - present.process.arrivalTime;

            // 전체 turnaround time
            // turnaroundTime[0]이 FCFS
            // 현재 프로세스의 turnaround time을 전체에 더해주기
            turnaroundTime[0].total += ganttChartInformation[0].turnaroundTime[present.process.id-1];

            // 이 프로세스의 wait time
            // 프로세스 start time - arrival time
            ganttChartInformation[0].waitTime[present.process.id-1] = present.processingTime.start - present.process.arrivalTime;

            // 전체 waiting time
            // waitingTime[0]이 FCFS
            // 현재 프로세스의 wait time을 전체에 더해주기
            waitingTime[0].total += ganttChartInformation[0].waitTime[present.process.id-1];

            // 다음 프로세스의 시작 시간 = 현재 프로세스 완료시간
            present.start = present.complete;
        }

        turnaroundTime[0].avg = turnaroundTime[0].total / processNum;
        waitingTime[0].avg = waitingTime[0].total / processNum;

        // 출력
        // FCFS의 algorithmNum = 0
        printAlgorithmResult(0);
    }

    /////////////////////////////////////////////////////////////////////////
    // SJF
    public void SJF() {
        // 초기에 remaining time을 burst time으로 설정
        Iterator<Process> it = processList.iterator();
        while(it.hasNext()) {
            Process tmpProcess = it.next();
            tmpProcess.remainingTime = tmpProcess.burstTime;
        }

        // 도착하기 전의 프로세스들을 도착시간이 빠른 기준인 우선순위 큐에 넣음
        PriorityQueue<Process> beforeArrivalQueue = new PriorityQueue<>(new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2) {
                if (p1.arrivalTime > p2.arrivalTime) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        it = processList.iterator();
        while (it.hasNext()) {
            Process tmpProcess = it.next();
            beforeArrivalQueue.add(tmpProcess);
        }

        // remaining time이 기준인 readyQ(우선순위 큐) 생성
        // remaining time이 빠를 수록 우선순위가 높음
        // remaining time이 같으면 arrival time이 빠를 수록 우선순위가 높음
        PriorityQueue<Process> readyQ = new PriorityQueue<>(new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2){
                if(p1.remainingTime > p2.remainingTime){
                    return 1;
                } else if(p1.remainingTime == p2.remainingTime){
                    if(p1.arrivalTime > p2.arrivalTime){
                        return 1;
                    } else{
                        return -1;
                    }
                } else {
                    return -1;
                }
            }
        });


        Present present = new Present(); // 현재 값들
        // 도착시간이 제일 빠른 프로세스를 readyQ에 넣기
        readyQ.add(beforeArrivalQueue.remove());
        // 현재 실행 중인 프로세스 = readyQ에서 꺼내기
        present.process = readyQ.remove();
        // 전체시간만큼 1초씩 진행
        while(present.time <= totalProcessingTime) {
            // 아직 도착하지 않은 프로세스들 중에 도착시간 == 현재시간인 프로세스가 없을 때까지 반복
            while (true) {
                // 아직 도착하지 않은 프로세스가 있고 && 제일 빨리 도착하는 프로세스의 도착시간 == 현재시간
                if (!beforeArrivalQueue.isEmpty() && beforeArrivalQueue.peek().arrivalTime == present.time) {
                    // 새로운 프로세스 도착
                    Process newProcess = beforeArrivalQueue.remove();
                    // remaining time이 새로운 프로세스 < 실행 중인 프로세스 -> 선점
                    if (newProcess.remainingTime < present.process.remainingTime) {
                        // 현재 실행 중이던 프로세스의 processing time 구하기
                        present.processingTime = new ProcessingTime();
                        present.end = present.time;
                        present.processingTime.start = present.start;
                        present.processingTime.end = present.end;
                        present.processingTime.burst = present.end - present.start;
                        // 현재 실행 중이던 프로세스가 실행이 되었을 때만 processing time을 간트차트에 표시
                        // 같은 시간에 여러 개의 프로세스가 들어왔으면 remaining time(새 프로세스이므로 burst time)이 가장 짧은 게 실행되기 때문!
                        // beforeArrivalQueue에서 도착 시간은 같은데 remaining time(burst time)이 더 짧은 게 뒤에 들어오면 앞에껀 burst time = 0 으로 계산될 것임
                        if (present.processingTime.burst != 0) {
                            ganttChartInformation[1].processingTime[present.process.id-1].add(present.processingTime);
                        }
                        // 실행 중인 프로세스를 readyQ에 넣음
                        readyQ.add(present.process);
                        // 새로운 프로세스 실행(선점)
                        present.process = newProcess;
                    } else {
                        // 새로운 프로세스를 readyQ에 넣음
                        readyQ.add(newProcess);
                    }
                } else {
                    break;
                }
            }

            // 프로세스 실행 완료 = 남은 시간이 0
            if (present.process.remainingTime == 0) {
                // 완료된 프로세스의 complete time = 현재 시간
                present.complete = present.time;
                ganttChartInformation[1].completeTime[present.process.id-1] = present.complete;

                // 완료된 프로세스의 processing time 구하기
                present.processingTime = new ProcessingTime();
                present.end = present.time;
                present.processingTime.start = present.start;
                present.processingTime.end = present.end;
                present.processingTime.burst = present.end - present.start;
                ganttChartInformation[1].processingTime[present.process.id-1].add(present.processingTime);

                // 완료된 프로세스의 wait time = complete time - burst time - arrival time
                ganttChartInformation[1].waitTime[present.process.id-1] = present.complete - present.process.burstTime - present.process.arrivalTime;

                // 전체 waiting time
                waitingTime[1].total += ganttChartInformation[1].waitTime[present.process.id-1];

                // 완료된 프로세스의 turnaround time = complete time - arrival time
                ganttChartInformation[1].turnaroundTime[present.process.id-1] = present.complete - present.process.arrivalTime;

                // 전체 turnaround time
                turnaroundTime[1].total += ganttChartInformation[1].turnaroundTime[present.process.id-1];

                // 새로운 프로세스 실행 - remaining time이 가장 짧은 프로세스
                if (!readyQ.isEmpty()) {
                    present.process = readyQ.remove();
                }
            }
            present.process.remainingTime -= 1; // 현재 실행 중인 프로세스 남은시간 -= 1
            // 다음 시작 시간 = 현재 끝 시간
            // end는 process 실행이 끝날 때마다 업데이트 되기 때문에 start 값은 현재 process 실행이 끝날 때까지 그대로 유지됨
            present.start = present.end;
            // 시간 1초 지남
            present.time += 1;
        }
        // 평균 turnaroud time, 평균 waiting time
        turnaroundTime[1].avg = (double)turnaroundTime[1].total / (double)processNum;
        waitingTime[1].avg = (double)waitingTime[1].total / (double)processNum;

        // 출력
        // SJF의 algorithmNum = 1
        printAlgorithmResult(1);
    }
    /////////////////////////////////////////////////////////////////////////
    // Priority
    public void Priority() {
        // 초기에 remaining time을 burst time으로 설정
        Iterator<Process> it = processList.iterator();
        while(it.hasNext()) {
            Process tmpProcess = it.next();
            tmpProcess.remainingTime = tmpProcess.burstTime;
        }

        // 도착하기 전의 프로세스들을 도착시간이 빠른 기준인 우선순위 큐에 넣음
        PriorityQueue<Process> beforeArrivalQueue = new PriorityQueue<>(new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2) {
                if (p1.arrivalTime > p2.arrivalTime) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        it = processList.iterator();
        while (it.hasNext()) {
            Process tmpProcess = it.next();
            beforeArrivalQueue.add(tmpProcess);
        }

        // priority가 기준인 readyQ(우선순위 큐) 생성
        // priority 숫자가 작을 수록 우선순위가 높음
        // priority가 같으면 arrival time이 빠를 수록 우선순위가 높음
        PriorityQueue<Process> readyQ = new PriorityQueue<>(new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2){
                if(p1.priority > p2.priority){
                    return 1;
                } else if(p1.priority == p2.priority){
                    if(p1.arrivalTime > p2.arrivalTime){
                        return 1;
                    } else{
                        return -1;
                    }
                } else {
                    return -1;
                }
            }
        });

        Present present = new Present(); // 현재 값들
        // 도착시간이 제일 빠른 프로세스를 readyQ에 넣기
        readyQ.add(beforeArrivalQueue.remove());
        // 현재 실행 중인 프로세스 = readyQ에서 꺼내기
        present.process = readyQ.remove();
        // 전체시간만큼 1초씩 진행
        while(present.time <= totalProcessingTime) {
            // 아직 도착하지 않은 프로세스들 중에 도착시간 == 현재시간인 프로세스가 없을 때까지 반복
            while (true) {
                // 아직 도착하지 않은 프로세스가 있고 && 제일 빨리 도착하는 프로세스의 도착시간 == 현재시간
                if (!beforeArrivalQueue.isEmpty() && beforeArrivalQueue.peek().arrivalTime == present.time) {
                    // 새로운 프로세스 도착
                    Process newProcess = beforeArrivalQueue.remove();
                    // priority가 새로운 프로세스 < 실행 중인 프로세스 -> 선점
                    if (newProcess.priority < present.process.priority) {
                        // 현재 실행 중이던 프로세스의 processing time 구하기
                        present.processingTime = new ProcessingTime();
                        present.end = present.time;
                        present.processingTime.start = present.start;
                        present.processingTime.end = present.end;
                        present.processingTime.burst = present.end - present.start;
                        // 현재 실행 중이던 프로세스가 실행이 되었을 때만 processing time을 간트차트에 표시
                        // 같은 시간에 여러 개의 프로세스가 들어왔으면 priority 숫자가 가장 작은 게 실행되기 때문!
                        // beforeArrivalQueue에서 도착 시간은 같은데 priority 숫자가 더 작은 게 뒤에 들어오면 앞에껀 burst time = 0 으로 계산될 것임
                        if (present.processingTime.burst != 0) {
                            ganttChartInformation[2].processingTime[present.process.id-1].add(present.processingTime);
                        }
                        // 실행 중인 프로세스를 readyQ에 넣음
                        readyQ.add(present.process);
                        // 새로운 프로세스 실행(선점)
                        present.process = newProcess;
                    } else {
                        // 새로운 프로세스를 readyQ에 넣음
                        readyQ.add(newProcess);
                    }
                } else {
                    break;
                }
            }

            // 프로세스 실행 완료 = 남은 시간이 0
            if (present.process.remainingTime == 0) {
                // 완료된 프로세스의 complete time = 현재 시간
                present.complete = present.time;
                ganttChartInformation[2].completeTime[present.process.id-1] = present.complete;

                // 완료된 프로세스의 processing time 구하기
                present.processingTime = new ProcessingTime();
                present.end = present.time;
                present.processingTime.start = present.start;
                present.processingTime.end = present.end;
                present.processingTime.burst = present.end - present.start;
                ganttChartInformation[2].processingTime[present.process.id-1].add(present.processingTime);

                // 완료된 프로세스의 wait time = complete time - burst time - arrival time
                ganttChartInformation[2].waitTime[present.process.id-1] = present.complete - present.process.burstTime - present.process.arrivalTime;

                // 전체 waiting time
                waitingTime[2].total += ganttChartInformation[2].waitTime[present.process.id-1];

                // 완료된 프로세스의 turnaround time = complete time - arrival time
                ganttChartInformation[2].turnaroundTime[present.process.id-1] = present.complete - present.process.arrivalTime;

                // 전체 turnaround time
                turnaroundTime[2].total += ganttChartInformation[2].turnaroundTime[present.process.id-1];

                // 새로운 프로세스 실행 - priority 숫자가 가장 작은 프로세스
                if (!readyQ.isEmpty()) {
                    present.process = readyQ.remove();
                }
            }
            present.process.remainingTime -= 1; // 현재 실행 중인 프로세스 남은시간 -= 1
            present.start = present.end; // 다음 시작 시간 = 현재 끝 시간
            // 시간 1초 지남
            present.time += 1;
        }
        // 평균 turnaroud time, 평균 waiting time
        turnaroundTime[2].avg = (double)turnaroundTime[2].total / (double)processNum;
        waitingTime[2].avg = (double)waitingTime[2].total / (double)processNum;

        // 출력
        // Priority의 algorithmNum = 2
        printAlgorithmResult(2);
    }
    public void RR() {
        // 초기에 remaining time을 burst time으로 설정
        Iterator<Process> it = processList.iterator();
        while(it.hasNext()) {
            Process tmpProcess = it.next();
            tmpProcess.remainingTime = tmpProcess.burstTime;
        }

        // 도착하기 전의 프로세스들을 도착시간이 빠른 기준인 우선순위 큐에 넣음
        PriorityQueue<Process> beforeArrivalQueue = new PriorityQueue<>(new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2) {
                if (p1.arrivalTime > p2.arrivalTime) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        it = processList.iterator();
        while (it.hasNext()) {
            Process tmpProcess = it.next();
            beforeArrivalQueue.add(tmpProcess);
        }

        // readyQ(선입선출 큐) 생성
        Queue<Process> readyQ = new LinkedList<>();

        Present present = new Present(); // 현재 값들
        // 전체시간만큼 time quantum초씩 진행
        while(present.time < totalProcessingTime) {
            // 처음시간이나 어떤 프로세스 실행이 끝난 시간에 들어오는 모든 프로세스들을 readyQ에 넣음
            while (true) {
                // 아직 도착하지 않은 프로세스가 있고 && 제일 빨리 도착하는 프로세스의 도착시간 == 현재시간
                if (!beforeArrivalQueue.isEmpty() && beforeArrivalQueue.peek().arrivalTime == present.time) {
                    Process newProcess = beforeArrivalQueue.remove();
                    readyQ.add(newProcess);
                } else {
                    break;
                }
            }

            // 현재 실행 중인 프로세스 = readyQ에서 꺼내기
            if (!readyQ.isEmpty()) {
                present.process = readyQ.remove();
                // 이 프로세스의 시작시간 = 현재 시간
                present.start = present.time;
            }

            // 이번 차례에 이 프로세스 실행 완료
            if (present.process.remainingTime <= timeQuantum) {
                // 이 프로세스가 실행되는 동안(끝나기 전) 들어온 프로세스들 모두 readyQ에 넣기
                while (true) {
                    // 아직 도착하지 않은 프로세스가 있고 && 제일 빨리 도착하는 프로세스의 도착시간 < 현재시간 + remaining time
                    if (!beforeArrivalQueue.isEmpty() && beforeArrivalQueue.peek().arrivalTime < present.time + present.process.remainingTime) {
                        Process newProcess = beforeArrivalQueue.remove();
                        readyQ.add(newProcess);
                    } else {
                        break;
                    }
                }

                // 완료된 프로세스의 complete time = 현재 시간 + 프로세스의 남은 burst time
                present.complete = present.time + present.process.remainingTime;
                ganttChartInformation[3].completeTime[present.process.id-1] = present.complete;

                // 완료된 프로세스의 processing time 구하기
                present.processingTime = new ProcessingTime();
                present.end = present.complete; // 끝 시간 = 완료 시간
                present.processingTime.start = present.start;
                present.processingTime.end = present.end;
                present.processingTime.burst = present.end - present.start;
                ganttChartInformation[3].processingTime[present.process.id-1].add(present.processingTime);

                // 완료된 프로세스의 wait time = complete time - burst time - arrival time
                ganttChartInformation[3].waitTime[present.process.id-1] = present.complete - present.process.burstTime - present.process.arrivalTime;

                // 전체 waiting time
                waitingTime[3].total += ganttChartInformation[3].waitTime[present.process.id-1];

                // 완료된 프로세스의 turnaround time = complete time - arrival time
                ganttChartInformation[3].turnaroundTime[present.process.id-1] = present.complete - present.process.arrivalTime;

                // 전체 turnaround time
                turnaroundTime[3].total += ganttChartInformation[3].turnaroundTime[present.process.id-1];

                // 현재 프로세스가 이번에 실행된 시간만큼 시간이 흐름
                present.time += present.process.remainingTime;
                present.process.remainingTime = 0; // 실행이 완료돼서 remaining time = 0이 됨
            }

            // 프로세스 remaining time이 time quantum보다 많이 남음
            else if (present.process.remainingTime > timeQuantum) {
                // 이 프로세스가 실행되는 동안(끝나기 전) 들어온 프로세스들 모두 readyQ에 넣기
                while (true) {
                    // 아직 도착하지 않은 프로세스가 있고 && 제일 빨리 도착하는 프로세스의 도착시간 < 현재시간 + time quantum
                    if (!beforeArrivalQueue.isEmpty() && beforeArrivalQueue.peek().arrivalTime < present.time + timeQuantum) {
                        Process newProcess = beforeArrivalQueue.remove();
                        readyQ.add(newProcess);
                    } else {
                        break;
                    }
                }

                // 완료된 프로세스의 processing time 구하기
                present.processingTime = new ProcessingTime();
                present.end = present.time + timeQuantum; // 끝 시간 = 현재 시간 + time quantum
                present.processingTime.start = present.start;
                present.processingTime.end = present.end;
                present.processingTime.burst = present.end - present.start;
                ganttChartInformation[3].processingTime[present.process.id-1].add(present.processingTime);

                // time quantum만큼 시간이 흐름
                present.time += timeQuantum;
                present.process.remainingTime -= timeQuantum;

                // 현재 프로세스는 다시 readyQ로 삽입됨
                // 프로세스 실행이 끝나는 시간에 새로운 프로세스들이 들어오면 방금 실행했던 프로세스가 우선권을 가짐(먼저 readyQ에 들어감_
                readyQ.add(present.process);
            }
        }

        // 평균 turnaroud time, 평균 waiting time
        turnaroundTime[3].avg = (double)turnaroundTime[3].total / (double)processNum;
        waitingTime[3].avg = (double)waitingTime[3].total / (double)processNum;

        // 출력
        // RR의 algorithmNum = 3
        printAlgorithmResult(3);
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
