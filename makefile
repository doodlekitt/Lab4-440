JCFLAGS = -g
JC = mpijavac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

default: DataCluster.java DNACluster.java ICluster.java
	$(JC) $(JCFLAGS) DataCluster.java DNACluster.java ICluster.java

clean:
	$(RM) *.class
