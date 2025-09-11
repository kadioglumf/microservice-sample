FROM maven:3.9.6-eclipse-temurin-21 as build
ARG FOLDER=latest

WORKDIR /opt/app

COPY pom.xml .
COPY ${FOLDER} /opt/app/${FOLDER}

WORKDIR /opt/app/${FOLDER}

RUN mvn clean compile package --batch-mode -Dmaven.test.skip=true -U
RUN mv target/$(mvn -q -Dexec.executable=echo -Dexec.args='${project.artifactId}-${project.version}.jar' --non-recursive exec:exec 2>/dev/null) app.jar

FROM openjdk:21
ARG FOLDER=latest

RUN mkdir -p /opt/app/${FOLDER} && \
    groupadd -r app -g 1000 && \
    useradd -u 1000 -r -g app -m -d /opt/app/${FOLDER} -s /sbin/nologin -c "App user" app && \
    chmod 755 /opt/app/${FOLDER}

COPY --from=build /opt/app/${FOLDER}/app.jar /opt/app/${FOLDER}/app.jar
RUN chown app:app /opt/app/${FOLDER}/app.jar

ENV FOLDER=${FOLDER}

RUN rm /etc/localtime; ln -s /usr/share/zoneinfo/Europe/Istanbul /etc/localtime

USER app
WORKDIR /opt/app/${FOLDER}

ENTRYPOINT ["/bin/bash", "-c", "java -Dspring.profiles.active=docker -Dfile.encoding=UTF-8 -jar /opt/app/${FOLDER}/app.jar"]