FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

COPY gradle gradle
COPY gradlew .
COPY build.gradle.kts .
COPY settings.gradle.kts .

RUN chmod +x ./gradlew
RUN ./gradlew --version
RUN ./gradlew dependencies --no-daemon || return 0

COPY . .

RUN ./gradlew clean bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]