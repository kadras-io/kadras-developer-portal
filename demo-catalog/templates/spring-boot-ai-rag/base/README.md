# ${{ values.name }}

${{ values.description }}

## Running the application

```shell
./gradlew bootTestRun
```

## Calling the application

LLM-powered application with RAG capabilities.
This example uses [httpie](https://httpie.io) to send HTTP requests.

```shell
http --raw "Who is the lead singer?" :8080/chat
```

```shell
http --raw "What instrument does Clara play" :8080/chat
```
