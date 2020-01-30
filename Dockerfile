ARG SERVICE_NAME
FROM openjdk:8
VOLUME /tmp
RUN echo $(ls -l /tmp)
ADD pkgExtract.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]