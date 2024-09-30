FROM ubuntu:latest
LABEL authors="anna"

ENTRYPOINT ["top", "-b"]