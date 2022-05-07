package com.company;

import java.util.ArrayList;

// 수행시간
class ProcessingTime {
    int start = 0;
    int end = 0;
    int burst = 0;
}

// 알고리즘 결과 시간
class ResultTime {
    int total = 0;
    int avg = 0;
}

// 알고리즘 실행할 때 현재 값들
class Present {
    Process process; // 현재 실행되고 있는 프로세스
    int complete = 0; // 현재 프로세스의 완료시간
    int start = 0; // 현재 프로세스의 시작시간
    int end = 0; // 현재 프로세스의 종료시간
    ProcessingTime processingTime; // 현재 프로세스의 실행시간
}

// 간트차트에 필요한 정보들
class GanttChartInformation {
    int [] waitTime; // 프로세스 별 wait time
    int [] completeTime; // 프로세스 별 complete time
    int [] remainingTime; // 프로세스 별 remaining time
    int [] turnaroundTime; // 프로세스 별 turnaround time
    ArrayList<ProcessingTime>[] processingTime; // 프로세스 별 실행시간

    // 프로세스 개수만큼 배열 생성
    public GanttChartInformation(int processNum){
        waitTime = new int[processNum];
        completeTime = new int[processNum];
        remainingTime = new int[processNum];
        turnaroundTime = new int[processNum];
        processingTime = new ArrayList[processNum];
        for(int i=0; i<processNum; i++){
            processingTime[i] = new ArrayList<>();
        }
    }
}

