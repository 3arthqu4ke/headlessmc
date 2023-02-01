# the image currently can't run non-headlessly which is ok, but maybe get an image with libX11?
# libX11.so.6: cannot open shared object file: No such file or directory

# TODO: download HMC-Specifics?
FROM openjdk:17.0.2-jdk as java17
FROM openjdk:8u332-jdk

COPY --from=java17 /usr/java/openjdk-17 /usr/java/openjdk-17

RUN cp --remove-destination /usr/local/openjdk-8/jre/lib/security/cacerts /usr/java/openjdk-17/lib/security/cacerts

COPY . /headlessmc
WORKDIR /headlessmc

RUN chmod +x ./gradlew
RUN ./gradlew build -Dhmc.jar.dir=headlessmc-scripts

WORKDIR /headlessmc/headlessmc-scripts
# add the scripts directory to the path. this allows us to just execute hmc ... comfortably without any ./
ENV PATH="/headlessmc/headlessmc-scripts:${PATH}"
RUN chmod +x hmc
# TODO: maybe clean up, create a directory which contains nothing but the launcher jar and the hmc file?
