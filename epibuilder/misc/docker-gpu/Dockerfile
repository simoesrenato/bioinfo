#FROM debian:11
FROM nvidia/cuda:11.3.1-runtime-ubuntu20.04

LABEL app="EpiBuilder"
LABEL version="2.0"
LABEL description="Bioinformatic tools"

#Keys to make nvidias repos work 
#RUN rm /etc/apt/sources.list.d/cuda.list && \
#    apt-key del 7fa2af80 && \
#    apt-key adv --fetch-keys https://developer.download.nvidia.com/compute/cuda/repos/ubuntu2004/x86_64/7fa2af80.pub

#VOLUME /epibuilder/
#WORKDIR /epibuilder/

#install python and pip package manager
RUN apt update
RUN apt install --no-install-recommends python3 python3-pip git curl libgomp1 -y
RUN apt clean
RUN rm -rf /var/lib/apt/lists/*

#create alias for python executable to be python3 executable
RUN ln -s $(which python3) /usr/bin/python

#INSTALL BLAST
COPY blast/blastp /usr/bin/
COPY blast/makeblastdb /usr/bin/

#INSTALL JAVA
COPY openjdk-21.0.1_linux-x64_bin.tar.gz /tmp/
RUN tar -xzf /tmp/openjdk-21.0.1_linux-x64_bin.tar.gz -C /usr/local/
ENV PATH="/usr/local/jdk-21.0.1/bin:${PATH}"


#install all requirements

COPY BepiPred3/ /

RUN pip3 install --no-cache-dir -r requirements.txt

#copy CLI script and model esm1b model into image

COPY esm_encodings.tar.gz .
RUN tar -xzf esm_encodings.tar.gz
RUN rm esm_encodings.tar.gz

RUN mkdir /models/
RUN curl https://dl.fbaipublicfiles.com/fair-esm/regression/esm2_t33_650M_UR50D-contact-regression.pt -o /models/esm2_t33_650M_UR50D-contact-regression.pt
RUN curl https://dl.fbaipublicfiles.com/fair-esm/models/esm2_t33_650M_UR50D.pt -o /models/esm2_t33_650M_UR50D.pt

COPY raw_output.csv .
COPY input.fasta .
COPY epibuilder-2.jar .
CMD ["bash"]
