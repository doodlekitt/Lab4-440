JCFLAGS = -g
JC = mpijavac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

default: ParDataCluster.java DataCluster.java DNACluster.java ICluster.java
	$(JC) $(JCFLAGS) ParDataCluster.java DataCluster.java DNACluster.java ICluster.java

clean:
	$(RM) *.class
