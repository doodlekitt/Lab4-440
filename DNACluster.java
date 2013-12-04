import java.lang.String;
import java.util.List;

class DNACluster extends ICluster<String> {
    public List<String> generate(String[] args) {
        // TODO: Implement
        return null;
    }

    public int distance(String strand1, String strand2) {
        if(strand1 == null || strand2 == null ||
           strand1.length() != strand2.length()) {
            return -1;
        }
        int dist = 0;
        for(int i = 0; i < strand1.length(); i++) {
            if(strand1.substring(i, i+1) != strand2.substring(i, i+1))
                dist++;
        }
        return dist;
    }

    public String mean(List<String> strands) {
        return "";
    }
}
