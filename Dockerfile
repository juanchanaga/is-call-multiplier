FROM eclipse-temurin:17-jdk-alpine
ENV TZ="America/New_York"
WORKDIR /app
ARG gradle_build
ARG spring_profiles
ARG spring_config_import
COPY build/libs/$gradle_build.jar /app/app.jar
ENV SPRING_PROFILES_ACTIVE=$spring_profiles
ENV SPRING_CONFIG_IMPORT=$spring_config_import
ENTRYPOINT ["java","-jar","/app/app.jar"]
EXPOSE 80 8000