Copyright 2015 by  Academy of Mathematics and Systems Science Chinese Academy of Sciences(University of Chinese Academy of Sciences)

GMap1.0 is a tool for ontology matching, This is raw, unoptimized research code. 

Time:6/23/2015  Author:Li Weizhuo   Mail:liweizhuo2014@gmail.com


Software: Java 1.6 or higher. 
Hardware: 2GB RAM or more. If you want to run matching of the large ontology such as mouse and human, you need at least 20GB RAM. The CPU is not limited, but we still hope that the CPU in your computer is as fast as possible, which can reduce a lot of time consumption.

Usage:
a)If you install eclipse, you can import this project directly and run the New_Main.java. There are lots of ontology saved in DataSets and you can use them to test GMap.

b)If you want to run the program by command line. you just need finish three steps:
1. enter directory of the GMap in command line. 
2. make sure that your tested ontologies have been listed in "Ontologies" file and the directory "result" has been created.
3. Run command "java -cp .;bin/;jars/* Actions.New_Main_cmd Ontologies cmt.owl Conference.owl result" in Windows environments
   or Run command "java -cp .:bin/:jars/* Actions.New_Main_cmd Ontologies cmt.owl Conference.owl result" in Linux environments

you can replace cmt.owl or Conference.owl with any ontologies, but the format of them need be ".rdf" or ".owl".

Results:
In directory "result", there are two types results . the common form is shown by triple(c1,c2,similarities), the standard format required by OAEI is like ",rdf" file. your can use Example/reference_alignments_test.java to parse it.

The our experiment results described in common form are saved in "Results" directory and the results in the standard format are stored in "Results_rdf" directory.








 