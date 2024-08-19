# the image currently can't run non-headlessly which is ok, but maybe get an image with libX11?
# libX11.so.6: cannot open shared object file: No such file or directory
FROM eclipse-temurin:21-jdk-noble AS build

COPY . /headlessmc
WORKDIR /headlessmc

RUN chmod +x ./gradlew
RUN ./gradlew headlessmc-launcher-wrapper:build

RUN rm headlessmc-launcher-wrapper/build/libs/headlessmc-launcher-*-dev.jar
RUN rm headlessmc-launcher-wrapper/build/libs/headlessmc-launcher-*-javadoc.jar
RUN rm headlessmc-launcher-wrapper/build/libs/headlessmc-launcher-*-sources.jar

FROM eclipse-temurin:8-jre-noble AS java8
FROM eclipse-temurin:17-jre-noble AS java17
FROM eclipse-temurin:21-jre-noble

COPY --from=java8 /opt/java/openjdk /opt/java/java8
COPY --from=java17 /opt/java/openjdk /opt/java/java17

COPY --from=build /headlessmc/headlessmc-scripts /headlessmc
COPY --from=build /headlessmc/headlessmc-launcher-wrapper/build/libs/headlessmc-launcher-*.jar /headlessmc

WORKDIR /headlessmc
# add the scripts directory to the path. this allows us to just execute hmc ... comfortably without any ./
ENV PATH="/headlessmc:${PATH}"
RUN chmod +x hmc

ENTRYPOINT ["/bin/bash"]
