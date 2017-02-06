FROM openjdk:8-jre-alpine

ENV RANCHER_APIV2_URL = ""\
    RANCHER_API_USER = "" \
    RANCHER_API_PASS = "" \
    GIT_REPO_URL = "" \
    GIT_REPO_USER = "" \
    GIT_REPO_PASS = "" \
    UPDATE_EVERY = "5m"

COPY target/rancher2git-jar-with-dependencies.jar /root/rancher2git.jar

WORKDIR /root

CMD while true; do \
     java -jar /root/rancher2git.jar \
       ${RANCHER_APIV2_URL} ${RANCHER_API_USER} ${RANCHER_API_PASS} \
       ${GIT_REPO_URL} ${GIT_REPO_USER} ${GIT_REPO_PASS}; \
     sleep ${UPDATE_EVERY}; \
    done