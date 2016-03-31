package MyCommons;

/**
 * Created by shubham on 3/30/16.
 */
public class MyCommons {
    private static int BUFFER_SIZE;
    private static int EMP_BLOCKS = 1000;
    private static int EMP_RECORDS = 10000;
    private static int DEPT_BLOCKS = 25;
    private static int DEPT_RECORDS = 100;
    private static int PROJ_BLOCKS = 500;
    private static int PROJ_RECORDS = 5000;

    public static void setBufferSize(int bufferSize) {
        BUFFER_SIZE = bufferSize;
    }

    public static int getBufferSize() {
        return BUFFER_SIZE;
    }
}
