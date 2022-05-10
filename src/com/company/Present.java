package com.company;

// 알고리즘 실행할 때 현재 값들
class Present {
    Process process; // 현재 실행되고 있는 프로세스
    int complete = 0; // 현재 프로세스의 완료시간
    int start = 0; // 현재 프로세스의 시작시간
    int end = 0; // 현재 프로세스의 종료시간
    ProcessingTime processingTime; // 현재 프로세스의 실행시간
    int time = 0; // 현재 시간
}