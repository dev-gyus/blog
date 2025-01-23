#!/bin/bash
PROCESS_NO=$(pgrep -f nohup)
if [ -z $PROCESS_NO ]; then
  echo "> 실행중인 프로세스가 없습니다"
else
  echo "> 실행중인 프로세스 Id = $PROCESS_NO"
  sudo kill -15 $PROCESS_NO # tcp $serverPort에 해당하는 port를 Kill함.
fi

# 프로세스가 down될때까지 대기
IS_PROCESS_KILLED=$(pgrep -f nohup)
while [[ -n $IS_PROCESS_KILLED ]]
  do
    echo "> 프로세스 종료 대기중"
    IS_PROCESS_KILLED=$(pgrep -f nohup)
    echo "> IS_PROCESS_KILLED = $IS_PROCESS_KILLED"
  done