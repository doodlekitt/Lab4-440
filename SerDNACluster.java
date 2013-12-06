import java.io.*;
import java.util.*;
import java.lang.*;
import java.net.*;

public class SerDNACluster {

    public static void main (String args[]){
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
                List<char[]> data = (List<char[]>) infile.readObject();

                //Creates and runs instance of DNACluster sequentially
                DNACluster dc = new DNACluster ();
                List<E> ans = dc.sequentialCluster(data, k);

                // Print centroids
                System.out.println(ans.toArray());

                infile.close();

            } catch (Exception e){
                System.out.println(e);
            }
        }
    }

}
