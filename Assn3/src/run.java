import Objects.*;

/**
 * Created by shubham on 3/29/16.
 */
public class run {

    private static GlobalTable globalTable;
    private static GlobalFreeList globalFreeList;
    private static PageTable pageTable;
    private static int hitCounter;
    private static int missCounter;

    private static void increaseHitCounter(){
        ++hitCounter;
    }

    private static void increaseMissCounter(){
        ++missCounter;
    }

    private static void requestPage(Page page, FileInstance fileInstance){
        if (globalTable.bufferListContains(page, pageTable))
        {
            //update statistics
        }
        else if (globalFreeList.freeListContains(page)){
            fileInstance.addToLocalitySet(page);
            globalTable.addToGlobalTable(page, pageTable);
        }
        else{
            globalFreeList.addToFreeList(page);
            requestPage(page, fileInstance);
        }

    }

    public static void main(String[] args) {
        globalTable = new GlobalTable();
        globalFreeList = new GlobalFreeList();
        pageTable = new PageTable();
        hitCounter = 0;
        missCounter = 0;
//        MyCommons.setBufferSize(10);
    }

}
