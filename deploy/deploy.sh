#!/bin/bash
date
usage(){
    echo "  Usage:"
    echo "     use '$0 dev/qa/yanghao web/agent/storage/all'."
    exit 1
}

ENV=$1
MODULE=$2
cd `dirname "$0"`
PRGDIR=`pwd`

if [ "$ENV" != "qa" -a "$ENV" != "dev" -a "$ENV" != "yanghao" ]; then
  echo "Your input is not corrent!"
  usage
  exit 1
fi
if [ "$MODULE" != "web" -a "$MODULE" != "agent" -a "$MODULE" != "storage" -a "$MODULE" != "all" ]; then
  echo "Your input is not corrent!"
  usage
  exit 1
fi

#svn co
cd /tmp
rm -rf yscheduler
svn co svn://svn.dy/platform/yscheduler/branches/develop yscheduler
cd yscheduler

#maven 
MVN_PROFILE="-P${ENV}"
mvn clean package -DskipTests $MVN_PROFILE

AGENT_HOST="172.20.0.160"
WEB_HOST="172.20.0.160"
STORAGE_HOST="172.20.0.180"
USER="ops"
PASSWORD="ops@123"
DATE=$(date +%Y%m%d_%H%M%S)
VERSION=`mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version | grep -v '\['`
echo "version is $VERSION"

if [ "$ENV" == "dev" ]; then
  AGENT_HOST="172.20.0.135"
  WEB_HOST="172.20.0.135"
  STORAGE_HOST="172.20.0.180"
  USER="ops"
  PASSWORD="ops@123"
elif [ "$ENV" == "yanghao" ]; then
  AGENT_HOST="172.30.50.52"
  WEB_HOST="172.30.50.52"
  STORAGE_HOST="172.20.0.180"
  USER="root"
  PASSWORD="12354"
fi

cd $PRGDIR
if [ "$MODULE" == "web" ]; then
  echo "=============== deploying web to $WEB_HOST ============="
  ./deploy-web.sh $WEB_HOST $USER $PASSWORD $DATE $VERSION
elif [ "$MODULE" == "agent" ]; then
  echo "=============== deploying agent to $AGENT_HOST ============="
  ./deploy-agent.sh $AGENT_HOST $USER $PASSWORD $DATE $VERSION $WEB_HOST
elif [ "$MODULE" == "storage" ]; then
  echo "=============== deploying storage to $STORAGE_HOST ============="
  ./deploy-storage.sh $STORAGE_HOST $USER $PASSWORD $DATE $VERSION $ENV
else
  echo "=============== deploying web to $WEB_HOST ============="
  ./deploy-web.sh $WEB_HOST $USER $PASSWORD $DATE $VERSION
  echo "=============== deploying agent to $AGENT_HOST ============="
  ./deploy-agent.sh $AGENT_HOST $USER $PASSWORD $DATE $VERSION $WEB_HOST
  echo "=============== deploying storage to $STORAGE_HOST ============="
  ./deploy-storage.sh $STORAGE_HOST $USER $PASSWORD $DATE $VERSION $ENV
fi

#rm
rm /tmp/yscheduler -rf\r
#go back
cd $PRGDIR
