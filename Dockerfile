FROM eclipse-temurin:21-jdk


# Copy demo_app to the image
COPY target/demo-0.0.1-SNAPSHOT.jar /usr/local/bin/demo.jar

# Set the default command
CMD ["java", "-jar", "/usr/local/bin/demo.jar"]