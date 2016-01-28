package LH.objects;

import LH.main.CommonUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by shubham on 1/21/16.
 */
public class LHSecMem {

    int hashExp;
    HashMap<Integer,LHBucket> bucketMap;
    int nextToSplit;
    HashSet<Integer> splitAccessNumSet;

    public LHSecMem(){
        hashExp=0;
        nextToSplit = 0;
        bucketMap = new HashMap<>();
        CommonUtils.setRecordNum(0);
        splitAccessNumSet = new HashSet<>();
    }

    public void split(){
//        System.out.println("Bucket to be split: "+nextToSplit);
        if (bucketMap.get(nextToSplit) == null)
        {
            ++nextToSplit;
            return;
        }

//        CommonUtils.addSplitCost(1);
        int[] poppedRecords = bucketMap.get(nextToSplit).popAllBucks();
//        System.out.println("Split Cost to be added: "+(int) (Math.ceil((float)poppedRecords.length / (float)CommonUtils.getBucketSize())));
//        CommonUtils.addSplitCost((int) (Math.ceil((float)poppedRecords.length / (float)CommonUtils.getBucketSize())));
        CommonUtils.setSplitCost(CommonUtils.getSplitCost()+(int)
                (Math.ceil((float)poppedRecords.length / (float)CommonUtils.getBucketSize())));
        bucketMap.remove(nextToSplit);
        ++nextToSplit;

        if (nextToSplit>=getRoundNum()){
            ++hashExp;
            nextToSplit=0;
        }
//        System.out.println("nextToSplit="+nextToSplit+", Round Num: "+getRoundNum());

//        //Printing records
//        System.out.println("Popped records:");
//        for (int poppedRec: poppedRecords) {
//            System.out.print(poppedRec+" => ");
//        }
//        System.out.println();

        for (int poppedRec: poppedRecords) {
            insertRecordInMem(poppedRec, false);
        }
    }

    /**
     *
     * @param record: record to be inserted
     * @param flag: true for new record; to call split on the current method call or not
     */
    public int insertRecordInMem(int record, boolean flag){

        int bucketAddr = primaryHash(record);
        if (bucketAddr < nextToSplit){
            bucketAddr = secondaryHash(record);
//            System.out.println("hashExp: "+hashExp+", Secondary Hash used: "
//                    +record+" MOD "+Math.pow(2,hashExp+1));
        }
//        System.out.println("bucketAddr: "+bucketAddr+", val: "+record);

        if (bucketMap.get(bucketAddr) == null) {
            //if there is no bucket
            bucketMap.put(bucketAddr,new LHBucket());
            bucketMap.get(bucketAddr).insertRecordInBucket(record);
        }
        else if (bucketMap.get(bucketAddr).isFull()){
//            System.out.println("Add to overflow");
            bucketMap.get(bucketAddr).addOverFlow(record);
            if (flag) {
                CommonUtils.setSplitCost(1);
                split();
            }
        }
        else {
            //Bucket exists and is not full
            bucketMap.get(bucketAddr).insertRecordInBucket(record);
        }

        if (flag){
            int returnVal = 0;
            CommonUtils.setRecordNum(CommonUtils.getRecordNum()+1);
            if (!splitAccessNumSet.isEmpty()){
//                System.out.println("Size of splitAccessSet: "+splitAccessNumSet.size());
//                CommonUtils.addSplitCost(splitAccessNumSet.size());
                CommonUtils.setSplitCost(CommonUtils.getSplitCost()+splitAccessNumSet.size());
                returnVal = CommonUtils.getSplitCost();
            }
            splitAccessNumSet = new HashSet<>();
            CommonUtils.setSplitCost(0);
            return returnVal;
        }
        else {
//            System.out.println("nextToSplit: "+nextToSplit+", bucketAddr: "+bucketAddr+", val: "+record);
            if (bucketAddr!=nextToSplit)
                splitAccessNumSet.add(bucketAddr);
            return -1;
        }

//        printAllRecords();
    }

    public int searchRecordInMem(int record){
        int successSearchBucketNum;
        int bucketAddr = primaryHash(record);
        if (bucketAddr < nextToSplit){
            bucketAddr = secondaryHash(record);
        }

        int[] poppedRecords = bucketMap.get(bucketAddr).popAllBucks();
        successSearchBucketNum = (int) (Math.ceil((float)poppedRecords.length / (float)CommonUtils.getBucketSize()));
//        System.out.println("successSearchBucketNum: "+successSearchBucketNum);
        boolean flag = false;
        for (int rec: poppedRecords) {
            if (rec == record){
                flag = true;
                break;
            }
        }

        if (flag){
            CommonUtils.setSuccessSearchBucketNum(CommonUtils.getSuccessSearchBucketNum()+successSearchBucketNum);
            return bucketAddr;
        }
        else
            return -1;
    }

    public void printAllRecords(){
        Set<Integer> keySet = bucketMap.keySet();
        for (int key: keySet) {
            System.out.println("Key: "+key+", Records:");
            bucketMap.get(key).printRecords();
        }
        System.out.println("Number of total records inserted: " + CommonUtils.getRecordNum());
    }

    public void countBuckets(){
        Set<Integer> keySet = bucketMap.keySet();
        for (int key: keySet) {
            bucketMap.get(key).count();
        }
    }

    public int getRoundNum(){
        return (int)(Math.pow(2,hashExp)* CommonUtils.getInitNumBuckets());
    }

    public int primaryHash(int key){
        return (key % (int)(Math.pow(2,hashExp)* CommonUtils.getInitNumBuckets()));
    }

    public int secondaryHash(int key){
        return (key % (int)(Math.pow(2,hashExp+1)* CommonUtils.getInitNumBuckets()));
    }

}
