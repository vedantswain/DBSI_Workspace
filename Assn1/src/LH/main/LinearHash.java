package LH.main;

import LH.objects.LHSecMem;

/**
 * Created by shubham on 1/22/16.
 */
public class LinearHash {

    LHSecMem lhSecMem;

    public LinearHash(int buckSize, int buckNum) {
        lhSecMem = new LHSecMem();
        CommonUtils.setBucketSize(buckSize);
        CommonUtils.setInitNumBuckets(buckNum);
        CommonUtils.setBucketNum(0);
        CommonUtils.setSuccessSearchNum(0);
        CommonUtils.setSplitCost(0);
    }

    public void insert(int val) {
        lhSecMem.insertRecordInMem(val, true);
    }

    public void search(int val) {
        int searchIndex = lhSecMem.searchRecordInMem(val);
        if (searchIndex != -1) {
            System.out.println("Found record at index: " + searchIndex);
            CommonUtils.setSuccessSearchNum(CommonUtils.getSuccessSearchNum()+1);
        } else
            System.out.println("No such record was found");
    }
}
