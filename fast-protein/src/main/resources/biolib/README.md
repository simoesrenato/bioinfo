# FastProtein 1.0
### _A fast and easy way to know more about your proteins :)_

### Informations that you will find here:
- ID: protein id from your fasta
- Length: the length of the sequence protein
- kDa: molecular mass in kilodaltons
- Isoeletric Point: isoeletric point of the full sequence
- Hydropathy: hydropathy of the full sequence
- Aromaticity: aromaticity of the full sequence
- Subcellular localization prediction ([WoLF PSORT](https://wolfpsort.hgc.jp/))
- Prediction of transmembrane helices in proteins ([TMHMM-2.0c](https://services.healthtech.dtu.dk/service.php?TMHMM-2.0))
- Prediction of Signal Peptides ([SignalP-5](https://services.healthtech.dtu.dk/service.php?SignalP-5.0))
- Endoplasmatic Reticulum Retention Total: total domains found with E.R retantion domain
- Endoplasmatic Reticulum Retention domains: E.R retantion domains found with peptide and position
- N-Glycosylation Total: total N-Glyc domains found
- N-Glycosylation domains: NGlyc domains peptide and position
- Header: protein header
- Gene ontology, Panther and Pfam: ([InterproScan5](https://www.ebi.ac.uk/interpro/)) 
- Sequence: protein sequence

| Domain | Reference |
| ------ | ------ |
| E.R Retention |  https://prosite.expasy.org/PDOC00014 |
| N-Glyc |  https://prosite.expasy.org/PDOC00001|

## Input options
| Type | Description |
| ------ | ------ |
| FASTA | protein file (required)|


## Output options
| Type | Description | Style | Example |
| ------ | ------ | ------ | ------ |
| tsv | tab formatted | column | [Link](https://github.com/simoesrenato/bioinfo/blob/master/fast-protein/python/test1.fasta.tsv) |
| csv | csv file using semicolon by default separator | column | [Link](https://github.com/simoesrenato/bioinfo/blob/master/fast-protein/python/test1.fasta.csv) |
| txt | formatted file with white space (default) | column | [Link](https://github.com/simoesrenato/bioinfo/blob/master/fast-protein/python/test1.fasta.txt) |
| sep | individual protein   | plain | [Link](https://github.com/simoesrenato/bioinfo/blob/master/fast-protein/python/test1.fasta.sep) |


This software was developed in Java ([BioJava](https://biojava.org/)), Python and Perl

Check the [source code](https://github.com/simoesrenato/bioinfo/tree/master/fast-protein) 
Developed by PhD. Renato Simoes - renato.simoes@ifsc.edu.br

## License

MIT
