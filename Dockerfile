FROM ubuntu:latest
LABEL authors="ilkay"

ENTRYPOINT ["top", "-b"]