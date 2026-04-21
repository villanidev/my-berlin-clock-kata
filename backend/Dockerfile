# ─── Stage 1: Build ─────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /workspace

COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

COPY src/ src/
RUN mvn -q -DskipTests package

# Build a minimized JRE with jlink.
# Note: This uses a conservative module list that works for typical Spring Boot apps.
RUN jlink \
    --module-path "$JAVA_HOME/jmods" \
    --add-modules java.base,java.logging,java.xml,java.naming,java.management,java.security.jgss,java.instrument,java.desktop,java.sql,java.transaction.xa,java.net.http,java.rmi,jdk.crypto.ec,jdk.unsupported,jdk.attach,jdk.jcmd,jdk.management,jdk.management.agent \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress=2 \
    --output /jre

# ─── Stage 2: Runtime ───────────────────────────────────────────────────────
FROM alpine:3.20
WORKDIR /app

RUN addgroup -S app && adduser -S app -G app

COPY --from=build /jre /opt/java
COPY --from=build /workspace/target/*.jar app.jar

# Define JVM Options for a 512MB Container
ENV PATH="/opt/java/bin:${PATH}"
ENV JAVA_TOOL_OPTIONS="\
-XX:+UseContainerSupport \
-XX:ActiveProcessorCount=1 \
-XX:MaxRAMPercentage=70.0 \
-XX:InitialRAMPercentage=70.0 \
-Xss256k \
-XX:MaxMetaspaceSize=96m \
-XX:ReservedCodeCacheSize=32m \
-XX:MaxDirectMemorySize=32m \
-XX:+UseG1GC \
-XX:ParallelGCThreads=1 \
-XX:ConcGCThreads=1 \
-XX:NativeMemoryTracking=summary \
-XX:+ExitOnOutOfMemoryError \
-Djava.awt.headless=true \
-Dfile.encoding=UTF-8"

EXPOSE 8080
USER app
ENTRYPOINT ["java", "-jar", "app.jar"]