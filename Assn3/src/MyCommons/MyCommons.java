package MyCommons;

/**
 * Created by shubham on 3/30/16.
 */
public class MyCommons {
    private static int BUFFER_SIZE = 50;
    private static int EMP_BLOCKS = 1000;
    private static int EMP_RECORDS = 10000;
    private static int DEPT_BLOCKS = 25;
    private static int DEPT_RECORDS = 100;
    private static int PROJ_BLOCKS = 500;
    private static int PROJ_RECORDS = 5000;

    public static int getEmpBlocks() {
        return EMP_BLOCKS;
    }

    public static int getEmpRecords() {
        return EMP_RECORDS;
    }

    public static int getDeptBlocks() {
        return DEPT_BLOCKS;
    }

    public static int getDeptRecords() {
        return DEPT_RECORDS;
    }

    public static int getProjBlocks() {
        return PROJ_BLOCKS;
    }

    public static int getProjRecords() {
        return PROJ_RECORDS;
    }

    public static void setBufferSize(int bufferSize) {
        BUFFER_SIZE = bufferSize;
    }

    public static int getBufferSize() {
        return BUFFER_SIZE;
    }

    public static float getProduct(int n, float d, int k){
        float res = 1;
        for (int i = 1; i <= k; i++) {
            res = res * (((n*d)-i+1)/(n-i+1));
//            System.out.println("deno: "+(n-i+1)+", d="+d);
        }
        return res;
    }

    public static int getYaosValue(int m, int n, int k){
        float d = (float) (1 - (1.0/m));
//        System.out.println("Product: "+getProduct(n, d, k));
        return (int) Math.ceil(m * (1 - getProduct(n, d, k)));
    }
}
