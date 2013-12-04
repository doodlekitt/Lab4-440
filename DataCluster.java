import java.lang.String;
import java.util.List;

class DataCluster extends ICluster<int[]> {
    public List<int[]> generate(String[] args) {
        // TODO: Implement
        return null;
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

    public int[] mean(List<int[]> pointss) {
        return null;
    }
}
