import mpi.*;
import java.util.List;
import java.util.HashMap;
import java.util.*;
import java.net.*
import java.io.*;

class DNACluster extends ICluster<char[]> {
    public void generate(int len, int num, String filename) {
        ArrayList<char[]> dna = new ArrayList<char[]>();
	ArrayList<char> TACG = new ArrayList<char>();
	TACG.add('A');
	TACG.add('T');
	TACG.add('G');
	TACG.add('C');

	// By randomly permuting the available DNA acids TACG each time, 
	// build num dna strands of length len

	for(int i = 0; i < num; i++){
	    char[] dnastrand = char[len];
	    for(int j = 0; j < len; j++){
		Collections.shuffle(TACG);
		dnastrand[j] = TACG.get(0);
	    }
	    dna.add(dnastrand);
	}

	// Write to file
	try{
	    FileOutputStream fs = new FileOutputStream(filename);
	    ObjectOutputStream outfile = new ObjectOutputStream(fs);
	    outfile.flush();
	    outfile.writeObject(dna);
	    outfile.close();
	} catch (Exception e) {
	    System.out.println(e);
	}
    }

    public int distance(char[] strand1, char[] strand2) {
        if(strand1 == null || strand2 == null ||
           strand1.length != strand2.length) {
            return -1;
        }
        int dist = 0;
        for(int i = 0; i < strand1.length; i++) {
            if(strand1[i] != strand2[i])
                dist++;
        }
        return dist;
    }

    // Assumes all strings of same length
    public char[] mean(List<char[]> strands) {
        if(strands == null || strands.size() == 0)
            return null;
        int strlen = strands.get(0).length;
        char[] mean = new char[strlen];
        for(int i = 0; i < strlen; i++) {
            // find the character which occurs most frequently
            HashMap<Character, Integer> freqs =
                new HashMap<Character, Integer>();
            for(char[] strand : strands) {
                char c = strand[i];
                if(!freqs.containsKey(c)) {
                    freqs.put(c, 1);
                } else {
                    int freq = freqs.get(c);
                    freqs.put(c, freq);
                }
            }
            int max = 0;
            char maxchar = 'G';
            for(Character c : freqs.keySet()) {
                if(freqs.get(c) > max) {
                    max = freqs.get(c);
                    maxchar = c;
                }
            }
            mean[i] = maxchar;
        }
        return mean;
    }

    static public void main(String[] args) throws MPIException {
        MPI.Init(args);
        int myrank = MPI.COMM_WORLD.Rank();
        // if is the master
        if(myrank == 0) {
            char[] message = "Hello, there".toCharArray();
            MPI.COMM_WORLD.Bcast(message, 0, message.length, MPI.CHAR, 0);
        } else {
            char[] message = new char[20];
            MPI.COMM_WORLD.Bcast(message, 0, 20, MPI.CHAR, 0);
            System.out.println("received:" + new String(message) + ":");
        }
        MPI.Finalize();
    }
}
