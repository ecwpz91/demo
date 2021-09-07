# Build the application first using Maven
FROM registry.access.redhat.com/ubi8/openjdk-11@sha256:76d357448c817d62aee4dcbbec20ac4c64211448158f0256b4d7b05a6646f706 as builder
WORKDIR /tmp/src
USER root
COPY . /tmp/src
RUN chown -R 185:0 /tmp/src
USER 185
RUN mvn clean install

# Inject the JAR file into a new container to keep the file small
FROM registry.access.redhat.com/ubi8/openjdk-11@sha256:76d357448c817d62aee4dcbbec20ac4c64211448158f0256b4d7b05a6646f706
COPY --from=builder /tmp/src/target/mrp-0.0.1.jar /deployments/root.jar
ENV JAVA_ARGS /deployments/root.jar
CMD java -jar $JAVA_ARGS