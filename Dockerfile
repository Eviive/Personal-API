FROM eclipse-temurin:17-jdk-alpine as build

WORKDIR /workspace/app

COPY gradlew *.gradle ./
COPY src src

RUN ./gradlew bootJar

RUN mkdir -p build/dependency && (cd build/dependency; jar -xf ../libs/*.jar)

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

ARG DEPENDENCY=/workspace/app/build/dependency

COPY --from=build ${DEPENDENCY}/BOOT-INF/lib     ./lib
COPY --from=build ${DEPENDENCY}/META-INF         ./META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes ./

CMD java -Dspring.profiles.active=prod -cp .:lib/* com.eviive.personalapi.PersonalApiApplication
