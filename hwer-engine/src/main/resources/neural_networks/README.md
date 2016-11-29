# Trained Feed Forward Neural Networks
In this directory you can find the trained Feed Forward Neural Networks that this Handwritten
Equations Recognizer uses.

## Architecture
Each of these neural networks consists of five layers. These layers are the input layer, the output
layer and three hidden layers. The input layer has 400 neurons, the first hidden layer has 64, the
second hidden layer has 32 and the third hidden layer has 16 neurons. The size of the output layer
is different for each neural network and is its size is given below:

cascade neural network: 4  
numbers neural network: 10  
variables neural network: 2  
operators neural network: 9  
letters neural network: 6

## Recognized symbols
* The cascade neural network can classify an input into 4 categories. These categories are: number,
variable, operator or letter.

* The numbers neural network can classify an input into 10 categories. These categories are: zero,
one, two, three, four, five, size, seven, eight and nine.

* The variables neural network can classify an input into 2 categories. These categories are: x or
y.

* The operators neural network can classify an input into 9 categories. These categories are: +, =,
-, sqrt, (, ), >, < and |.

* The letters neural network can classify an input into 6 categories. These categories are: a, e, i,
l, n and t.

## Data Used from Training and Testing
These neural networks have been trained on data created by the GeoGebra users and gathered during
Google Summer of Code 2015.

## Accuracy
The data used to train these neural networks where split into two groups. The first group contained
the 70% of the data and was used for training while the second group contained the rest 30% of the
data and was used for testing. On this testing set, each neural network achieved an accuracy higher
that 95%.

The training of the neural networks was done using [Tensorflow](https://www.tensorflow.org/).
