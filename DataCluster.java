import java.lang.String;
import java.util.List;
import java.io.*;
import java.util.*;
import java.net.*;

class DataCluster extends ICluster<int[]> {
    public static void generate(int dim, int num, int range, String filename) {
	ArrayList<int[]> points = new ArrayList<int[]>();
	Random rand = new Random();

	// Loop through and generate num points of length dim within the 
	// range of 0 to range using a random generator

	for(int i = 0; i < num; i++){
	    int[] vector = new int[dim]; 
	    for(int j = 0; j < dim; j++){
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
        if(points == null || points.size() == 0)
            return null;
        int length = points.get(0).length;
        int[] mean = new int[length];
        for(int i = 0; i < length; i++) {
            int sum = 0;
            for(int[] point : points) {
                sum += point[i];
            }
            mean[i] = sum / points.size();
        }
        return mean;
    }
}
