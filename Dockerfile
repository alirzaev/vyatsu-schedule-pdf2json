FROM maven:3-jdk-11-slim as builder


WORKDIR /usr/src/project/
COPY . /usr/src/project/

RUN ["mvn", "-DskipTests", "package"]


FROM openjdk:alpine

EXPOSE 80

WORKDIR /usr/src/project/
COPY --from=builder /usr/src/project/target/pdf2json-jar-with-dependencies.jar pdf2json.jar

CMD ["java", "-jar", "pdf2json.jar"]
