# FastProtein 1.0
### _A fast and easy way to know more about your proteins :)_

### Informations that you will find here:
- ID: protein id from your fasta
- Length: the length of the sequence protein
- kDa: molecular mass in kilodaltons
- Isoeletric Point: isoeletric point of the full sequence
- Hydropathy: hydropathy of the full sequence
- Aromaticity: aromaticity of the full sequence
- Endoplasmatic Reticulum Retention Total: total domains found with E.R retantion domain
- Endoplasmatic Reticulum Retention domains: E.R retantion domains found with peptide and position
- N-Glycosylation Total: total N-Glyc domains found
- N-Glycosylation domains: NGlyc domains peptide and position
- Header: protein header
- Sequence: protein sequence

| Plugin | Domain | About |
| ------ | ------ | ------ |
| E.R Retention | [KRHQSA][DENQ][E][L] | https://prosite.expasy.org/PDOC00014 |
| N-Glyc | [N][Xaa(except P)][ST] where Xaa = any aminoacid | https://prosite.expasy.org/PDOC00001|

## Input options
| Type | Description |
| ------ | ------ |
| FASTA | protein file (required)|


## Output options
| Type | Description | Style |
| ------ | ------ | ------ |
| tsv | tab formatted | column |
| csv | csv file using semicolon by default separator | column |
| txt | formatted file with white space (default) | column
| sep | individual protein   | plain |
Check the [source code](https://github.com/simoesrenato/bioinfo/tree/master/fast-protein) 
Developed by Dr. Renato Simoes - renato.simoes@ifsc.edu.br

## License

MIT
