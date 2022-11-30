FROM debian

LABEL app="EpiBuilder"
LABEL version="2.0"
LABEL description="Bioinformatic tools"

#VOLUME /epibuilder/
#WORKDIR /epibuilder/

#install python and pip package manager
RUN apt update
RUN apt install --no-install-recommends python3 python3-pip git -y
#install java jre 
RUN apt install openjdk-11-jre -y
#install NCBI-Blast (for blastp and makeblastdb)
RUN apt install ncbi-blast+ -y
RUN apt clean -y
RUN rm -rf /var/lib/apt/lists/*

#install all requirements
COPY requirements.txt .
RUN pip3 install --no-cache-dir -r requirements.txt

#create alias for python executable to be python3 executable
RUN ln -s $(which python3) /usr/bin/python

#copy CLI script and model esm1b model into image
COPY bepipred3_CLI.py .
COPY models.tar.gz .
COPY esm_encodings.tar.gz .
RUN tar -xzf models.tar.gz
RUN tar -xzf esm_encodings.tar.gz
RUN rm models.tar.gz
RUN rm esm_encodings.tar.gz
RUN mkdir epibuilder/
COPY example_antigens.fasta .
COPY raw_output.csv .
COPY epibuilder-2.jar epibuilder/
CMD ["bash"]
