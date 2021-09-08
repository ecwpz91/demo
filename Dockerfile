# Stage 1: Build the application first using Maven
# FROM registry.access.redhat.com/ubi8/openjdk-11@sha256:76d357448c817d62aee4dcbbec20ac4c64211448158f0256b4d7b05a6646f706 as builder
# WORKDIR /tmp/src
# USER root
# COPY . /tmp/src
# RUN chown -R 185:0 /tmp/src
# USER 185
# RUN mvn clean install

FROM docker.io/maven@sha256:21eab4eb808f517bbb38d25e7529d98a5b4cb88e49c73e5b40c9d30f1c039c9d as builder
WORKDIR /app
COPY . .
RUN mvn clean install

# Stage 2: Inject the JAR file into a new container to keep the file small
# FROM registry.access.redhat.com/ubi8/openjdk-11@sha256:76d357448c817d62aee4dcbbec20ac4c64211448158f0256b4d7b05a6646f706
# COPY --from=builder /tmp/src/target/mrp-0.0.1.jar /deployments/root.jar
# ENV JAVA_ARGS /deployments/root.jar
# CMD java -jar $JAVA_ARGS

FROM docker.io/openjdk@sha256:e1fc4eea0cd62a04f5041a6e09a2289de93d149d9ca28a7c2616610fa626897c
WORKDIR /app
COPY --from=builder /app/target/mrp-0.0.1.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-c"]
CMD ["java -jar app.jar"]