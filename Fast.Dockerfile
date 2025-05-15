# Building HeadlessMc on Linux ARM64 in Github Actions is very slow.
# This Dockerfile assumes that the HeadlessMc launcher has been build.

# the image currently can't run non-headlessly which is ok, but maybe get an image with libX11?
# libX11.so.6: cannot open shared object file: No such file or directory
FROM eclipse-temurin:21-jdk-noble AS build

COPY . /headlessmc
WORKDIR /headlessmc

FROM eclipse-temurin:8-jre-noble AS java8
FROM eclipse-temurin:17-jre-noble AS java17
FROM eclipse-temurin:21-jre-noble

COPY --from=java8 /opt/java/openjdk /opt/java/java8
COPY --from=java17 /opt/java/openjdk /opt/java/java17

COPY --from=build /headlessmc/headlessmc-scripts/HeadlessMC /headlessmc/HeadlessMC
COPY --from=build /headlessmc/headlessmc-scripts/version-independent /headlessmc
COPY --from=build /headlessmc/headlessmc-launcher-wrapper.jar /headlessmc

WORKDIR /headlessmc
# add the scripts directory to the path. this allows us to just execute hmc ... comfortably without any ./
ENV PATH="/headlessmc:${PATH}"
RUN chmod +x hmc

ENTRYPOINT ["/bin/bash"]
