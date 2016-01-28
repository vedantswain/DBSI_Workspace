package LH.main;

import java.util.Random;

/**
 * Created by shubham on 1/25/16.
 */
public class LHLauncher {

    static LinearHash linearHash;

    public static void main(String[] args) {
//        for LH: Bucket Size = 10
        int bucketSize = 10;
        int bucketNum = 1;
        linearHash = new LinearHash(bucketSize, bucketNum);

        Random rand = new Random();

        int searchRecord = 0;
        int randInt;
        for (int i = 0; i < 100000; i++) {
            randInt = rand.nextInt(800000);
            searchRecord = randInt;
            System.out.println("Inserting: " + randInt + " | Record No. " + i);
            try {
                linearHash.insert(randInt);
            } catch (Exception e) {
                System.out.println("Inserting: " + randInt + " | Record No. " + i);
                e.printStackTrace();
                break;
            }
        }

//        linearHash.lhSecMem.printAllRecords();
        linearHash.lhSecMem.countBuckets();

        System.out.println();
        System.out.println("Record to be searched:" + searchRecord);
        linearHash.search(searchRecord);

        System.out.println();
//        System.out.println("Number of records: "+CommonUtils.getRecordNum()+",  Number of buckets: "+
//                CommonUtils.getBucketNum()+", Size of the Bucket:"+CommonUtils.getBucketSize());
//        System.out.println("Number of successful search bucket: "+CommonUtils.getSuccessSearchBucketNum()+
//                ", Number of successful searches: "+CommonUtils.getSuccessSearchNum());
        System.out.println("Storage Utilization:"+CostMetrics.getStorageUtil());
        System.out.println("Average Successful Search Cost:"+CostMetrics.getAvgSuccessCost());
        System.out.println("Splitting Cost:"+CostMetrics.getSplitingCost());
    }
}