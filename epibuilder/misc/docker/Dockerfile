FROM debian:11

LABEL app="EpiBuilder"
LABEL version="2.0"
LABEL description="Bioinformatic tools"

#VOLUME /epibuilder/
#WORKDIR /epibuilder/

RUN apt-get update
RUN apt install --no-install-recommends python3 python3-pip libgomp1 git unzip nano -y

RUN apt clean -y
RUN rm -rf /var/lib/apt/lists/*

COPY blast/blastp /usr/bin/
COPY blast/makeblastdb /usr/bin/
COPY openjdk-21.0.1_linux-x64_bin.tar.gz /tmp/

RUN tar -xzf /tmp/openjdk-21.0.1_linux-x64_bin.tar.gz -C /usr/local/
ENV PATH="/usr/local/jdk-21.0.1/bin:${PATH}"

#create alias for python executable to be python3 executable
RUN ln -s $(which python3) /usr/bin/python

#install all requirements

COPY bepipred3.zip /
RUN unzip bepipred3.zip -d /

RUN pip3 install --no-cache-dir -r requirements.txt

#copy CLI script and model esm1b model into image

COPY blast/blastp /usr/bin/
COPY blast/makeblastdb /usr/bin/
COPY models.tar.gz .
COPY esm_encodings.tar.gz .
RUN tar -xzf models.tar.gz
RUN tar -xzf esm_encodings.tar.gz
RUN rm models.tar.gz
RUN rm esm_encodings.tar.gz
RUN mkdir epibuilder/
COPY raw_output.csv .
COPY epibuilder-2.jar .
CMD ["bash"]
