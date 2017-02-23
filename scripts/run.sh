#!/usr/bin/env sh

if [ -n "$RANCHER_APIV2_URL" ]; then
    while true; do

     java -jar /root/rancher2git.jar \
       ${RANCHER_APIV2_URL} ${RANCHER_API_USER} ${RANCHER_API_PASS}  \
       ${GIT_REPO_URL} ${GIT_REPO_USER} ${GIT_REPO_PASS};  \

     sleep ${UPDATE_EVERY};

    done
fi

if [ -n "$CATTLE_URL" ]; then
    while true; do

     java -jar /root/rancher2git.jar \
       ${CATTLE_URL} ${CATTLE_ACCESS_KEY} ${CATTLE_SECRET_KEY}  \
       ${GIT_REPO_URL} ${GIT_REPO_USER} ${GIT_REPO_PASS};  \

     sleep ${UPDATE_EVERY};

    done
fi

echo "You have to provide your Rancher API URL either by setting RANCHER_APIV2_URL environment variable or the tags: "
echo "io.rancher.container.create_agent=true"
echo "io.rancher.container.agent.role=environment"

exit -1