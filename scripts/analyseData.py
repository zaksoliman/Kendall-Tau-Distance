import os

csv_location = os.path.join(os.path.expanduser('~'),'Development/Kendall-Tau-Distance/Results/scripts')
csv_location = os.path.join(csv_location, "csv")

csv_files = os.listdir(csv_location)

csv_files.sort()
file_list = list(range(8))
file_list[7] =  csv_files[:30]

for i in range(7):
    file_list[i] = csv_files[:30]
    del csv_files[:30]



