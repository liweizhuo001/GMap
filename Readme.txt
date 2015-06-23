Copyright 2015 by  Academy of Mathematics and Systems Science Chinese Academy of Sciences(University of Chinese Academy of Sciences)

GMap1.0 is a tool for ontology matching, This is raw, unoptimized research code. 

6/23/2015 Li Weizhuo   liweizhuo2014@gmail.com

Requirements(Windows):

Software: Java 1.6 or higher. 
Hardware: 2GB RAM or more. If you want to run the large ontology such as anatomy. you need 16GB. The CPU is not limited.

Usage:

a)If you install eclipse, you can import this project directly and run the New_Main.java. There are lots of ontology saved in DataSet and you can use them to test.

b)If you want to run the program by command line. you just need three steps:
1. enter directory of the GMap . 
2. make sure that your tested ontologies have been listed in Ontologies File and the directory result is created.
3. Run java -cp .;bin/;jars/* Actions.Main_cmd Ontologies russiaA.owl russiaB.owl result

you can replace russiaA.owl or russiaB.owl with any ontologies, but the format of them need be ".rdf" or ".owl".
In directory result, The results  have two file. One is shown by triple(c1,c2,similarities), the other is depicted in special format required by OAEI.




 