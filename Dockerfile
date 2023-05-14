FROM eclipse-temurin:17-jdk-alpine as build

WORKDIR /workspace/app

COPY gradlew *.gradle ./
COPY gradle gradle
COPY src src

RUN ./gradlew bootJar

RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/*.jar)

FROM eclipse-temurin:17-jdk-alpine

ARG DEPENDENCY=/workspace/app/build/dependency

COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-Dspring.profiles.active=prod","-cp","app:app/lib/*","com.eviive.personalapi.PersonalApiApplication"]
