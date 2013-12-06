import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.*;

public class SerDataCluster{

    public static void main(String[] args){
	if(args.length != 2){
	     System.out.println("Expecting args of form <filename> <k>");
	     return;
	}
	else{
	    String filepath = args[0];
	    int k = Integer.valueOf(args[1]).intValue();
	    try{
		FileInputStream fs = new FileInputStream(filepath);
		ObjectInputStream infile = new ObjectInputStream(fs);
		List<int[]> data = (List<int[]>) infile.readObject();
        
        	//Creates and runs instance of Data Cluster sequentially
        	DataCluster dc = new DataCluster ();
        	List<E> ans = dc.sequentialCluster(data, k);

		// Print centroids
	  	System.out.println(Arrays.toString(ans.toArray()));

		infile.close();

	    } catch (Exception e){
		System.out.println(e);
	    }
	}
    }

}
