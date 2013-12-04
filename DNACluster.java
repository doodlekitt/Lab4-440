import java.util.List;
import java.util.HashMap;

class DNACluster extends ICluster<char[]> {
    public List<char[]> generate(String[] args) {
        // TODO: Implement
        return null;
    }

    public int distance(char[] strand1, char[] strand2) {
        if(strand1 == null || strand2 == null ||
           strand1.length != strand2.length) {
            return -1;
        }
        int dist = 0;
        for(int i = 0; i < strand1.length; i++) {
            if(strand1[i] != strand2[i])
                dist++;
        }
        return dist;
    }

    // Assumes all strings of same length
    public char[] mean(List<char[]> strands) {
        if(strands == null || strands.size() == 0)
            return null;
        int strlen = strands.get(0).length;
        char[] mean = new char[strlen];
        for(int i = 0; i < strlen; i++) {
            // find the character which occurs most frequently
            HashMap<Character, Integer> freqs =
                new HashMap<Character, Integer>();
            for(char[] strand : strands) {
                char c = strand[i];
                if(!freqs.containsKey(c)) {
                    freqs.put(c, 1);
                } else {
                    int freq = freqs.get(c);
                    freqs.put(c, freq);
                }
            }
            int max = 0;
            char maxchar = 'G';
            for(Character c : freqs.keySet()) {
                if(freqs.get(c) > max) {
                    max = freqs.get(c);
                    maxchar = c;
                }
            }
            mean[i] = maxchar;
        }
        return mean;
    }
}
