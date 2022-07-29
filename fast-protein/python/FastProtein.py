from Bio import SeqIO
from Bio.SeqUtils.ProtParam import ProteinAnalysis
from pathlib import Path
import re
import sys


class Protein:
    id = 0
    header = ''
    sequence = ''
    erretDomains = []
    nGlycDomains = []

    def __init__(self, id, header, sequence, erretDomains, nGlycDomains):
        self.id = id
        self.header = header
        self.sequence = sequence
        self.erretDomains = erretDomains
        self.nGlycDomains = nGlycDomains

    def addErretDomain(self, group, span):
        self.erretDomains.append(str(group)+str(span).replace('(', '[').replace(')',']'))
    def addNGlycDomain(self, group, span):
        self.nGlycDomains.append(str(group)+str(span).replace('(', '[').replace(')',']'))
    
class Main:
    
        
    def main():
        def showAbout():
            print('#Fast-protein Software 1.0')
            print('#Developed by Renato Simões - renato.simoes@ifsc.edu.br')
            print('#Protein information software')
            print('#')
            print('#Obs:')
            print('#Pattern ER Retention [KRHQSA][DENQ][E][L] based on PROSITE-PS00014 https://prosite.expasy.org/')
            print('#Pattern N-Glyc [N][Xaa(except P)][ST] Xaa = any aminoacid')
            print('')

        def showHelp():
            
            print("Fast-protein commands manual")
            print()
            print("USAGE")
            print("python3 FastProtein.py [-h] [-t <value>] [-out <output_file>]")
            print("")
            print("DESCRIPTION")
            print("\tFast-protein 1.0")
            print("")
            print("ARGUMENTS\n"
                    + "\t*<fastaFile> - Input FASTA file (required as 1st argument)\n"
                    + "\t-h (optional)\n"
                    + "\t\tPrint USAGE, DESCRIPTION and ARGUMENTS; ignore all other parameters\n"
                    + "\t-v or -version (optional)\n"
                    + "\t\tPrint version number;  ignore all other arguments\n"
                    + "\t-t <options> (optional)\n"
                    + "\t\tType of output format options:\n"
                    + "\t\t\tcsv - Comma separated values,\n"
                    + "\t\t\ttsv - Tab separated values,\n"
                    + "\t\t\ttxt - Formatted output (default)\n"
                    + "\t-out <output_file> (optional) \n"
                    + "\t\tOutput file name - if not informed, the output will be displayed only in the console."
            )
            sys.exit()

        if len(sys.argv[1:])==0: 
            print('Choose a file to process. Please check the help (arg -h) bellow')
            showHelp()
            sys.exit()
        if '-v' in sys.argv: 
            print('FastProtein - version 1.0')
            sys.exit()
        if '-h' in sys.argv: showHelp()
        
        ext = 'txt'
        sep = ''
        if '-t' in sys.argv:
            try:
                type = sys.argv[sys.argv.index('-t')+1]
                if type == 'csv':
                    ext='csv'
                    sep = ';'
                elif type == 'tsv':
                    ext='tsv'
                    sep = '\t'
                elif type != 'txt':
                    print('Invalid option -t = ',type)
                    raise Exception() 
            except:
                print('Error: invalid option -t, please check the help bellow')
                print()
                showHelp()
                sys.exit() 
            print('Selecionou tipo', type)

        args = sys.argv[1:]
        
        path_to_file = args[0]
        path = Path(path_to_file)
        
        out_file = ''

        if path.is_file():
            out_file= path_to_file+'.'+ext
            if '-out' in sys.argv:
                try:
                    out_file = sys.argv[sys.argv.index('-out')+1]
                except Exception as e:
                    print('Error: invalid option -o, please check the help bellow')
                    showHelp()
                    sys.exit()
        else:
            print('Incorrect file: ' + path_to_file)
            print('')
            sys.exit()


        patternErret = '[KRHQSA][DENQ][E][L]'
        patternNglyc = '[N][ARNDBCEQZGHILKMFSTWYV][ST]'

        proteinsFasta = SeqIO.parse(args[0], "fasta")
        
        proteins = []
        for prt in proteinsFasta:

            #Find for ERRET domain
            res_erret = re.finditer(patternErret, str(prt.seq), flags=re.IGNORECASE)
            er_domains = []
            for erret in res_erret:
                er_domains.append(str(erret.group())+str(erret.span()).replace('(', '[').replace(')',']'))  
            #Find for NGlyc domain    
            res_nglyc = re.finditer(patternNglyc, str(prt.seq), flags=re.IGNORECASE)
            nglyc_domains = []
            for nglyc in res_nglyc:
                nglyc_domains.append(str(nglyc.group())+str(nglyc.span()).replace('(', '[').replace(')',']'))
                #protein.addNGlycDomain(nglyc.group(), nglyc.span())
            
            
            protein = Protein(prt.id, prt.description, str(prt.seq), er_domains, nglyc_domains)            
            proteins.append(protein);
        
        header = ['ID', 'Length', 'kDa', 'Isoeletric Point', 'Hydropathy', 'ERRet Total', 'ERRet domains', 'NGlyc Total', 'NGlyc domains', 'Header', 'Sequence']
        
        results = []
        for prot in proteins:
            proteinProp = ProteinAnalysis(prot.sequence)
            line = [
                prot.id, 
                str(len(prot.sequence)), 
                '{:.2f}'.format(proteinProp.molecular_weight()/1000),
                '{:.2f}'.format(proteinProp.isoelectric_point()),
                '{:.2f}'.format(proteinProp.aromaticity()), 
                str(len(prot.erretDomains)),
                ','.join(prot.erretDomains),
                str(len(prot.nGlycDomains)),
                ','.join(prot.nGlycDomains),
                prot.header,
                prot.sequence]
            results.append(line)

        report = ''

        if ext == 'txt':
            #Calculating the max column length - this is use to arrange a formatted txt file
            col_length = []
            for column in header:
                col_length.append(len(column))

            for ln in results:
                for i in range(len(ln)):
                    if(len(ln[i])>col_length[i]):
                        col_length[i]=len(ln[i])

            results.insert(0,header)
            for idx, res in enumerate(results):
                if(idx > 0):
                    report += '\n'
                report += str(res[0]).ljust(col_length[0],' ')+' '
                report += str(res[1]).rjust(col_length[1],' ')+' '
                report += str(res[2]).rjust(col_length[2],' ')+' '
                report += str(res[3]).rjust(col_length[3],' ')+' '
                report += str(res[4]).rjust(col_length[4],' ')+' '
                report += str(res[5]).rjust(col_length[5],' ')+' '
                report += str(res[6]).ljust(col_length[6],' ')+' '
                report += str(res[7]).rjust(col_length[7],' ')+' '
                report += str(res[8]).ljust(col_length[8],' ')+' '
                report += str(res[9]).ljust(col_length[9],' ')+' '
                report += str(res[10]).ljust(col_length[10],' ')

        else:
            report = sep.join(header)
            for ln in results:
                report += '\n'
                report += sep.join(ln)
        
        showAbout()
        print(report)

        output = open(out_file, "w")
        output.write(report)
        output.close()
    if __name__ == "__main__":
        main()
    
    

        