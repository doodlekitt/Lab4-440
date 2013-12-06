public class GenDNACluster {
    public static void main(String[] args) {
        if(args.length != 3) {
            System.out.println("Expects arguments of the form:");
            System.out.println("<len> <num> <filename>");
            System.out.println("Where <len> is the strand lengtj, " +
                "<num> is the number of data points, " +
                "and the result is saved to <filename>");
            return;
        }
        int len = Integer.valueOf(args[0]).intValue();
        int num = Integer.valueOf(args[1]).intValue();
        String filename = args[2];

        DNACluster dc = new DNACluster();
        dc.generate(len, num, filename);
    }
}
