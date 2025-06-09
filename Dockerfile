FROM maven:3.9.6-eclipse-temurin-21 AS builder
LABEL authors="Vitor Tigre"

WORKDIR /build

COPY . .

RUN mvn clean package -DskipTests -Dcheckstyle.skip=true

FROM openjdk:21-slim

WORKDIR /app

COPY --from=builder /build/target/*.jar /app/app.jar

ENV AZURE_STORAGE_ACCESS_KEY=DefaultEndpointsProtocol/=https\;AccountName/=blobrevest\;AccountKey/=mdkjfOm1SqR7BlmJ3QrNu7S0KbV3t43xvnjKZ0Zfig7hOUP5eYS7JobDiN+BcoD5RVJa10sRTdvx+AStI3ybHQ==\;EndpointSuffix/=core.windows.net
ENV AZURE_STORAGE_ACCOUNT_KEY=mdkjfOm1SqR7BlmJ3QrNu7S0KbV3t43xvnjKZ0Zfig7hOUP5eYS7JobDiN+BcoD5RVJa10sRTdvx+AStI3ybHQ==
ENV AZURE_STORAGE_ACCOUNT_NAME=blobrevest
ENV AZURE_STORAGE_BLOB_ENDPOINT=https://blobrevest.blob.core.windows.net/revest-container
ENV AZURE_STORAGE_CONTAINER_NAME=revest-container
ENV DATABASE_HOST=database
ENV DATABASE_PORT=3306
ENV DATABASE_USER=root
ENV DATABASE_PASSWORD=urubu123
ENV DATABASE_NAME=revest
ENV DATABASE_PRIVATE_DNS=jdbc:mysql://10.0.2.69:3306/revest

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]