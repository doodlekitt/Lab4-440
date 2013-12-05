import java.lang.String;
import java.util.List;
import java.io.*;
import java.util.*;
import java.net.*;

class DataCluster extends ICluster<int[]> {
    public void generate(int len, int num, int range, String filename) {
	ArrayList<int[]> points = new ArrayList<int[]>();
	Random rand = new Random();

	// Loop through and generate num points of length len within the 
	// range of 0 to range using a random generator

	for(int i = 0; i < num; i++){
	    int[] vector =  int[len]; 
	    for(int j = 0; j < len; j++){
		vector[j] = rand.nextInt(range + 1);
	    }
	    points.add(vector);
	}

	//Write to file

	try{
	    FileOutputStream fs = new FileOutputStream(filename);
	    ObjectOutputStream outfile = new ObjectOutputStream(fs);
	
	    outfile.flush();
	    outfile.writeObject(points);
	    outfile.close();
	}catch (Exception e) {
	    System.out.println(e);
	}


    }

    public int distance(int[] point1, int[] point2) {
        if(point1 == null || point2 == null ||
           point1.length != point2.length)
            return -1;
        int sum = 0;
        for(int i = 0; i < point1.length; i++) {
            int diff = point1[i] - point2[i];
            sum += diff * diff;
        }
        return (int)Math.sqrt(sum);
    }

    public int[] mean(List<int[]> points) {
        for(int i = 0; i < points.size(); i++) {
            for(int[] point : points) {
                // do things
            }
        }
        return null;
    }
}
