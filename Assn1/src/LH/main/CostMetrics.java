package LH.main;

/**
 * Created by shubham on 1/25/16.
 */
public class CostMetrics {

    public static float getAvgSuccessCost(){
        return ((float)CommonUtils.getSuccessSearchBucketNum()/(float)CommonUtils.getSuccessSearchNum());
    }

    public static float getStorageUtil(){
        return ((float)CommonUtils.getRecordNum()/(float)(CommonUtils.getBucketNum()*CommonUtils.getBucketSize()));
    }

    public static float getSplitingCost(){
        return ((float)CommonUtils.getSplitCost());
    }

}
