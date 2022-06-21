# OS_CPU_Scheduling
운영체제 CPU scheduling algorithm(FCFS, SJF, Priority, RR) java로 구현

멀티미디어운영체제 과제2

## CPU 스케줄링 알고리즘
FCFS, SJF, Priority, RR 알고리즘 구현

언어 : java

## 목차
1.	전체적인 구조
2.	프로그램 작동 흐름
3.	알고리즘 구현 방법설명
  1)	FCFS
  2)	SJF
  3)	Priority
  4)	RR
4.	실행결과
5.	소스코드
 
### 1.	전체적인 구조
## 1)	Process 클래스
프로세스의 기본 정보가 저장되어 있는 클래스이다.
### 변수
```
int id; // 프로세스 id 번호
int arrivalTime; // 도착 시간
int burstTime; // busrt time
int priority; // 우선순위
int remaining time; // 남은 시간
```
알고리즘을 수행할 때 남은 시간 계산이 필요하므로 여기에 정의해두고 Algorithm 클래스 안에 있는 setRemainingTime 함수로 burst time으로 초기화해준다.

--------

## 2)	ProcessingTime 클래스
수행시간을 저장해 놓는 클래스이다.
### 변수
```
int start = 0;
int end = 0;
int burst = 0;
```

<img width="452" alt="image" src="https://user-images.githubusercontent.com/87538540/174724947-bcdd90fd-26da-4792-ab6b-80f0af5892bd.png">

그림과 같이 한 프로세스가 전체 시간에서 언제 수행을 했는지 표시해주기 위한 변수들이다. [시작시간-끝시간](burst time)의 형식으로 나열하였다. 이렇게 나열하기 위해서 뒤에서 설명할 GanttChartInformation 클래스에 자동으로 배열 크기를 조절해주는 ProcessingTime형 ArrayList를 정의하여 프로세스가 수행을 할 때마다 ArrayList에 추가해주었다. 

---------

## 3)	GanttChartInformation 클래스
간트 차트를 표시할 때 필요한 정보들을 저장해 놓는 클래스이다. 프로세스 별 정보들이 변수로 저장되어 있다.
### 변수
```
int [] waitTime; // 프로세스 별 wait time
int [] completeTime; // 프로세스 별 complete time
int [] turnaroundTime; // 프로세스 별 turnaround time
ArrayList<ProcessingTime>[] processsingTime; // 프로세스 별 실행시간
```
앞에서 언급한 ProcessingTime형 ArrayList이다.
사진과 같은 정보들을 표시해주기 위해 필요한 변수들이다.

<img width="149" alt="image" src="https://user-images.githubusercontent.com/87538540/174725347-64429fef-f3a1-4c71-84fa-1e09f078c94d.png">
 
### 생성자
GanttChartInformation 생성자에서 waitTime, completeTime, turnaroundTime, processinTime의 공간을 할당해준다.

-----

## 4) Present 클래스
### 변수
알고리즘을 실행할 때 현재 값들을 저장하기 위한 클래스이다. 이 임시값들을 이용하여 먼저 계산을 해두고 GanttChartInformation 클래스에 정의되어 있는 변수들에 값을 저장한다.
```
Process process; // 현재 실행되고 있는 프로세스
int complete = 0; // 현재 프로세스의 완료 시간
int start = 0; // 현재 프로세스의 시작 시간
int end = 0; // 현재 프로세스의 종료 시간
ProcessingTime processingTime; // 현재 프로세스의 실행시간
int time = 0; // 현재 시간
```
SJF, Priority, RR 알고리즘은 현재 시간이 진행됨에 따라 알고리즘도 진행되므로 현재 시간 변수가 필요하다.

-----

## 5) Result Time 클래스
알고리즘 결과 시간을 저장하는 클래스이다.
### 변수
```
int total = 0;
double avg = 0;
```

-----

## 6) Main 클래스
Main 클래스에서 전체적인 프로그램 흐름을 제어한다. 정보를 입력받고 알고리즘을 수행하는 역할
### 변수
```
int processNum; // 프로세스 개수
Algorithm algorithm; // 알고리즘
```
### 함수
```
input() // 초기 프로세스 정보, time quantum 입력받기
```

<img width="279" alt="image" src="https://user-images.githubusercontent.com/87538540/174725309-d17d04cf-f1fc-4d30-a9f2-1955fdda0157.png">

process 개수를 입력 받는다. 이때, 프로세스 개수가 4개 이상이면다시 입력 받게 예외처리를 해주었다. 알고리즘 객체인 algorithm을 생성하고 매개변수로 프로세스 개수인 processNum을 전달해준다. 새로운 프로세스 객체인 newProcess를 생성하고 id는 들어오는 순서대로 1번부터 정한다. arrival time, burst time, priority를 입력 받고 algorithm의 프로세스 리스트인 processList에 새 프로세스를 추가해준다. 그리고 algorithm의 totalProcessingTime에 새 프로세스의 burst time을 더해준다. process가 들어올 때마다 burst time을 더해주면 결국 전체 processing time을 구할 수 있다. 마지막으로 time quantum을 입력받는다.
```
printInitialAllProcess() // 입력받은 초기 프로세스 정보 출력
```

<img width="308" alt="image" src="https://user-images.githubusercontent.com/87538540/174725457-46a01316-531d-41ae-bd12-baa208bc95a4.png">
 
```
main() // 프로그램 전체를 돌리는 함수
```
초기 프로세스들의 정보와 time quantum을 입력 받고 입력 받은 정보를 출력한다. 그리고 알고리즘 각각을 수행한다.

--------

## 7)	Algorithm 클래스
알고리즘을 수행하고 결과를 출력해주는 클래스이다. 알고리즘 별로 번호를 지정해두고 인덱스로 활용하였다.
FCFS = 0, SJF = 1, Priority = 2, RR = 3
### 변수
```
ArrayList<Process> processList; // process들을 담는 리스트
int processNum = 0; // 프로세스 개수
int totalProcessingTime; // 모든 프로세스 실행이 끝나는 시간
ResultTime [] waitingTime; // 알고리즘 별 대기시간
Resulttime [] turnaroundTime; // 알고리즘 별 총처리시간
GanttChartInformation [] ganttChartInformation; // 알고리즘 별 간트차트 정보
int time quantum; // time quantum
```
알고리즘 별로 정보를 저장할 수 있는 배열들을 정의해 놓았고 전체적으로 필요한 프로세스리스트, 프로세스 개수, 전체 프로세싱 시간, time quantum을 정의했다. 알고리즘 별 간트차트 정보에서 앞서 정의해둔 GanttChartInformation형 배열을 썼기 때문에 알고리즘 별 간트차트 정보 안에 프로세스 별 정보들이 들어있다. 

<img width="396" alt="image" src="https://user-images.githubusercontent.com/87538540/174725634-1fdf0a1d-5cbc-492a-8b3e-2d7c796642de.png">

### 생성자
매개변수로 넘겨받은 프로세스 개수를 설정해주고 알고리즘 별 waiting time, turnaround time, 간트 차트 정보 공간을 할당해준다.

### 함수
```setRemainingTime()```

초기에 remaining time을 burst time으로 설정하는 함수

processList에 접근해서 processList에 들어있는 모든 프로세스들의 remaining time을 burst time으로 설정해둠
알고리즘 시작할 때 이 함수로 초기화를 한다.

```FCFS()```
FCFS 알고리즘을 수행하는 함수

```SJF()```
SJF 알고리즘을 수행하는 함수

```Priority()```
Priority 알고리즘을 수행하는 함수

```RR()```
RR 알고리즘을 수행하는 함수

```printAlgorithmResult()```
알고리즘 수행 결과를 출력해준다. 매개변수로 algorithmNum을 받아서 번호에 따라 이름을 설정한다. 0은 FCFS, 1은 SJF, 2는 Priority, 3은 RR이다. ganttChartInformation과 waitingTime, turnaroundTime 변수에 접근하여 알고리즘 번호에 따른 저장된 정보들을 모두 출력해준다.

# 2. 프로그램 작동 흐름
Main 클래스의 객체인 mainObject를 생성한다. mainObject의 내부 함수인 input()를 이용하여 프로세스 개수, 프로세스들의 정보, time quantum를 입력받고 프로세스 개수를 매개변수로 넘겨주면서 algorithm 객체를 생성한다. algorithm 객체 안에 있는 processList에 직접 접근하여 입력 받은 프로세스들을 넣어준다. 그리고 전체 프로세스 실행 시간도 구한다. 그 이후, printInitialAllProcess() 함수로 입력 받은 정보를 출력해준다.
 algorithm 객체에서 각각의 알고리즘 함수를 호출하여 알고리즘을 수행하고 printAlgorithmResult() 함수를 이용하여 결과를 출력해준다. 모든 정보들은 알고리즘 클래스 안에 변수로 정의되어 있으며 구조를 효율적으로 사용하기 위해 ResultTime 클래스, Present 클래스, Process 클래스, ProcessingTime 클래스, GanttChartInformation 클래스를 만들었다. 모든 정보는 Algorithm 클래스에 있지만 세부적인 내용을 쪼개서 담은 것이다.

# 3. 알고리즘 구현 방법 설명
## 1) FCFS
먼저 온 프로세스를 먼저 처리해주는 알고리즘이다. 비선점형이므로 모든 프로세스가 1번씩 처리되어 완료될 때까지 프로세스 개수만큼 반복문을 돌리면 된다.
우선, arrival time이 기준인 우선순위 큐를 생성한다. arrival time이 빠를 수록 우선순위가 높아. 만약 arrival time이 같은 프로세스가 있다면 그냥 임의로 프로세스 id 번호가 작을 수록 우선순위가 높게 설정해 놓았다. 그 이후, 우선순위 큐에 모든 프로세스들을 넣어준다.
현재 값들을 저장해둘 Present 클래스의 객체인 present를 생성해준다. 프로세스 개수만큼 반복문을 실행한다. 우선순위 큐에서 프로세스 하나를 꺼내서(그럼 도착 시간이 가장 빠른 프로세스가 꺼내진다.) 현재 실행 중인 프로세스인 present.process에 설정해 둔다. 어차피 FCFS에서는 프로세스들이 한 번 처리될 때 끝까지 처리되고 종료되므로 바로 complete time을 구해준다. 현재 complete time은 이전 complete time에 현재 프로세스의 burst time을 더한 값이다.
그리고 바로 processing time을 구한다. 현재 프로세스의 끝 시간인 present.end도 complete time과 똑같이 구하면 된다. 현재 프로세스의 시작시간인 present.start는 이전 끝 시간 값이 저장되어 있다. burst time은 end-start로 구할 수 있다. present.start는 반복문 마지막에서 현재 끝 시간으로 설정해준다. 그럼 다음 start 시간을 구할 수 있다.
이 3개의 값을 구한 후에 present.processingTime의 start, end, burst에 값을 각각 넣어주고 이 processingTime을 ganttChartInformation의 인덱스 0(FCFS의 알고리즘 번호가 0번이다.)에 processingTime의 프로세스 id-1 인덱스에 추가해주면 된다. 프로세스 present.processingTime의 start, end 값과 present.start, present.end 값을 따로 쓴 것은 present.start와 present.end는 반복문 전체에서 계속 업데이트 하면서 사용하고 present.processingTime의 start, end는 현재의 processingTime을 구해서 ArrayList에 추가해주기 위한 값이기 때문이다. 
이 프로세스의 turnaround time은 complete time – arrival time으로 구한다. 알고리즘 별 전체 turnaround time도 구해야 하므로 전체에 더해준다.
이 프로세스의 wait time은 start time – arrival time으로 구한다. 알고리즘 별 전체 waiting time도 구해야 하므로 전체에 더해준다.

### Gantt Chart
<img width="353" alt="image" src="https://user-images.githubusercontent.com/87538540/174726094-e39d8c24-f415-41e8-b5a6-fbeb34718f02.png">

------------
 
## 2) SJF
remaining time을 기준으로 remaining time이 가장 적게 남은 프로세스부터 스케줄링 해주는 알고리즘이다. SJF의 선점형 알고리즘을 SRTF 알고리즘이라고 하기도 한다.
setRemainingTime() 함수를 이용하여 초기에 remaining time을 burst time으로 설정해둔다. 여기서는 큐를 2개 사용하였다. 도착시간 순서대로 초기에 모든 프로세스를 넣어 놓는 beforeArrivalQueue와 remaining time을 기준으로 remaining time이 적을 수록 우선순위가 높은 readyQ이다. 여기서 remaining time이 같으면 arrival time이 빠른 순서대로 정렬되도록 하였다. 현재 시간을 0초부터 모든 프로세스 수행이 끝나는 시간인 totalProcessingTime까지 1초씩 움직이면서 현재시간과 beforeArrivalQueue에 있는 프로세스의 도착시간이 같으면 프로세스가 도착한다고 하는 것이다. 어떤 시간에 여러 프로세스들이 한꺼번에 도착할 수도 있기 때문에 무한루프로 설정해두고 이 시간에 도착하는 프로세스가 더 이상 없으면 무한루프를 빠져나가게 설정해 두었다.
 새로운 프로세스가 도착하면 선점을 해서 바로 실행할지 readyQ에 넣을지 결정한다. 새로운 프로세스의 burst time(그런데 어차피 remaining time의 초기 설정을 burst time으로 해 두었기 때문에 remaining time으로 전부 비교했다.)이 실행 중인 프로세스의 remaining time보다 짧으면 선점하고 실행한다. 현재 실행 중이던 프로세스는 processing time을 구해서 ganttChartInformation에 추가해두고 readyQ에 넣는다. 그리고 새 프로세스를 현재 프로세스 즉, present.process로 설정한다. 만약 새로운 프로세스의 burst time이 실행 중인 remaining time보다 길면 새로운 프로세스를 바로 readyQ에 넣는다. 이렇게 이 시간에 도착하는 모든 프로세스에 대해 검사를 진행하면 된다.
그 이후, 현재 프로세스가 이 시간에 실행이 완료되었는지 검사한다. 현재 프로세스의 remaining time이 0이 되면 실행이 완료된 것이다. 그러면 complete time, processing time, wait time, turnaround time을 구하고 새로운 프로세스를 실행한다. 새로운 프로세스는 readyQ에서 하나 꺼내면 되는데 remaining time이 가장 짧은 프로세스가 나오게 될 것이다. complete time은 현재 시간이다. processing time은 end는 현재 시간이고 start는 반복문이 끝날 때 end로 계속 설정해주기 때문에 이전 프로세스 실행이 끝난 시간이다. end는 프로세스 실행이 끝날 때마다 업데이트 되기 때문에 start 값은 1초마다 end로 설정해 두어도 현재 프로세스 실행이 끝날 때까지 값을 유지한다. wait time은 complete time – burst time – arrival time이다. 전체 waiting time도 구해야 하기 때문에 전체에도 더해준다. turnaround time은 complete time – arrival time이다. 전체 turnaround time도 구해야 하기 때문에 전체에도 더해준다.
반복문이 끝나기 전에 현재 프로세스의 remaining time을 1초 빼주고 start를 end로 설정해준다. 현재 시간은 1초가 지난다.
반복문이 끝나면 turnaround time과 waiting time의 평균을 구하고 printAlgorithmResult() 함수를 이용하여 결과를 출력한다. 매개변수로는 SJF의 algorithmNum인 1을 전달해준다.

### Gantt Chart

<img width="358" alt="image" src="https://user-images.githubusercontent.com/87538540/174726142-bea17feb-37c0-4571-9c8c-d5d3d6f07f01.png">

-----------
 
## 3) Priority
Priority 알고리즘은 설정된 우선순위가 높은 순서대로 스케줄링 하는 알고리즘이다. Priority 알고리즘도 선점형으로 구현하였다. SJF가 Priority의 한 종류이므로 readyQ의 우선순위 기준만 빼고 다른 건 구현 방법이 모두 같다. readyQ에서 priority 값이 작을 수록 우선순위가 높도록 설정해 두었다.

### Gantt Chart

<img width="452" alt="image" src="https://user-images.githubusercontent.com/87538540/174726177-996e7303-2943-4e83-86eb-75a2302c4944.png">

--------
 
## 4) RR
RR 알고리즘은 time quantum 마다 현재 실행 중인 프로세스의 실행을 중단하고 새 프로세스를 스케줄 하는 알고리즘이다. 여기서도 반복문이 시간의 흐름에 따라 진행되지만 SJF와 Priority처럼 1초씩 지나는 게 아니라 time quantum초씩 지난다.
 우선, setRemainingTime() 함수로 초기에 remaining time을 burst time으로 설정한다. 그리고 beforeArrivalQueue에 모든 프로세스를 넣어 도착 시간이 빠른 순서대로 꺼낼 수 있게 해둔다. readyQ도 생성하는데 여기서는 우선순위 큐가 아니라 그냥 선입선출 큐이다. Present 클래스의 present 객체를 생성하고 반복문을 시작한다.
 반복문은 0초부터 시작해서 모든 프로세스의 실행이 끝나는 시간인 totalProcessingTime까지 time quantum초씩 진행한다.
 무한루프에서 이 시간에 도착하는 프로세스들을 모든 프로세스들을 readyQ에 넣는다. 무한루프에서 나오면 readyQ에서 프로세스 하나를 꺼내서 현재 프로세스로 설정한다. 그리고 이 프로세스의 시작시간인 present.start를 현재 시간으로 설정해 둔다.
그리고 이 프로세스의 remaining time이 time quantum보다 작거나 같은 지 큰 지 검사한다. 작거나 같으면 이번 차례에 이 프로세스 실행이 완료되는 것이고 크면 time quantum만큼 실행하고 다시 readyQ로 돌아가는 것이다.
 remaining time이 time quantum보다 작거나 같아서 이번 차례에 이 프로세스 실행을 완료하는 경우, 이 프로세스가 실행되는 동안(끝나기 전에) 들어온 프로세스들 모두를 readyQ에 넣는다. 끝나는 시간에 딱 맞게 들어오는 프로세스들은 어차피 다음 time quantum에서 처음 무한루프에 의해 다 들어오게 될 것이다. readyQ에 모두 넣고 나면 이 프로세스는 이번에 실행이 완료되는 것이기 때문에 complete time, processing time, wait time, turnaround time을 구하고 현재 시간을 이 프로세스의 remaining time만큼 흐르게 하고 이 프로세스의 remaining time을 0으로 설정한다. 여기서 time quantum 대신 remaining time만큼 시간이 흐르게 한 이유는 remaining time이 time quantum보다 작은 경우도 있기 때문이다. 그래서 complete time 계산할 때도 현재 시간 + remaining time으로 계산한다. processing time에서 end는 complete time과 같고 start는 이 프로세스가 시작할 때 설정해 두었다. burst time은 end – start로 구한다. wait time은 complete time – burst time – arrival time이고 전체 waiting time도 구해야 하기 때문에 전체에도 더해준다. turnaround time은 complete time – arrival time이다. 전체 turnaround time도 구해야 하기 때문에 전체에도 더해준다. 이렇게 끝나고 나면 다시 다음 반복이 실행될 때 그 시간에 도착하는 모든 프로세스들을 readyQ에 넣고 readyQ에서 프로세스 하나를 꺼내서 새로 실행하면 된다.
 remaining time이 time quantum보다 크면 time quantum만큼 실행하고 다시 readyQ에 넣어주면 된다. 우선, 이 프로세스가 실행되는 동안(끝나기 전에) 들어온 프로세스들 모두를 reayQ에 넣는다. 현재 프로세스를 time quantum만큼 실행해도 프로세스 실행이 완료되지 않으므로 processing time만 구한다. end는 현재 시간 + time quantum이고 start는 이 프로세스가 스케줄될 때 설정해두었다. burst time은 end – start이다. 그리고 time quantum만큼 시간이 흐르게 하고 이 프로세스의 remaining time에서 time quantum만큼 시간을 뺀다. 마지막으로 readyQ에 이 프로세스를 다시 넣어주면 된다. 그런데 이렇게 하면 현재 실행 중이던 프로세스를 먼저 readyQ에 넣고 그 후에 새로 도착하는 프로세스들을 readyQ에 넣는 것이므로 도착시간과 이 프로세스 실행이 끝난 시간이 같은 경우 실행 중이던 프로세스가 다음 차례에 한번 더 실행되게 된다. 하지만, 다른 프로세스들도 있을 때 3번 이상 연속으로 실행될 수는 없기 때문에 RR 알고리즘의 목적을 만족한다고 할 수 있다. 실행해본 예제에서도 새로 도착하는 프로세스의 도착시간과 프로세스 하나가 time quantum만큼 실행하고 끝나는 시간이 같은 경우가 있다. P1이 1초에 끝나는데 P2가 1초에 들어오는 것이다. 그래서 P1이 다시 한 번 실행이 되고 그 이후에 P2가 실행되었다.
아무튼 반복문이 끝나고 나면 평균 turnaround time과 평균 waiting time을 구하고 printAlgorithmResult() 함수를 통해 결과를 출력하면 된다. 매개변수로 RR 알고리즘의 algorithmNum인 3을 전달해주었다.

<img width="516" alt="image" src="https://user-images.githubusercontent.com/87538540/174726375-6c74d7ff-fdcd-4503-9dd7-7406c7011f88.png">

--------
 
# 4. 실행결과
<img width="261" alt="image" src="https://user-images.githubusercontent.com/87538540/174726442-51c92f32-0f5b-4612-a934-221f61961876.png">

<img width="231" alt="image" src="https://user-images.githubusercontent.com/87538540/174726451-f4edf845-7b78-4de2-aeb4-eba020cb3ba7.png">

<img width="452" alt="image" src="https://user-images.githubusercontent.com/87538540/174726473-6d9d8eef-378b-4157-af08-32ecf85a3151.png">
