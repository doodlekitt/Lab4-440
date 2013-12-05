import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

// An abstract class for k-means clustering
public abstract class ICluster<E> {
    public static abstract List<E> generate(String[] args);

    public static abstract int distance(E elem1, E elem2);

    public static abstract E mean(List<E> elements);

    public static List<E> sequentialCluster(List<E> data, int k) {
        // if k >= length of data, data are the centroids
        if(k >= data.size())
            return data;
        if(k <= 0)
            return new ArrayList<E>(); // return empty list

        // Pick initial centroids by choosing random data points
        Collections.shuffle(data);
        List<E> centroids = data.subList(0, k);

        boolean hasConverged = false;
        // Repeat the algorithm until convergence
        while(!hasConverged) {
            // Assign elements to nearest cluster
            HashMap<E, List<E>> clusters = new HashMap<E, List<E>>();
            for(E c : centroids) {
                clusters.put(c, new ArrayList<E>());
            }
            for(E elem : data) {
                int mindist = Integer.MAX_VALUE;
                E mincent = null;
                for(E c : centroids) {
                    int dist = distance(elem, c);
                    if(dist < mindist) {
                        mindist = dist;
                        mincent = c;
                    }
                }
                // Add element to cluster of nearest centroid
                List<E> cluster = clusters.get(mincent);
                cluster.add(elem);
                clusters.put(mincent, cluster);
            }
            // Reevaluate centroids
            centroids = new ArrayList<E>();
            hasConverged = true;
            for(E oldcentroid : clusters.keySet()) {
                E newcentroid = mean(clusters.get(oldcentroid));
                if (oldcentroid != newcentroid)
                    hasConverged = false;
                centroids.add(newcentroid);
            }
        }
        return centroids;
    }

    public static class Message implements java.io.Serializable {
        public enum Type {
            MEAN, FINALIZE;
        }

        Type type;
        List<E> elements;

        // Constructors
        Message(Type type) {
            this.type = type;
        }

        Message(Type type, List<E> elements) {
            this.type = type;
            this.elements = elements;
        }
    }

    // Runs the parallel algorithm with MPI
    public static void main(String[] args) throws MPIException {
        if(args.length != 2) {
            String message = "Expects arguments of the form:\n";
            message += "<filename> <k>\n";
            message += "Where <filename> contains the data and
            message += "<k> is the desired number of clusters\n";
            System.out.print(message);
            return;
        }

        // parse args
        List<E> data = null;
        try {
            ObjectInputStream ois =
                new ObjectInputStream(new FileInputStream(args[0]));
            data = (List<E>) ois.readObject();
        } catch (Exception e) {
            System.out.println("ERROR: Could not read data from file");
            return;
        }
        int k = Integer.valueOf(args[1]).intValue();

        // if k >= length of data, data are the centroids
        if(k >= data.size())
            return data;
        if(k <= 0)
            return new ArrayList<E>(); // return empty list
        
        MPI.Init();
        int myrank = MPI.COMM_WORLD.Rank();

        // Pick initial centroids by choosing random data points
        Collections.shuffle(data);
        List<E> centroids = data.subList(0, k);

        // if is the master
        if(myrank == 0) {
            boolean hasConverged = false;
            // Repeat the algorithm until convergence
            while(!hasConverged) {
                // Assign elements to nearest cluster
                HashMap<E, List<E>> clusters = new HashMap<E, List<E>>();
                for(E c : centroids) {
                    clusters.put(c, new ArrayList<E>());
                }
                for(E elem : data) {
                    int mindist = Integer.MAX_VALUE;
                        E mincent = null;
                        for(E c : centroids) {
                            int dist = distance(elem, c);
                        if(dist < mindist) {
                            mindist = dist;
                            mincent = c;
                        }
                    }
                }
                // Add element to cluster of nearest centroid
                List<E> cluster = clusters.get(mincent);
                cluster.add(elem);
                clusters.put(mincent, cluster);
            }
            MPI.COMM_WORLD.Bcast(message, 0, message.length, MPI.CHAR, 0);
        } else {
            char[] message = new char[20];
            MPI.COMM_WORLD.Bcast(message, 0, 20, MPI.CHAR, 0);
            System.out.println("received:" + new String(message) + ":");
        }
        MPI.Finalize();
    }
}
