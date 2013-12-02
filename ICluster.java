// An interface for k-means clustering
public interface ICluster<E> {
    public List<E> Generate(String[] args);

    public int Distance(E elem1, E elem2);

    public List<E> SequentialCluster(List<E> data, int k, int mu) {
        // if k >= length of data, data are the centroids
        if(k >= data.size())
            return data;
        if(k <= 0)
            return new List<E>(); // return empty list

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
                    int dist = Distance(elem, c);
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
            for(E c : clusters.keySet()) {
                // find mean 
            }
        }
    }

    public E[] ParallelCluster(E[] data);
}
