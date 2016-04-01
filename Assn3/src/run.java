import Eviction.Replacement;
import MyCommons.MyCommons;
import Objects.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.SocketHandler;

/**
 * Created by shubham on 3/29/16.
 */
public class run {

    private static GlobalTable globalTable;
    private static GlobalFreeList globalFreeList;
    private static PageTable pageTable;
    private static int hitCounter;
    private static int missCounter;
    private static Buffer globalBuffer;
    private static int checkPageCtr = 0;

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
//            if (fileInstance.getUsedPages()>1000){
//                System.out.println("Exiting program, Page: "+page.getPage());
//                return;
//            }
//            System.out.println("Locality Set size: "+
//                    fileInstance.getUsedPages()+", Page: "+page.getPage());
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

    private static void checkPage(Page page){
        if(globalBuffer.getPageList().contains(page)){
            int ind = globalBuffer.getPageList().indexOf(page);
            globalBuffer.getPageList().set(ind, page);
            increaseHitCounter();
//            System.out.println("Hit page: "+page.getPage()+" timestamp: "+page.getTimestamp());
        }
        else{
            if (!globalBuffer.insertPage(page)){
                Page evictPage = Replacement.getPageLRU(globalBuffer.getPageList());
                globalBuffer.removePage(evictPage);
                System.out.println("Evicted page: "+evictPage.getPage());
                globalBuffer.insertPage(page);
                System.out.println("Page inserted after eviction: "+page.getPage());
            }
            else {
                System.out.println("Page inserted: "+page.getPage()+", bufferSize: "+globalBuffer.getPageList().size());
            }
            increaseMissCounter();
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

        for (int i = 0; i < outerBlocks; i++) {
            for (int j = 0; j < innerBlocks; j++) {
                String outerSuffix = Integer.toString(i+1);
                Page outerPage = new Page(outerPrefix.concat(outerSuffix), System.currentTimeMillis()+
                        2*(checkPageCtr));
//                System.out.println();
//                requestPage(outerPage, outerInstance, false);
                checkPage(outerPage);
                String innerSuffix = Integer.toString(j+1);
                Page innerPage = new Page(innerPrefix.concat(innerSuffix), System.currentTimeMillis()+
                        2*(checkPageCtr+1));
                System.out.print("Checking for : "+outerPage.getPage());
                System.out.print(" "+innerPage.getPage());
                System.out.println();
//                requestPage(innerPage, innerInstance, false);
                checkPage(innerPage);
                ++checkPageCtr;
            }
            System.out.println("----");
        }
        System.out.println("FIN");
        System.out.println("Page faults: "+missCounter);
        System.out.println("Page hits: "+hitCounter);
    }

    private static void singleLoopJoin(int loopCase){
        int outerBlocks = 0;
        int innerBlocks = 0;
        int indexLevels = 0;
        int localitySetSize = 0;
        String outerPrefix = "O";
        String innerPrefix = "I";
        Random random = new Random();
        switch (loopCase){
            case 1:
                outerBlocks = MyCommons.getProjBlocks();
                innerBlocks = MyCommons.getDeptBlocks();
                indexLevels = 2;
                localitySetSize = getLocalitySetSize(1);
                break;
            case 2:
                outerBlocks = MyCommons.getDeptBlocks();
                innerBlocks = MyCommons.getEmpBlocks();
                indexLevels = 3;
                localitySetSize = getLocalitySetSize(2);
                break;
            default:
                System.out.println("No case found");
        }
        System.out.println("Locality Set: "+localitySetSize);
        FileInstance outerInstance = new FileInstance(1);
        FileInstance innerInstance = new FileInstance(localitySetSize);

        Page rootPage = new Page("ROOT", System.currentTimeMillis());

        for (int i = 0; i < outerBlocks; i++) {
            String outerSuffix = Integer.toString(i+1);
            Page outerPage = new Page(outerPrefix.concat(outerSuffix), System.currentTimeMillis());
            System.out.print("Checking for : "+outerPage.getPage());
            requestPage(outerPage, outerInstance, false);

            System.out.print(", ROOT,");
            requestPage(rootPage, innerInstance, false);
            if(indexLevels > 2){
                String nodePrefix = "N";
                int nodeBlock = random.nextInt((innerBlocks) + 1);
                String nodeSuffix = Integer.toString(nodeBlock);
                Page nodePage = new Page(nodePrefix.concat(nodeSuffix), System.currentTimeMillis());
                System.out.print(" "+nodePage.getPage()+", ");
                requestPage(nodePage, innerInstance, false);
            }
            int randomBlock = random.nextInt((innerBlocks) + 1);
            String innerSuffix = Integer.toString(randomBlock);
            Page innerPage = new Page(innerPrefix.concat(innerSuffix), System.currentTimeMillis());
            System.out.print(innerPage.getPage());
            System.out.println();
            requestPage(innerPage, innerInstance, false);

            System.out.println("----");
        }
        System.out.println("FIN");
        System.out.println("Page faults: "+missCounter);
        System.out.println("Page hits: "+hitCounter);
    }

    public static int getLocalitySetSize(int lsCase){
        int ls = 1;
        ArrayList<Integer> BList = new ArrayList<>();
        ArrayList<Integer> KList = new ArrayList<>();
        switch (lsCase){
            case 1:
                BList.add(MyCommons.getYaosValue(MyCommons.getDeptBlocks(), MyCommons.getDeptRecords(), 1));
                BList.add(MyCommons.getYaosValue(MyCommons.getDeptBlocks(), MyCommons.getDeptRecords(),
                        MyCommons.getDeptBlocks()));
                KList.add(1);
                KList.add(MyCommons.getDeptBlocks());
                break;
            case 2:
                BList.add(MyCommons.getYaosValue(MyCommons.getEmpBlocks(), MyCommons.getEmpRecords(), 1));
                BList.add(MyCommons.getYaosValue(MyCommons.getEmpBlocks(), MyCommons.getEmpRecords(), 30));
                BList.add(MyCommons.getYaosValue(MyCommons.getEmpBlocks(), MyCommons.getEmpRecords(),
                        MyCommons.getEmpBlocks()));
                KList.add(1);
                KList.add(30);
                KList.add(MyCommons.getEmpBlocks());
                break;
            default:
                System.out.println("No case found");
        }
        int j = 0;
        float lhs = 0;
        for (int i = 0; i < BList.size(); i++) {
            float nLhs = (float) ((KList.get(i) - BList.get(i))/(BList.get(i)*1.0));
            if (nLhs > lhs){
                lhs = nLhs;
                j = i;
            }
        }
        int lsSize = 0;
        int sum = 0;
        for (int i = 0; i <= j; i++) {
            sum = sum + BList.get(i);
        }
        lsSize = sum + 2;
        return lsSize;
    }

    public static void main(String[] args) {
        globalTable = new GlobalTable();
        globalFreeList = new GlobalFreeList();
        pageTable = new PageTable();
        globalBuffer = new Buffer();
        hitCounter = 0;
        missCounter = 0;
        blockNestedLoop(1);
        System.out.println("checkPageCtr: "+checkPageCtr);
//        singleLoopJoin(2);
//        System.out.println("yao's value: "+ MyCommons.getYaosValue(MyCommons.getEmpBlocks(),
//                MyCommons.getEmpRecords(), 1000));
//        System.out.println("LS Size: "+getLocalitySetSize(2));
    }
}
