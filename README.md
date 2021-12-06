# Overview

This command line tool can be used to efficiently count number of occurrences of specific words in selected file. Its
results are by default printed to standard output and written to text file. It also supports export to .xlsx file and
adding those files to .zip archive.

# Usage

Note: you need `java` pointing to Java 11+ JRE on your path.

```java -jar IntivePatronage_Project.jar [OPTIONS]```

* `-h`, `--help`                Print help message and quit
* `-p`, `--source-path <arg>`   Path of source file (including file)
* `-r`, `--result-path <arg>`   Path of result file (directory; source directory by default)
* `-x`, `--xlsx`                save to .xlsx file
* `-z`, `--zip`                 zip result file(s)

For easier demonstration, there is Example.txt file added to this repository. You can test run this program using
following command:
```java -jar IntivePatronage_Project.jar -p Example.txt --xlsx```

# Tech overview

## Code optimizations

I used HashSet for efficient execution of program's core procedure - counting words in a file. HashSet is great for
storing results of such operations, because of O(1) read and write complexity.

## Used libraries

I used some libraries from Apache project, namely POI and Commons CLI, for parsing CLI arguments and writing to .xlsx
file. Thanks to that I was able to focus on other, more creative aspects of this project.

### Unit testing

For unit testing core function from WordCounter class, I used JUnit 5.

### Package management

For managing external libraries/packages, I used Maven. `pom.xml` file contains list of dependencies.