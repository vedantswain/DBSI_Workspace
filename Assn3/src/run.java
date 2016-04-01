import Eviction.Replacement;
import MyCommons.MyCommons;
import Objects.*;

import java.util.ArrayList;
import java.util.Random;

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
    private static int[] bufferSizeArray;

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
//            System.out.println("Page already in memory");
        }
        else if (globalFreeList.freeListContains(page)){
            if (fileInstance.addToLocalitySet(page, globalTable, pageTable))
                globalTable.addToGlobalTable(page, pageTable);
            if (!recFlag)
                increaseHitCounter();
//            System.out.println("Page added to Global Table");
        }
        else{
            increaseMissCounter();
            globalFreeList.addToFreeList(page);
//            System.out.println("Page added to Memory");
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
                Page evictPage = Replacement.getPageMRU(globalBuffer.getPageList());
                globalBuffer.removePage(evictPage);
//                System.out.println("Evicted page: "+evictPage.getPage());
                globalBuffer.insertPage(page);
//                System.out.println("Page inserted after eviction: "+page.getPage());
            }
            else {
//                System.out.println("Page inserted: "+page.getPage()+", bufferSize: "+globalBuffer.getPageList().size());
            }
            increaseMissCounter();
        }
    }

    private static void blockNestedLoop(int loopCase, String algoCase){
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
                if (algoCase.equals("DBMIN"))
                    requestPage(outerPage, outerInstance, false);
                else
                    checkPage(outerPage);
                String innerSuffix = Integer.toString(j+1);
                Page innerPage = new Page(innerPrefix.concat(innerSuffix), System.currentTimeMillis()+
                        2*(checkPageCtr+1));
                if (algoCase.equals("DBMIN"))
                    requestPage(innerPage, innerInstance, false);
                else
                    checkPage(innerPage);
                System.out.print(outerPage.getPage());
                System.out.print(" "+innerPage.getPage());
                System.out.println();
                ++checkPageCtr;
            }
            System.out.println("----");
        }
        System.out.println("RESULTS: ");
        System.out.println("Page faults: "+missCounter);
        System.out.println("Page hits: "+hitCounter);
    }

    private static void singleLoopJoin(int loopCase, String algoCase){
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
//        System.out.println("Locality Set: "+localitySetSize);
        FileInstance outerInstance = new FileInstance(1);
        FileInstance innerInstance = new FileInstance(localitySetSize);

//        Page rootPage = new Page("ROOT", System.currentTimeMillis());
        Page nodePage = null;

        for (int i = 0; i < outerBlocks; i++) {
            String outerSuffix = Integer.toString(i+1);
            Page outerPage = new Page(outerPrefix.concat(outerSuffix), System.currentTimeMillis());
            if (algoCase.equals("DBMIN")) {
                requestPage(outerPage, outerInstance, false);
            }
            else {
                checkPage(outerPage);
            }

            //checking for Root
            Page rootPage = new Page("ROOT", System.currentTimeMillis()+ 3*(checkPageCtr));
            if (algoCase.equals("DBMIN")) {
                requestPage(rootPage, innerInstance, false);
            }
            else {
                checkPage(rootPage);
            }

            if(indexLevels > 2){
                String nodePrefix = "N";
                int nodeBlock = random.nextInt((innerBlocks) + 1);
                String nodeSuffix = Integer.toString(nodeBlock);
                nodePage = new Page(nodePrefix.concat(nodeSuffix), System.currentTimeMillis()+
                        3*(checkPageCtr+1));

                if (algoCase.equals("DBMIN")) {
                    requestPage(nodePage, innerInstance, false);
                }
                else {
                    checkPage(nodePage);
                }
            }
            int randomBlock = random.nextInt((innerBlocks) + 1);
            String innerSuffix = Integer.toString(randomBlock);
            Page innerPage = new Page(innerPrefix.concat(innerSuffix), System.currentTimeMillis()+
                    3*(checkPageCtr+2));
            if (algoCase.equals("DBMIN")) {
                requestPage(innerPage, innerInstance, false);
            }
            else {
                checkPage(innerPage);
            }
            System.out.print(outerPage.getPage());
            System.out.print(", ROOT, ");
            if(indexLevels > 2)
                System.out.print(" "+nodePage.getPage()+", ");
            System.out.print(innerPage.getPage());
            System.out.println();
            System.out.println("----");
            ++checkPageCtr;
        }
        System.out.println("RESULTS: ");
        System.out.println("Page faults: "+missCounter);
        System.out.println("Page hits: "+hitCounter);
        switch (loopCase){
            case 1:
                checkPageCtr = checkPageCtr*3;
                break;
            case 2:
                checkPageCtr = checkPageCtr*4;
                break;
        }
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
        bufferSizeArray = new int[]{50, 100, 200, 400, 600, 800, 1000, 1200};
//        blockNestedLoop(1,"");
//        singleLoopJoin(2,"DBMIN");
        for (int i = 0; i < bufferSizeArray.length; i++) {
            hitCounter = 0;
            missCounter = 0;
            checkPageCtr = 0;
            MyCommons.setBufferSize(bufferSizeArray[i]);
            globalBuffer = new Buffer();
//            blockNestedLoop(2,"");
            singleLoopJoin(2,"");
            System.out.println("Page Accesses: "+checkPageCtr+", Buffer Size: "+globalBuffer.getPageList().size());
            System.out.println("======");
            System.out.println();
        }
    }
}
