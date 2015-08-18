# handwritten_equations_recognizer
Implementation of a Handwritten Equations Recognizer.

## Description
This project was developed as part of the Google Summer of Code 2015[1] for the
International Institute of Geogebra[2]. Its goal is to create a program that
will recognize handwritten mathematical equations written with a mouse or a pen
or by using a touch screen.

The project consists of three(3) repositories hosted on Github. These
repositories are:

1. https://github.com/gouzouni625/handwritten_equations_recognizer
2. https://github.com/gouzouni625/neural_network
3. https://github.com/gouzouni625/symbol_extractor

with the second being a sub-module of the first.

## Implementation
The program uses Minimum Spanning Trees to separate the symbols of a given
equation and a Feed Forward Neural Network to classify these symbols. For more
information about the work being done behind the scenes please visit: [3]

## Getting the Code
To download the code to your local file system and work on it you have to do:

```
git clone https://github.com/gouzouni625/handwritten_equations_recognizer.git
cd handwritten_equations_recognizer
git submodule update --init
```

. The documentation is written using Doxygen[4]. To generate the documentation
pages, you have to install Doxygen and download the code to your local file
system. After that, do:

```
cd handwritten_equations_recognizer/doxygen
doxygen Doxyfile
```

. This will create two directories inside
`handwritten_equations_recognizer/doxygen`, `html` and `latex`. To view the
documentation pages, open  `html/index.html` with a browser of your choice.

## Usage
ToDo

## Efficiency
At the moment, the Hanwritten Equation Recognizer can recognize equation that
include the following symbols:

0, 1, 2, 3, 4, 5, 6, 7, 8, 9, =, +, -, x, y, (, ), sqrt(square root).

It can recognize fractions, exponents, indices and square roots, all of
arbitrary depth. Currently, the recognition of floating point numbers is not
supported.

To get the best result possible, make sure your inputs comply with the
following rules:

1. Draw your symbols large and clear.
2. Ink traces that belong to the same symbol, should be connected.
3. Ink traces that do not belong to the same symbol, should not be connected.
4. Make sure fractions "wrap" all their children. Concretely, the fraction line
    should be longer than the numerator and the denominator.
5. Make sure square roots "wrap" all their children. Concretely, the ink trace
    of the square root symbol should be longer than all the symbols under it.
6. Exponents and indices should be, at least, half the size of their base.
     That is, in 5^{2}, 2 should be, at least, half the size of 5.

## External Libraries
The external libraries used by this project are:

1. OpenCV for image processing. For more information, please visit
   http://opencv.org/

2. MySQL connector for Java. For more information, please visit
   https://www.mysql.com/products/connector/

3. Javascript Applet of GeoGebra. For more information, please visit
   https://www.geogebra.org/

4. JUnit for testing. For more information, please visit
   http://junit.org/

Each of these external libraries is subject to its own license.

## References

[1] https://www.google-melange.com/gsoc/homepage/google/gsoc2015

[2] http://wiki.geogebra.org/en/Comments:International_GeoGebra_Institute

[3] https://docs.google.com/document/d/1M4iMJfxZABqRuptV3rpgzYlqsVsFWxV0dmcqm6FA9_A/edit?usp=sharing

[4] http://www.doxygen.org
