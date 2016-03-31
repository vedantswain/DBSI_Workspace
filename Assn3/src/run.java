import MyCommons.MyCommons;
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

    private static void requestPage(Page page, FileInstance fileInstance, boolean recFlag){
        if (globalTable.bufferListContains(page, pageTable))
        {
            //update statistics
            if (!recFlag)
                increaseHitCounter();
            System.out.println("Page already in memory");
        }
        else if (globalFreeList.freeListContains(page)){
            if (fileInstance.addToLocalitySet(page, globalTable, pageTable))
                globalTable.addToGlobalTable(page, pageTable);
            if (!recFlag)
                increaseHitCounter();
            System.out.println("Page added to Global Table");
        }
        else{
            increaseMissCounter();
            globalFreeList.addToFreeList(page);
            System.out.println("Page added to Memory");
            requestPage(page, fileInstance, true);
        }

    }

    private static void blockNestedLoop(int loopCase){
        int outerBlocks = MyCommons.getDeptBlocks();
        int innerBlocks = 0;
        String outerPrefix = "O";
        String innerPrefix = "I";
        switch (loopCase){
            case 1:
                innerBlocks = MyCommons.getEmpBlocks();
                break;
            case 2:
                innerBlocks = MyCommons.getProjBlocks();
                break;
            default:
                System.out.println("No case found");
        }
        FileInstance outerInstance = new FileInstance(1);
        FileInstance innerInstance = new FileInstance(innerBlocks);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < innerBlocks; j++) {
                String outerSuffix = Integer.toString(i+1);
                Page outerPage = new Page(outerPrefix.concat(outerSuffix), System.currentTimeMillis());
                System.out.print("Checking for : "+outerPage.getPage());
                System.out.println();
                requestPage(outerPage, outerInstance, false);
                String innerSuffix = Integer.toString(j+1);
                Page innerPage = new Page(innerPrefix.concat(innerSuffix), System.currentTimeMillis());
                System.out.print(" "+innerPage.getPage());
                System.out.println();
                requestPage(innerPage, innerInstance, false);
            }
            System.out.println("----");
        }
        System.out.println("FIN");
        System.out.println("Page faults: "+missCounter);
        System.out.println("Page hits: "+hitCounter);
    }

    public static void main(String[] args) {
        globalTable = new GlobalTable();
        globalFreeList = new GlobalFreeList();
        pageTable = new PageTable();
        hitCounter = 0;
        missCounter = 0;
        blockNestedLoop(1);
//        Page page1 = new Page("O1",126456);
//        Page page2 = new Page("O1",124567);
//        System.out.println(page1.equals(page2));
//        pageTable.addToPageTable(page1, 1);
//        System.out.println(pageTable.getPageTableIndex(page2));
//        MyCommons.setBufferSize(10);
    }

}
