FROM gitpod/workspace-full

USER gitpod

# Instala Java 21 e Maven via SDKMAN
RUN sdk install java 21.0.2-tem && \
    sdk default java 21.0.2-tem && \
    sdk install maven 3.9.6 && \
    sdk default maven 3.9.6
