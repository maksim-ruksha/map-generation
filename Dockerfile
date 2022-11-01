FROM openjdk:18
COPY ./ ./
CMD ["java", "-jar", "target/MapGeneration.jar"]