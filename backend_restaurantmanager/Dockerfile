FROM maven:3.9.12-eclipse-temurin-21

WORKDIR /app

# Copiamos pom y descargamos dependencias primero (cache eficiente)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el código
COPY src ./src

EXPOSE 8080

CMD ["mvn", "spring-boot:run"]