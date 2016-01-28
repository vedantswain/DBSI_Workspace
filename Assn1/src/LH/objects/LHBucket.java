package LH.objects;

import LH.main.CommonUtils;

import java.util.ArrayList;

/**
 * Created by shubham on 1/22/16.
 */
public class LHBucket {
    int bucketIndex;
    int[] records;
    boolean isOverFlow;
    ArrayList<LHBucket> overflowList;

    public LHBucket(){
        bucketIndex = 0;
        records = new int[CommonUtils.getBucketSize()];
        isOverFlow = false;
        overflowList = new ArrayList<>();
    }

    public void insertRecordInBucket(int record){
        if (overflowList.isEmpty()) {
            records[bucketIndex] = record;
            ++bucketIndex;
        }
        else {
            LHBucket insertBucket = overflowList.get(overflowList.size()-1);
            insertBucket.records[insertBucket.bucketIndex] = record;
            ++insertBucket.bucketIndex;
        }
//        printRecords();
    }

    public boolean isFull(){
        if (overflowList.isEmpty())
            return (bucketIndex >=CommonUtils.getBucketSize());
        else{
            return overflowList.get(overflowList.size()-1).bucketIndex >= CommonUtils.getBucketSize();
        }
    }

    public void printRecords(){
        for (int i = 0; i<this.bucketIndex; ++i){
            System.out.println(this.records[i]);
        }
        if (!this.overflowList.isEmpty()){
            for (LHBucket lhBucket: overflowList) {
                System.out.print("-->");
                lhBucket.printRecords();
            }
        }
    }

//    public void count(){
//        CommonUtils.setBucketNum(CommonUtils.getBucketNum()+1);
//        if (!this.overflowList.isEmpty()){
//            CommonUtils.setBucketNum(CommonUtils.getBucketNum()+overflowList.size());
//        }
//    }

    public void addOverFlow(int record){
        if (overflowList.isEmpty() || overflowList.get(overflowList.size()-1).isFull()){
            overflowList.add(new LHBucket());
            CommonUtils.setBucketNum(CommonUtils.getBucketNum()+1);
        }
        overflowList.get(overflowList.size()-1).insertRecordInBucket(record);
    }

    private ArrayList<Integer> popRecords(LHBucket buck){
        ArrayList<Integer> finRecList = new ArrayList<>();
        for (int i = 0; i < buck.bucketIndex; i++) {
            finRecList.add(buck.records[i]);
        }
        return finRecList;
    }

    public int[] popAllBucks(){
        ArrayList<Integer> finalRecordsList = new ArrayList<>();
        finalRecordsList.addAll(popRecords(this));

        if (!overflowList.isEmpty())
        {
            for (LHBucket buck: overflowList) {
                finalRecordsList.addAll(popRecords(buck));
            }
        }

        this.bucketIndex=0;
        overflowList.clear();

        int[] finalRecords = new int[finalRecordsList.size()];
        for (int i = 0; i < finalRecordsList.size(); i++) {
            finalRecords[i]=finalRecordsList.get(i);
        }

        return finalRecords;
    }
}
