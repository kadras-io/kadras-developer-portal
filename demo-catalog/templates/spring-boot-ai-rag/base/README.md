# ${{ values.name }}

![Build](https://github.com/${{ values.repoUrl.owner }}/${{ values.repoUrl.repo }}/actions/workflows/commit-stage.yml/badge.svg)
[![The SLSA Level 3 badge](https://slsa.dev/images/gh-badge-level3.svg)](https://slsa.dev/spec/v1.0/levels)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=${{ values.repoUrl.owner }}_${{ values.repoUrl.repo }}&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=${{ values.repoUrl.owner }}_${{ values.repoUrl.repo }})
[![The Apache 2.0 license badge](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

${{ values.description }}

## 🚀&nbsp; Running the application

```shell
./gradlew bootTestRun
```

## 💻&nbsp; Calling the application

LLM-powered application with RAG capabilities.
This example uses [httpie](https://httpie.io) to send HTTP requests.

```shell
http --raw "Who is the lead singer?" :8080/chat -b --pretty none
```

```shell
http --raw "What instrument does Clara play" :8080/chat -b --pretty none
```

## 🖊️&nbsp; License

This project is licensed under the **Apache License 2.0**. See [LICENSE](LICENSE) for more information.
