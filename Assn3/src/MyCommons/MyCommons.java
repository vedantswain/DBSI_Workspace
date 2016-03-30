package MyCommons;

/**
 * Created by shubham on 3/30/16.
 */
public class MyCommons {
    private static int BUFFER_SIZE;

    public static void setBufferSize(int bufferSize) {
        BUFFER_SIZE = bufferSize;
    }

    public static int getBufferSize() {
        return BUFFER_SIZE;
    }
}
