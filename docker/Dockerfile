FROM alpine:3.19.1
RUN mkdir -p /opt/app
RUN apk add --no-cache openjdk17-jre
WORKDIR /opt/app
ENTRYPOINT ["/opt/app/start.sh"]