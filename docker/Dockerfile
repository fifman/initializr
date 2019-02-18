FROM dockerproxy.msplat.io/openjdk:8
# ENV port=8080
WORKDIR /app
# VOLUME ["/var/log/"]
EXPOSE 8080
ADD ./build/libs/initializr.jar /app/initializr.jar
CMD ["sh", "-c", "java -jar /app/initializr.jar"]
