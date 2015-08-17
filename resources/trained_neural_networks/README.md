# Trained Feed Forward Neural Networks
In this folder you can find the two version(binary and xml) of a
Feed Forward Neural Network that can be used with the
handwritten equations recognizer.

## Architecture
This Neural Network consists of four(4) layers(including input and output
layers). The size of each layer is:

First(input layer)  : 2500

Second              :  100

Third               :  100

Fourth(output layer):   18

## Recognized symbols
The symbols that this neural network can recognize are the following:
0, 1, 2, 3, 4, 5, 6, 7, 8, 9, x, y, +, =, -, (, ), sqrt(square root).

## Data Used from Training and Testing
This Neural Network has been trained on data created by GeoGebra users and
gathered during the Google Summer of Code 2015 period. The pre-processing
of the data before being used to training the Neural Network was done using the
symbol extractor[1].

## Accuracy
The data used to train this Neural Network where split into two groups. The
first group contained the 70% of the data and was used for training the
Neural Network while the second group contained 30% of the data and was
used for testing the Neural Network. On this testing set, this Neural Network
reached an accuracy of 95.70%.

## References
[1] https://github.com/gouzouni625/symbol_extractor
