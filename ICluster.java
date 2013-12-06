import java.util.*;
import java.io.*;
import mpi.*;

// An abstract class for k-means clustering
public abstract class ICluster<E> {
    public abstract int distance(E elem1, E elem2);

    public abstract E mean(List<E> elements);

    public List<E> sequentialCluster(List<E> data, int k) {
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
}
