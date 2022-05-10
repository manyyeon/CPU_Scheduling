package com.company;

import java.util.ArrayList;

// 간트차트에 필요한 정보들
class GanttChartInformation {
    int [] waitTime; // 프로세스 별 wait time
    int [] completeTime; // 프로세스 별 complete time
    int [] turnaroundTime; // 프로세스 별 turnaround time
    ArrayList<ProcessingTime>[] processingTime; // 프로세스 별 실행시간

    // 프로세스 개수만큼 배열 생성
    public GanttChartInformation(int processNum){
        waitTime = new int[processNum];
        completeTime = new int[processNum];
        turnaroundTime = new int[processNum];
        processingTime = new ArrayList[processNum];
        for(int i=0; i<processNum; i++){
            processingTime[i] = new ArrayList<>();
        }
    }
}

