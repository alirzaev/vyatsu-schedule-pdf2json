FROM maven:3.6.0-jdk-8-alpine

EXPOSE 80

WORKDIR /usr/src/project/
COPY . /usr/src/project/

RUN ["mvn", "-DskipTests", "package"]

CMD ["java", "-jar", "target/vyatsu_pdf_parser-jar-with-dependencies.jar"]
