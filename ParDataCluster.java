import java.util.*;
import java.io.*;
import mpi.*;

class ParDataCluster {

    public static class Message implements Serializable {
        public enum Type {
            FINALIZE, MEAN;
        }

        private Type type;
        private List<int[]> task;

        Message(Type type) {
            this.type = type;
        }

        Message(Type type, List<int[]> task) {
            this.type = type;
            this.task = task;
        }

        public Type type() {
            return type;
        }

        public List<int[]> task() {
            return task;
        }
    }

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        if(args.length != 2) {
            String message = "Expects arguments of the form:\n";
            message += "<filename> <k>\n";
            message += "Where <filename> contains the data and";
            message += "<k> is the desired number of clusters\n";
            System.out.print(message);
            return;
        }

        DataCluster dc = new DataCluster();
        intArrayComp comp = new intArrayComp();

        List<int[]> data = null;
        try {
            ObjectInputStream ois =
                new ObjectInputStream(new FileInputStream(args[0]));
            data = (List<int[]>) ois.readObject();
        } catch (Exception e) {
            System.out.println("ERROR: Could not read data from file");
            return;
        }
        int k = Integer.valueOf(args[1]).intValue();
        int datalen = data.get(0).length;

        int myrank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        Collections.shuffle(data);
        List<int[]> centroids = data.subList(0, k);
        // sort the centroids
        Collections.sort(centroids,comp);

        if(myrank == 0) {
            boolean hasConverged = false;
            while(!hasConverged) {
                HashMap<int[], List<int[]>> clusters =
                    new HashMap<int[], List<int[]>>();
                for(int[] c : centroids) {
                    clusters.put(c, new ArrayList<int[]>());
                }
                for(int[] elem : data) {
                    int mindist = Integer.MAX_VALUE;
                        int[] mincent = null;
                        for(int[] c : centroids) {
                            int dist = dc.distance(elem, c);
                        if(dist < mindist) {
                            mindist = dist;
                            mincent = c;
                        }
                    }
                    List<int[]> cluster = clusters.get(mincent);
                    cluster.add(elem);
                    clusters.put(mincent, cluster);
                }

                List<int[]> newcentroids = new ArrayList<int[]>();

                // Send data to other threads
                Message[] messages = new Message[centroids.size()];
                for(int i = 0; i < centroids.size(); i++) {
                    Message message =
                        new Message(Message.Type.MEAN,
                                    clusters.get(centroids.get(i)));
                    messages[i] = message;
                }
                MPI.COMM_WORLD.Bcast(messages, 0, k, MPI.OBJECT, 0);

                for(int i = 0; i < k; i++) {
                    int[] newcentroid = new int[datalen];
                    MPI.COMM_WORLD.Recv(newcentroid, 0, datalen, MPI.INT, 0, 99);
                    newcentroids.add(newcentroid);
                }
                // sort centroids
                Collections.sort(newcentroids, comp);
                // Check if new centroids are same
                hasConverged = true;
                for(int i = 0; i < centroids.size(); i++) {
                   if(!Arrays.equals(centroids.get(i), newcentroids.get(i))) {
                       hasConverged = false;
                   }
                }
                centroids = newcentroids;
            }
            Message[] kill = new Message[1];
            kill[0] = new Message(Message.Type.FINALIZE);
            MPI.COMM_WORLD.Bcast(kill, 0, 1, MPI.OBJECT, 0);
            System.out.println(centroids);
        } else {
            while(true) {
                Message[] messages = new Message[k];
                MPI.COMM_WORLD.Bcast(messages, 0, k, MPI.OBJECT, 0);
                if(messages[0].type() == Message.Type.FINALIZE)
                    break;
                for(int i = myrank-1; i < k; i+= MPI.COMM_WORLD.Size()-1) {
                    int[] centroid = dc.mean(messages[i].task());
                    MPI.COMM_WORLD.Send(centroid, 0, centroid.length, MPI.INT,
                                        0, 99);
                }
            }
        }
        MPI.Finalize();
    }

    private static class intArrayComp implements Comparator<int[]> {
        public int compare(int[] o1, int[] o2) {
            for(int i = 0; i < Math.min(o1.length, o2.length); i++) {
                if(o1[i] < o2[i])
                    return -1;
                else if(o1[i] > o2[i])
                    return 1;
            }
            if(o1.length < o2.length)
                return -1;
            else if(o1.length > o2.length)
                return 1;
            return 0;
        }
    }
}

