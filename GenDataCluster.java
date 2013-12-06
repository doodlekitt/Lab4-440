public class GenDataCluster {
    public static void main(String[] args) {
        if(args.length != 4) {
            System.out.println("Expects arguments of the form:");
            System.out.println("<dim> <num> <range> <filename>");
            System.out.println("Where <dim> is the dimension of the data, " +
                "<num> is the number of data points, " +
                "the data points are between 0 and <range>, " +
                "and the result is saved to <filename>");
            return;
        }
        int dim = Integer.valueOf(args[0]).intValue();
        int num = Integer.valueOf(args[1]).intValue();
        int range = Integer.valueOf(args[2]).intValue();
        String filename = args[3];

        DataCluster.generate(dim, num, range, filename);
    }
}
