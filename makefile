JCFLAGS = -g -Xlint
JC = mpijavac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

default: GenDataCluster.java ParDataCluster.java DataCluster.java GenDNACluster.java ParDNACluster.java DNACluster.java ICluster.java
	$(JC) $(JCFLAGS) GenDataCluster.java ParDataCluster.java DataCluster.java GenDNACluster.java ParDNACluster.java DNACluster.java ICluster.java

clean:
	$(RM) *.class
