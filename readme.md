# Ranch2Git

[![](https://images.microbadger.com/badges/image/gmentsik/ranch2git.svg)](https://microbadger.com/images/gmentsik/ranch2git "Get your own image badge on microbadger.com")
[![Docker Repository on Quay](https://quay.io/repository/gmentsik/ranch2git/status "Docker Repository on Quay")](https://quay.io/repository/gmentsik/ranch2git)
[![Codeship](https://img.shields.io/codeship/c5be5190-cedd-0134-3391-22c62aa246c8.svg?style=flat-square)](https://app.codeship.com/projects/c5be5190-cedd-0134-3391-22c62aa246c8)
[![Docker Automated buil](https://img.shields.io/docker/automated/gmentsik/ranch2git.svg?style=flat-square)](https://hub.docker.com/r/gmentsik/ranch2git/)
[![Docker Pulls](https://img.shields.io/docker/pulls/gmentsik/ranch2git.svg?style=flat-square)](https://hub.docker.com/r/gmentsik/ranch2git)
[![Docker Stars](https://img.shields.io/docker/stars/gmentsik/ranch2git.svg?style=flat-square)](https://hub.docker.com/r/gmentsik/ranch2git)

- [Introduction](#introduction)
- [Getting started](#getting-started)
  - [Installation](#installation)
  - [Quickstart](#quickstart)
  - [Environment Variables](#environment-variables)
  - [Labels (Rancher API V1)](#labels)
- [To-Do](#todo)
- [Contributing](#contributing)
- [Issues](#issues)

# Introduction

Ranch2Git keeps track of your [Rancher](http://rancher.com/) stacks.
It is supposed to be deployed as a [Dockerimage](https://www.docker.com/).
As such, it can be easily deployed with Rancher, making it really easy to use.

# Getting started

## Installation

Automated builds of the image are available on [Dockerhub](https://hub.docker.com/r/gmentsik/ranch2git) and is the recommended method of installation.

```bash
docker pull gmentsik/ranch2git
```

_Alternatively you can build the image yourself._

```bash
docker build -t ranch2git github.com/gmentsik/ranch2git
```

## Quickstart
The recommended way of running ranch2git is to deploy it directly as service in Rancher.

1. Create a new Git-Repository on your host. This can be any Git-Repo on any host. 
_Please note that currently only HTTP(S) is supported, since the main target is Github and GitLab_

2. Make sure you have access to your repo with a username + password combination. (HTTP Simple Auth)

3. Add a new [Account API Key in Rancher](http://docs.rancher.com/rancher/v1.4/en/api/v2-beta/api-keys/)
_Rancher Web UI -> API -> Add Account API Key_

5. You can copy&paste this docker-compose file in a new rancher-stack.

6. Replace the `RANCHER_API_USER` and `RANCHER_API_PASS` environmental variables with the generated User and Password.


```yaml
version: '2'
services:
  ranch2github:
    image: gmentsik/ranch2git:latest
    environment:
      RANCHER_APIV2_URL: http://rancher-url.com/v2-beta
      RANCHER_API_USER: api-user
      RANCHER_API_PASS: api-user-password
      GIT_REPO_URL: https://github.com/username/repo.git
      GIT_REPO_USER: username
      GIT_REPO_PASS: password
      UPDATE_EVERY: 5m
    stdin_open: true
    network_mode: host
    tty: true
    labels:
      io.rancher.container.pull_image: always
```

*OR you can use the v1 API for older rancher versions

```yaml
version: '2'
services:
  ranch2github:
    image: gmentsik/ranch2git
    environment:
      GIT_REPO_URL: https://github.com/username/repo.git
      GIT_REPO_USER: git-user
      GIT_REPO_PASS: git-password
      UPDATE_EVERY: 1h
    labels:
      io.rancher.container.create_agent: "true"
      io.rancher.container.agent.role: "environment"
    stdin_open: true
    network_mode: host
    tty: true
```

*Alternatively, you can use the sample [docker-compose.yml](docker-compose.yml) file to start the container using [Docker Compose](https://docs.docker.com/compose/)*

## Environment Variables


| Variable          | Default Value   | Description                                                                     | Examples                                          |
| ----------------- | --------------- | --------------------------------------------------------------------------------| ------------------------------------------------- |
| RANCHER_APIV2_URL | ""              | The full URL to your Rancher API V2                                             | `http://rancher.company1.com/v2-beta`             |
| RANCHER_API_USER  | ""              | The generated rancher API User                                                  | `2CE23B40FA3761K91C66`                            |
| RANCHER_API_PASS  | ""              | The generated password of your rancher API User                                 | `A2nO8r43jAMrXMQeJd5VpFvEYHKzYgKPSuuTt7ct`        |
| GIT_REPO_URL      | ""              | Full http URL to your Git-Repository                                            | `https://github.com/company1/rancher-conf.git`    |
| GIT_REPO_USER     | ""              | Your Git-Username                                                               | `company1`                                        |
| GIT_REPO_PASS     | ""              | Your Git Password                                                               | `soVeryStrongPasswordCannotBeHacked`              |
| UPDATE_EVERY      | "5m"            | The Frequency of the script execution.                                          | `1m`, `10s`, `2h`                                 |

## Labels (Rancher API V1)
You can use the v1 API by adding two labels to your service

      io.rancher.container.create_agent=true
      io.rancher.container.agent.role=environment
    

This will provide the three environment variables required to access the rancher api.

## Shell Access

For debugging and maintenance purposes you may want access the containers shell. If you are using Docker version `1.3.0` or higher you can access a running containers shell by starting `bash` using `docker exec`:

```bash
docker exec -it ranch2git bash
```

everything happens inside the `/root` folder.

# Contributing

If you find this image useful here's how you can help:

- Send a pull request with your awesome features and bug fixes, to the dev branch.
- Help users resolve their [issues](../../issues?q=is%3Aopen+is%3Aissue).

# Issues

Before reporting your issue please try updating Docker to the latest version and check if it resolves the issue. Refer to the Docker [installation guide](https://docs.docker.com/installation) for instructions.

SELinux users should try disabling SELinux using the command `setenforce 0` to see if it resolves the issue.

If the above recommendations do not help then [report your issue](../../issues/new) along with the following information:

- Output of the `docker version` and `docker info` commands
- The `docker run` command or `docker-compose.yml` used to start the image. Mask out the sensitive bits.
- Please state if you are using [Boot2Docker](http://www.boot2docker.io), [VirtualBox](https://www.virtualbox.org), etc.