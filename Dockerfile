FROM gradle:7.3.2-jdk11-alpine
COPY --chown=gradle:gradle . /mud
WORKDIR /mud
RUN gradle build --no-daemon
RUN gradle run --args='migrate'
EXPOSE 9999
ENTRYPOINT ["gradle", "run"]