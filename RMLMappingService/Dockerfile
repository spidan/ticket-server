FROM adoptopenjdk:11-jre-hotspot
RUN mkdir -p /rmlmapper/app
COPY /target/RMLMappingService.jar rmlmapper/app
EXPOSE 8801
CMD ["java", "-jar", "/rmlmapper/app/RMLMappingService.jar"]