# build
FROM maven:4.0.0-rc-5-amazoncorretto-25-debian-trixie as build
WORKDIR /build

# Copia todos os arquivos da pasta atual para a psata build
COPY . .

WORKDIR /build/LibraryAPI

RUN mvn clean package -DskipTests


# run
FROM amazoncorretto:25.0.1
WORKDIR /app

COPY --from=build /build/LibraryAPI/target/*-SNAPSHOT.jar /app/app.jar

EXPOSE 8080
EXPOSE 9090

ENV DB_URL=''
ENV DB_USER=''
ENV JWT_SECRET='my-secret-token'

ENV SPRING_PROFILES_ACTIVE='production'
ENV TZ='America/Sao_Paulo'

ENTRYPOINT ["java","-jar","/app/app.jar"]