This is a Maven project developed in NetBeans and takes as argument the name of the relative path of the file

The challenge here was the size of the input file.
The approach used follows the below logic,

The input file is read using a combination of stream and tree-model parsing. 
Each individual record is read in a tree structure, but the file is never read in its entirety into memory, 
making it possible to process JSON files gigabytes in size while using minimal memory.

From a basic ~20,000 records run the result is 100 ms