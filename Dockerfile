FROM openjdk:22-jdk
ADD target/employee_cv-0.0.1-SNAPSHOT.jar app1.jar
ENTRYPOINT [ "java", "-jar","app1.jar" ]