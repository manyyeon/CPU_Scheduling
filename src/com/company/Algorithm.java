package com.company;

import java.util.*;

// 알고리즘 결과 시간
class ResultTime {
    int total = 0;
    int avg = 0;
}

// 알고리즘 클래스
class Algorithm {
    ArrayList<Process> processList; // process들
    ResultTime waitingTime; // 대기시간
    ResultTime turnaroundTime; // 총처리시간
    int timeQuantum; // time quantum
}
