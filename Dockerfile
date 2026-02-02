FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copia arquivos do Maven
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Baixa dependências (cache layer)
RUN ./mvnw dependency:go-offline -B

# Copia código fonte
COPY src ./src

# Compila o projeto
RUN ./mvnw clean package -DskipTests

# Imagem final (mais leve)
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copia o JAR compilado
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]