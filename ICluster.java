import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

// An abstract class for k-means clustering
public abstract class ICluster<E> {
    public abstract List<E> generate(String[] args);

    public abstract int distance(E elem1, E elem2);

    public abstract E mean(List<E> elements);

    public List<E> sequentialCluster(List<E> data, int k, int mu) {
        // if k >= length of data, data are the centroids
        if(k >= data.size())
            return data;
        if(k <= 0)
            return new ArrayList<E>(); // return empty list

        // Pick initial centroids by choosing random data points
        Collections.shuffle(data);
        List<E> centroids = data.subList(0, k);
        HashMap<E, List<E>> clusters = new HashMap<E, List<E>>();        

        // Repeat the algorithm mu times
        for(int i = 0; i < mu; i++) {
            // Assign elements to nearest cluster
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
            for(List<E> cluster : clusters.values()) {
                E centroid = mean(cluster);
                centroids.add(centroid);
            }
        }
        return centroids;
    }

    public List<E> parallelCluster(E[] data) {
        return null;
    }
}
