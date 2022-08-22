import matplotlib.pyplot as plt
import csv

file = open("kdaiso.csv", "r")
tsv_file = csv.reader(file, delimiter=",")
	# printing data line by line
kda_all = []
ph_all = []

for line in tsv_file:
	kda = float(line[0])
	ph = float(line[1])

	kda_all.append(kda)
	ph_all.append(ph)

plt.figure(1)
plt.scatter(ph_all, kda_all)
plt.ylabel('kDa', fontsize=16)
plt.xlabel('Isoelectric point (pH)', fontsize=16)
plt.savefig('kda-vs-pi.png')
plt.savefig('kda-vs-pi-300dpi.png', dpi=300)

plt.figure(2)
plt.hist(kda_all)
plt.ylabel('Proteins', fontsize=16)
plt.xlabel('Molecular mass (kDa)', fontsize=16)
yint = []
locs, labels = plt.yticks()
for each in locs:
    yint.append(int(each))
plt.yticks(yint)
plt.savefig('his-kda.png')
plt.savefig('his-kda-300dpi.png', dpi=300)
