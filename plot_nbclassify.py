import json
import matplotlib.pyplot as plt

with open('overall_result.json') as f:
    data = json.load(f)

i = 0
x_axis = []
while i <= 5.0:
    x_axis.append(round(i,2))
    i += 0.1

def plot_for_key(k1):
    new_data = {}
    for key in data[k1]:
            new_data[round(float(key), 2)] = data[k1][key]
    values = []
    i = 0
    j = 0
    while i <= 5.0:
        values.append(new_data[round(i,2)])
        j += 1
        print i, new_data[round(i,2)]
        i+=0.1
    return values

def plot_line(x,values,axis):
    plt.plot(x,values,'r-')
    plt.axis(axis)
    plt.show()

values = plot_for_key('4')
plot_line(x_axis,values,[0,5,80,90])
