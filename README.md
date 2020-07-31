# stringcorrector
An implementation based on MTrees and Levenshtein distance to find the nearest string to a given string.

## build
This project uses Java 1.8+ and maven as it's build toolchain. To build and run execute 

mvn compile 
mvn exec:java -Dexec.mainClass="org.free.App"

## usage
The main app has a terminal interface similar to ncurses. You can add a string, search the nearest string,
load a file (or multiple files) into memory and search. A parameter tolerance values can be set which is 
the maximum edit distance that any input string is allowed to have with a given search string.

## attribution 
This project uses the following third party libraries

https://github.com/erdavila/M-Tree 

https://github.com/mabe02/lanterna
