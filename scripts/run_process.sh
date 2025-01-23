#!/bin/bash
# 임시폴더에서 deploy하는 폴더로 데이터 복사
sudo rm -rf /home/ec2-user/deploy
sudo mkdir /home/ec2-user/deploy
sudo cp -r /home/ec2-user/deploy-temp/* /home/ec2-user/deploy
# nohup을 그냥 실행하면 code deploy 콘솔에 로그가 찍히므로 nohup을 빠져나오도록 표준입출력관련된걸 모두 /dev/null로 보냄
# sudo nohup java -jar -Dspring.profiles.active=prod $JAR_FILE > /dev/null 2> /dev/null < /dev/null &
# 위 방법을 쓰면 배포는 정상적으로 되지만, 로그가 안남음. 따라서 EC2내부에 java 배포 쉘을 작성하고, 이를 CodeDeploy가 로그안찍고 배포하도록함
echo "> javadeploy.sh 백그라운드 실행. > /dev/null 2> /dev/null"
sudo sh /home/ec2-user/deploy-scripts/javadeploy.sh > /dev/null 2> /dev/null &