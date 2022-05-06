package com.company;

import java.util.*;

class StartEnd {
    int start;
    int end;
}

class Process {
    int id; // process id number
    int arrivalTime; // arrival time
    int burstTime; // burst time
    int priority; // 우선순위
    int processingTime; // 수행시간
    int waitTime; // wait time
    int completeTime; // complete time
    int remainingTime; // remaining time
    ArrayList<StartEnd> startEnd; // {시작, 끝}
}