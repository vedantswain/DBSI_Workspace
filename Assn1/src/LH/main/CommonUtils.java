package LH.main;

/**
 * Created by shubham on 1/22/16.
 */
public class CommonUtils {

    private static int bucketSize;
    private static int initNumBuckets;
    private static int recordNum;
    private static int bucketNum;
    private static int successSearchNum;
    private static int successSearchBucketNum;
    private static int splitCost;

    public static void addSplitCost(int val){
        CommonUtils.splitCost += val;
    }

    public static void setSplitCost(int splitCost) {
        CommonUtils.splitCost = splitCost;
    }

    public static int getSplitCost() {
        return splitCost;
    }

    public static void setSuccessSearchBucketNum(int successSearchBucketNum) {
        CommonUtils.successSearchBucketNum = successSearchBucketNum;
    }

    public static int getSuccessSearchBucketNum() {
        return successSearchBucketNum;
    }

    public static void setSuccessSearchNum(int successBucketSearch) {
        CommonUtils.successSearchNum = successBucketSearch;
    }

    public static int getSuccessSearchNum() {
        return successSearchNum;
    }

    public static void setBucketNum(int bucketNum) {
        CommonUtils.bucketNum = bucketNum;
    }

    public static int getBucketNum() {
        return bucketNum;
    }

    public static void setRecordNum(int recordNum) {
        CommonUtils.recordNum = recordNum;
    }

    public static int getRecordNum() {
            return recordNum;
    }

    public static void setInitNumBuckets(int initNumBuckets) {
        CommonUtils.initNumBuckets = initNumBuckets;
    }

    public static int getInitNumBuckets() {
        return initNumBuckets;
    }

    public static void setBucketSize(int bucketSize) {
        CommonUtils.bucketSize = bucketSize;
    }

    public static int getBucketSize() {
        return bucketSize;
    }
}
