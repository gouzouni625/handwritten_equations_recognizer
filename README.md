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

```bash
git clone https://github.com/gouzouni625/handwritten_equations_recognizer.git
cd handwritten_equations_recognizer
git submodule update --init
```

.

## Building the Documentation
The documentation is written using Doxygen[4]. To generate the documentation
pages, you have to install Doxygen and download the code to your local file
system. After that, do:

```bash
cd handwritten_equations_recognizer/doxygen
doxygen Doxyfile
```

. This will create two directories inside
`handwritten_equations_recognizer/doxygen`, `html` and `latex`. To view the
documentation pages, open  `html/index.html` with a browser of your choice.

## Usage
Since the project is still in beta version, using it requires some work but it
is possible. First, we have to download the code by doing:

```bash
git clone https://github.com/gouzouni625/handwritten_equations_recognizer.git
cd handwritten_equations_recognizer
git submodule update --init
```

. After that, we have to build the project and package it in `.jar` files.
This is done using Apache Maven so make sure it is installed on your system.
To build the project and package it to `.jar files`, we need to do:

```bash
cd maven
mvn package
```

. Now we must download the dependences of the project and move everything on
a single directory. This can be done with the following commands:

```bash
mvn dependency:copy-dependencies
cd ..
mkdir run
cp target/handwritten_equation_recognizer-0.9.jar run/
cp sub_modules/neural_network/target/neural_network-0.9.jar run/
cp sub_modules/neural_network/target/dependency/opencv-2.4.9-4.jar run/
cp resources/trained_neural_networks/neural_network.binary run/
```

. Note that `mvn dependency:copy-dependencies` will show some errors when
trying to download `neural_network` but you can ignore this. Now that we have
everything on a single directory, there are a few more things to do. That is,
we must extract the native library of OpenCV from inside the `.jar` file so
it can be loaded from the Handwritten Equation Recognizer. This can be done
with the following commands:

```bash
cd run
unzip opencv-2.4.9-4.jar -d OpenCV
```

. Now, you are ready to use the Handwritten Equation Recognizer. You can use it
with the following command:

```bash
java -Djava.library.path=OpenCV/nu/pattern/opencv/<proper-library> \
-cp opencv-2.4.9-4.jar:neural_network-0.9.jar:handwritten_equation_recognizer-0.9.jar main.java.executables.HandWrittenEquationRecognizer \
"neural_network.binary" \
"<inkml-string>"
```

. To make sure everything is working properly, try running:

```bash
java -Djava.library.path=OpenCV/nu/pattern/opencv/linux/x86_64/ \
-cp opencv-2.4.9-4.jar:neural_network-0.9.jar:handwritten_equation_recognizer-0.9.jar main.java.executables.HandWrittenEquationRecognizer \
"neural_network.binary" \
"<ink xmlns="http://www.w3.org/2003/InkML"/><definitions><canvasTransform xml:id="canvasTransform1"><mapping type="affine"><affine>161.2538 -339.8977 0, 162.9998 -547.9897 0,</affine></mapping></canvasTransform><context xml:id="context1" canvasTransformRef="#canvasTransform1"/></definitions><annotation type="equationInTeX">$undefined$</annotation><traceGroup xml:id="traceGroup1" contextRef="#context1"><trace>0 3.1, 0.64 1.74, 0.86 1.3, 1.68 0.12, 1.82 -0.06, 1.82 0.04</trace><trace>1.98 3.02, 1.36 2.04, 0.5 1.1, -0.02 0.34, -0.12 0.28, -0.08 0.46</trace><trace>2.3 4.66, 2.52 4.88, 2.86 5.04, 3.1 5.04, 3.24 4.82, 3.22 4.3, 2.94 3.8, 2.72 3.6, 3.1 3.6, 3.52 3.6, 3.58 3.58, 3.52 3.54</trace><trace>4.86 3.48, 4.86 3.3, 4.76 2.2, 4.7 1.26, 4.72 1.02</trace><trace>4.44 1.9, 4.82 1.96, 5.2 1.98, 5.34 2.02</trace><trace>8.2 3.68, 7.9 3.74, 7.14 3.84, 6.78 3.72, 6.7 2.96, 6.7 2.34, 6.74 2.18, 6.84 2.2, 7.7 2.38, 8.74 2.4, 8.92 2.26, 8.8 1.8, 8.26 1.14, 7.72 0.9, 7.18 1, 6.9 1.14</trace><trace>9.46 3.26, 9.52 3.28, 10.46 2.04, 11.2 1.2, 11.5 0.98</trace><trace>11.24 3, 10.66 2.32, 10.02 1.52, 9.66 1.04, 9.62 0.94, 10 1.06</trace><trace>12.48 3.18, 12.48 3.02, 12.56 1.8, 12.64 1.26</trace><trace>11.86 2.12, 11.96 2.14, 12.82 2.14, 13.44 2.14, 13.56 2.16</trace><trace>15.72 3.78, 15.5 3.78, 14.78 3.04, 14.46 2.06, 14.52 1.18, 14.9 0.8, 15.54 0.8, 15.94 1.22, 15.96 1.54, 15.74 1.76, 15.12 1.94, 14.78 1.96</trace><trace>16.74 2.14, 16.9 2.16, 18.04 2.16, 18.92 2.16, 19.04 2.16</trace><trace>16.8 1.62, 16.86 1.6, 17.4 1.5, 18.2 1.48, 18.8 1.5, 18.92 1.58</trace><trace>20.28 2.72, 20 2.22, 20 1.52, 20.2 1.02, 20.42 0.84, 20.74 0.9, 21.08 1.32, 21.22 1.84, 21.14 2.38, 20.8 2.78, 20.42 2.84, 20.28 2.82</trace></traceGroup></ink>"
```

. You should get the output: x^{2}+5x+6=0.

## Efficiency
At the moment, the Handwritten Equation Recognizer can recognize equation that
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
