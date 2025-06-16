FROM eclipse-temurin:21-jdk

RUN apt-get update && \
    apt-get install -y curl unzip && \
    curl -s "https://get.sdkman.io" | bash && \
    bash -c "source /root/.sdkman/bin/sdkman-init.sh && sdk install maven 3.9.6"

ENV JAVA_HOME=/opt/java/openjdk
ENV PATH=$JAVA_HOME/bin:$PATH
