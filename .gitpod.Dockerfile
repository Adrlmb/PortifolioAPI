FROM gitpod/workspace-full

USER gitpod

# Instalar dependências necessárias
RUN sudo apt-get update && \
    sudo apt-get install -y wget ca-certificates

# Baixar e instalar o JDK 21
RUN wget https://github.com/AdoptOpenJDK/openjdk21-binaries/releases/download/jdk-21+35/OpenJDK21U-jdk_x64_linux_hotspot_21_35.tar.gz && \
    sudo tar -xzf OpenJDK21U-jdk_x64_linux_hotspot_21_35.tar.gz -C /opt/ && \
    sudo rm OpenJDK21U-jdk_x64_linux_hotspot_21_35.tar.gz

# Configurar variáveis de ambiente
ENV JAVA_HOME=/opt/jdk-21
ENV PATH=$JAVA_HOME/bin:$PATH
