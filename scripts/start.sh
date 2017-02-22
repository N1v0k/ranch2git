#!/usr/bin/env bash

while true; do

 java -jar /root/rancher2git.jar \
   ${RANCHER_APIV2_URL} ${RANCHER_API_USER} ${RANCHER_API_PASS}  \
   ${GIT_REPO_URL} ${GIT_REPO_USER} ${GIT_REPO_PASS};  \

 sleep ${UPDATE_EVERY};

done