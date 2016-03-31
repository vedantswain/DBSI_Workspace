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
}
