from Bio import SeqIO
from Bio.SeqUtils.ProtParam import ProteinAnalysis
from pathlib import Path
import argparse
import re
import sys

# PARSER ARGUMENTS -------------------------------------------------------------

parser = argparse.ArgumentParser(
    add_help=False,
    description="""
FastProtein Software 1.0
    """,
    epilog="Do you have any questions? Please contact us at renato.simoes@ifsc.edu.br!",
    formatter_class=argparse.RawTextHelpFormatter,
)

required_args = parser.add_argument_group("required arguments")
optional_args = parser.add_argument_group("custom arguments")

# mandatory arguments
required_args.add_argument(
    "-i",
    "--input",
    "-I" "-INPUT",
    dest="input",
    help="FASTA file to process",
    required=True,
)


# optional arguments
#   no argument: uses default
#   type (default): string
optional_args.add_argument(
    "-t",
    "--type",
    "-T",
    "-TYPE",
    dest="type",
    default="txt",
    choices=["csv", "tsv", "txt"],
    help="Type of output format options:\n"
    + "csv - Comma separated values,\n"
    + "tsv - Tab separated values,\n"
    + "txt - Formatted output (default)\n",
)

optional_args.add_argument(
    "-o",
    "-out",
    "-O",
    "--output",
    "-OUTPUT",
    default="results.out",
    dest="output",
    help="Output file name - if not informed, the output is showed in the console and saved the file <input>.<type>",
)

# custom [--help] argument
optional_args.add_argument(
    "-h",
    "-help",
    "--help",
    action="help",
    default=argparse.SUPPRESS,
    help="FastProtein 1.0 ",
)


class Protein:
    id = 0
    header = ""
    sequence = ""
    erretDomains = []
    nGlycDomains = []

    def __init__(self, id, header, sequence, erretDomains, nGlycDomains):
        self.id = id
        self.header = header
        self.sequence = sequence
        self.erretDomains = erretDomains
        self.nGlycDomains = nGlycDomains

    def addErretDomain(self, group, span):
        self.erretDomains.append(
            str(group) + str(span).replace("(", "[").replace(")", "]")
        )

    def addNGlycDomain(self, group, span):
        self.nGlycDomains.append(
            str(group) + str(span).replace("(", "[").replace(")", "]")
        )


class Main:
    def main():
        def hydrophobicity(sequence):
            # Amino acid scale: Hydropathicity.
            # Author(s): Kyte J., Doolittle R.F.
            # Reference: J. Mol. Biol. 157:105-132(1982).
            # https://web.expasy.org/protscale/pscale/Hphob.Doolittle.html

            scale = {
                "A": 1.8,
                "R": -4.5,
                "N": -3.5,
                "D": -3.5,
                "C": 2.5,
                "Q": -3.5,
                "E": -3.5,
                "G": -0.4,
                "H": -3.2,
                "I": 4.5,
                "L": 3.8,
                "K": -3.9,
                "M": 1.9,
                "F": 2.8,
                "P": -1.6,
                "S": -0.8,
                "T": -0.7,
                "W": -0.9,
                "Y": -1.3,
                "V": 4.2,
            }
            total = 0
            for residue in sequence:
                total += scale[residue]
            return total / len(sequence)

        def showAbout():
            print("#FastProtein Software 1.0")
            print("#Developed by Renato Simões - renato.simoes@ifsc.edu.br")
            print("#Protein information software")
            print("#")
            print("#Obs:")
            print(
                "#Pattern ER Retention [KRHQSA][DENQ][E][L] based on PROSITE-PS00014 https://prosite.expasy.org/"
            )
            print("#Pattern N-Glyc [N][Xaa(except P)][ST] Xaa = any aminoacid")
            print("")

        # Initial processin

        # arguments saved here
        args = parser.parse_args()

        path_to_file = args.input
        path = Path(path_to_file)

        out_file = args.output

        if out_file == "results.out":
            out_file = args.input + "." + args.type

        patternErret = "[KRHQSA][DENQ][E][L]"
        patternNglyc = "[N][ARNDBCEQZGHILKMFSTWYV][ST]"

        proteinsFasta = SeqIO.parse(args.input, "fasta")

        proteins = []
        for prt in proteinsFasta:

            # Find for ERRET domain
            res_erret = re.finditer(patternErret, str(prt.seq), flags=re.IGNORECASE)
            er_domains = []
            for erret in res_erret:
                er_domains.append(
                    str(erret.group())
                    + str(erret.span()).replace("(", "[").replace(")", "]")
                )
            # Find for NGlyc domain
            res_nglyc = re.finditer(patternNglyc, str(prt.seq), flags=re.IGNORECASE)
            nglyc_domains = []
            for nglyc in res_nglyc:
                nglyc_domains.append(
                    str(nglyc.group())
                    + str(nglyc.span()).replace("(", "[").replace(")", "]")
                )
                # protein.addNGlycDomain(nglyc.group(), nglyc.span())

            protein = Protein(
                prt.id, prt.description, str(prt.seq), er_domains, nglyc_domains
            )
            proteins.append(protein)

        header = [
            "ID",
            "Length",
            "kDa",
            "Isoeletric Point",
            "Hydropathy",
            "Aromaticity",
            "ERRet Total",
            "ERRet domains",
            "NGlyc Total",
            "NGlyc domains",
            "Header",
            "Sequence",
        ]

        results = []
        for prot in proteins:
            try:
                proteinProp = ProteinAnalysis(prot.sequence)
                line = [
                    prot.id,
                    str(len(prot.sequence)),
                    f"{proteinProp.molecular_weight()/1000:.3f}",
                    f"{proteinProp.isoelectric_point():.2f}",
                    f"{hydrophobicity(prot.sequence):.2f}",
                    f"{proteinProp.aromaticity():.2f}",
                    str(len(prot.erretDomains)),
                    ",".join(prot.erretDomains),
                    str(len(prot.nGlycDomains)),
                    ",".join(prot.nGlycDomains),
                    prot.header,
                    prot.sequence,
                ]
                results.append(line)
            except Exception as e:
                print(f"An error occurred during processing the protein {prot.id}")
                print("Please check your FASTA file and restart the process")
                print(e)
                sys.exit()

        report = ""

        if args.type == "txt":
            # Calculating the max column length - this is use to arrange a formatted txt file
            col_length = []
            for column in header:
                col_length.append(len(column))

            for ln in results:
                for i in range(len(ln)):
                    if len(ln[i]) > col_length[i]:
                        col_length[i] = len(ln[i])

            results.insert(0, header)

            for idx, res in enumerate(results):
                if idx > 0:
                    report += "\n"

                report += str(res[0]).ljust(col_length[0], " ") + " "
                report += str(res[1]).rjust(col_length[1], " ") + " "
                report += str(res[2]).rjust(col_length[2], " ") + " "
                report += str(res[3]).rjust(col_length[3], " ") + " "
                report += str(res[4]).rjust(col_length[4], " ") + " "
                report += str(res[5]).rjust(col_length[5], " ") + " "
                report += str(res[6]).rjust(col_length[6], " ") + " "
                report += str(res[7]).ljust(col_length[7], " ") + " "
                report += str(res[8]).rjust(col_length[8], " ") + " "
                report += str(res[9]).ljust(col_length[9], " ") + " "
                report += str(res[10]).ljust(col_length[10], " ") + " "
                report += str(res[11]).ljust(col_length[11], " ")

        else:
            sep = "\t"
            if args.type == "csv":
                sep = ";"
            report = sep.join(header)
            for ln in results:
                report += "\n"
                report += sep.join(ln)

        showAbout()
        print(report)

        output = open(out_file, "w")
        output.write(report)
        output.close()

    if __name__ == "__main__":
        main()
