FROM adoptopenjdk:11-jre-hotspot
RUN mkdir -p /smartticket/app
COPY /target/smartticket-0.1.jar smartticket/app
EXPOSE 8090
CMD ["java", "-jar", "/smartticket/app/smartticket-0.1.jar"]