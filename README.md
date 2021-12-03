[![Basic Model Interface](https://img.shields.io/badge/CSDMS-Basic%20Model%20Interface-green.svg)](https://bmi.readthedocs.io/)
[![Build CI](https://github.com/csdms/bmi-example-java/actions/workflows/build.yml/badge.svg)](https://github.com/csdms/bmi-example-java/actions/workflows/build.yml)

# bmi-example-java

An example of implementing the [Java specification][bmi-java]
for the CSDMS [Basic Model Interface][bmi] (BMI).


## Overview

This is an example of implementing a BMI
for a simple model that solves the diffusion equation
on a uniform rectangular plate
with Dirichlet boundary conditions.
Tests and examples of using the BMI are provided.
The model and its BMI are written in Java.

This project is organized as follows:

<dl>
    <dt>src/main/java</dt>
	<dd><em>edu.colorado.csdms.heat</em>: Holds the model and a sample main program</dd>
	<dd><em>edu.colorado.csdms.bmiheat</em>: Holds the BMI for the model and a main   program to run the model through its BMI</dd>
	<dd><em>edu.colorado.csdms.example</em>: A helper to hold the main programs</dd>
	<dt>src/test/java</dt>
	<dd><em>edu.colorado.csdms.heat</em>: Unit tests for the model</dd>
	<dd><em>edu.colorado.csdms.bmiheat</em>: Unit tests for the model BMI</dd>
</dl>

## Build/Install

This example can be built on Linux, macOS, and Windows.

**Prerequisites:**
* A Java compiler
* [Maven](http://maven.apache.org)
* The Java BMI specification. The `pom.xml` in this project is configured to automatically download the `bmi` package from the [CSDMS Apache Maven Repository][csdms-maven-repo]

To compile and package the BMI Java bindings, run

    $ mvn clean verify

This will generate files in the project directory `target`.
The result will look like
```bash
target
|-- bmi-example-java.jar
|-- classes
|   `-- edu
|       `-- colorado
|           `-- csdms
|               |-- bmiheat
|               |   `-- BmiHeat.class
|               |-- example
|               |   `-- Examples.class
|               `-- heat
|                   |-- Heat.class
|                   |-- HeatConfigFile.class
|                   `-- Solve2D.class
|-- generated-sources
|   `-- annotations
|-- generated-test-sources
|   `-- test-annotations
|-- maven-archiver
|   `-- pom.properties
|-- maven-status
|   `-- maven-compiler-plugin
|       |-- compile
|       |   `-- default-compile
|       |       |-- createdFiles.lst
|       |       `-- inputFiles.lst
|       `-- testCompile
|           `-- default-testCompile
|               |-- createdFiles.lst
|               `-- inputFiles.lst
|-- surefire-reports
|   |-- TEST-edu.colorado.csdms.bmiheat.TestGetAndSetValue.xml
|   |-- TEST-edu.colorado.csdms.bmiheat.TestGridMethods.xml
|   |-- TEST-edu.colorado.csdms.bmiheat.TestIRF.xml
|   |-- TEST-edu.colorado.csdms.bmiheat.TestVarMethods.xml
|   |-- TEST-edu.colorado.csdms.heat.HeatTest.xml
|   |-- edu.colorado.csdms.bmiheat.TestGetAndSetValue.txt
|   |-- edu.colorado.csdms.bmiheat.TestGridMethods.txt
|   |-- edu.colorado.csdms.bmiheat.TestIRF.txt
|   |-- edu.colorado.csdms.bmiheat.TestVarMethods.txt
|   `-- edu.colorado.csdms.heat.HeatTest.txt
`-- test-classes
    |-- data
    |   `-- heat.xml
    `-- edu
        `-- colorado
            `-- csdms
                |-- bmiheat
                |   |-- TestGetAndSetValue.class
                |   |-- TestGridMethods.class
                |   |-- TestIRF.class
                |   `-- TestVarMethods.class
                `-- heat
                    `-- HeatTest.class

26 directories, 27 files
```

To install the jar file to your local Maven repository
(typically `~/.m2/repository`), run

    $ mvn install

The result in the local repository will look like
```bash
edu
`-- colorado
    `-- csdms
        |-- bmi
        |   |-- 2.0
        |   |   |-- _remote.repositories
        |   |   |-- bmi-2.0.jar
        |   |   |-- bmi-2.0.jar.md5
        |   |   |-- bmi-2.0.jar.sha1
        |   |   |-- bmi-2.0.pom
        |   |   |-- bmi-2.0.pom.md5
        |   |   `-- bmi-2.0.pom.sha1
        |   |-- maven-metadata-local.xml
        |   |-- maven-metadata-local.xml.md5
        |   `-- maven-metadata-local.xml.sha1
        `-- bmi-example-java
            |-- 1.0
            |   |-- _remote.repositories
            |   |-- bmi-example-java-1.0.jar
            |   `-- bmi-example-java-1.0.pom
            `-- maven-metadata-local.xml

6 directories, 14 files
```
Note that the `bmi` package is also present.


## Use

Run the Heat model through its BMI with

    $ mvn exec:java

Both the Heat and the BmiHeat examples are run, with output
```
*
* Example: Heat Model
*
shape: [8, 6]
spacing: [1.0, 1.0]
origin: [0.0, 0.0]
time = 0.0
temperature =
   0.00   0.00   0.00   0.00   0.00   0.00
   0.00   0.00   0.00   0.00   0.00   0.00
   0.00   0.00   0.00   0.00   0.00   0.00
   0.00   0.00 100.00   0.00   0.00   0.00
   0.00   0.00   0.00   0.00   0.00   0.00
   0.00   0.00   0.00   0.00   0.00   0.00
   0.00   0.00   0.00   0.00   0.00   0.00
   0.00   0.00   0.00   0.00   0.00   0.00
time = 0.25
temperature =
   0.00   0.00   0.00   0.00   0.00   0.00
   0.00   0.00   0.00   0.00   0.00   0.00
   0.00   0.00  12.50   0.00   0.00   0.00
   0.00  12.50  50.00  12.50   0.00   0.00
   0.00   0.00  12.50   0.00   0.00   0.00
   0.00   0.00   0.00   0.00   0.00   0.00
   0.00   0.00   0.00   0.00   0.00   0.00
   0.00   0.00   0.00   0.00   0.00   0.00
time = 0.5
temperature =
   0.00   0.00   0.00   0.00   0.00   0.00
   0.00   0.00   1.56   0.00   0.00   0.00
   0.00   3.13  12.50   3.13   0.00   0.00
   0.00  12.50  31.25  12.50   1.56   0.00
   0.00   3.13  12.50   3.13   0.00   0.00
   0.00   0.00   1.56   0.00   0.00   0.00
   0.00   0.00   0.00   0.00   0.00   0.00
   0.00   0.00   0.00   0.00   0.00   0.00
time = 0.75
temperature =
   0.00   0.00   0.00   0.00   0.00   0.00
   0.00   0.59   2.34   0.59   0.00   0.00
   0.00   4.69  11.13   4.69   0.59   0.00
   0.00  10.94  21.88  11.13   2.34   0.00
   0.00   4.69  11.13   4.69   0.59   0.00
   0.00   0.59   2.34   0.59   0.00   0.00
   0.00   0.00   0.20   0.00   0.00   0.00
   0.00   0.00   0.00   0.00   0.00   0.00
*
* Example: Heat Model run through its BMI
*
Model name: Heat
Start time: 0.0
End time: 1.7976931348623157E308
Current time: 0.0
Time step: 0.25
Time unit: null
Input variables: 
- plate_surface__temperature
Output variables: 
- plate_surface__temperature
Variable: plate_surface__temperature
- grid_id: 0
- grid type: uniform_rectilinear
- grid rank: 2
- grid size: 48
- grid shape:
  - 8
  - 6
- grid spacing:
  - 1.0
  - 1.0
- grid origin:
  - 0.0
  - 0.0
- var type: double
- var units: K
- var itemsize: 8
- var nbytes: 384
time = 0.0
temperature =
   0.00   0.00   0.00   0.00   0.00   0.00
   0.00   0.00   0.00   0.00   0.00   0.00
   0.00   0.00   0.00   0.00   0.00   0.00
   0.00   0.00 100.00   0.00   0.00   0.00
   0.00   0.00   0.00   0.00   0.00   0.00
   0.00   0.00   0.00   0.00   0.00   0.00
   0.00   0.00   0.00   0.00   0.00   0.00
   0.00   0.00   0.00   0.00   0.00   0.00
time = 0.25
temperature =
   0.00   0.00   0.00   0.00   0.00   0.00
   0.00   0.00   0.00   0.00   0.00   0.00
   0.00   0.00  12.50   0.00   0.00   0.00
   0.00  12.50  50.00  12.50   0.00   0.00
   0.00   0.00  12.50   0.00   0.00   0.00
   0.00   0.00   0.00   0.00   0.00   0.00
   0.00   0.00   0.00   0.00   0.00   0.00
   0.00   0.00   0.00   0.00   0.00   0.00
time = 0.5
temperature =
   0.00   0.00   0.00   0.00   0.00   0.00
   0.00   0.00   1.56   0.00   0.00   0.00
   0.00   3.13  12.50   3.13   0.00   0.00
   0.00  12.50  31.25  12.50   1.56   0.00
   0.00   3.13  12.50   3.13   0.00   0.00
   0.00   0.00   1.56   0.00   0.00   0.00
   0.00   0.00   0.00   0.00   0.00   0.00
   0.00   0.00   0.00   0.00   0.00   0.00
time = 0.75
temperature =
   0.00   0.00   0.00   0.00   0.00   0.00
   0.00   0.59   2.34   0.59   0.00   0.00
   0.00   4.69  11.13   4.69   0.59   0.00
   0.00  10.94  21.88  11.13   2.34   0.00
   0.00   4.69  11.13   4.69   0.59   0.00
   0.00   0.59   2.34   0.59   0.00   0.00
   0.00   0.00   0.20   0.00   0.00   0.00
   0.00   0.00   0.00   0.00   0.00   0.00
```


<!-- Links -->
[bmi]: https://bmi-spec.readthedocs.io
[bmi-java]: https://github.com/csdms/bmi-java
[csdms-maven-repo]: https://csdms.colorado.edu/repository
