import EH.CommonUtils;
import EH.ExtHash;

/**
 * Created by vedantdasswain on 19/01/16.
 */
public class launcher {

    public static void main(String[] args){
        CommonUtils.setBuckLength(2);
        ExtHash.insert(2);
        ExtHash.insert(2);
        ExtHash.insert(3);
        ExtHash.insert(1);
        ExtHash.insert(1);

        ExtHash.printBAT();
//        secMem.printMap();
//
//        System.out.println("Total Buckets: "+getBucketCount());

//        System.out.println(searchVal(2));
//        System.out.println(searchVal(3));
//        System.out.println(searchVal(1));
//        System.out.println(searchVal(0));

//        secMem.printMap();

    }
}
