import Objects.GlobalFreeList;
import Objects.GlobalTable;

/**
 * Created by shubham on 3/29/16.
 */
public class run {

    private static GlobalTable globalTable;
    private static GlobalFreeList globalFreeList;

    public static void main(String[] args) {
        int gtSize = 10;
        globalTable = new GlobalTable(gtSize);
        globalFreeList = new GlobalFreeList();
    }

}
