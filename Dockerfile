FROM openjdk:8-jre-alpine

ENV RANCHER_APIV2_URL = ""\
    RANCHER_API_USER = "" \
    RANCHER_API_PASS = "" \
    GIT_REPO_URL = "" \
    GIT_REPO_USER = "" \
    GIT_REPO_PASS = "" \
    UPDATE_EVERY = "5m"

COPY target/rancher2git-jar-with-dependencies.jar /root/rancher2git.jar
COPY scripts/start.sh /root/start.sh

WORKDIR /root

CMD ./start.sh