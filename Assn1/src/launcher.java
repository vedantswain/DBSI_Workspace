import EH.CommonUtils;
import EH.ExtHash;

/**
 * Created by vedantdasswain on 19/01/16.
 */
public class launcher {

    public static void main(String[] args){
        CommonUtils.setBuckLength(2);
//        CommonUtils.setBuckLength(10);


//        System.out.println(ExtHash.getMSB(2));
//        System.out.println(ExtHash.getMSB(1));
//        System.out.println(ExtHash.getMSB(3));
//        System.out.println(ExtHash.getMSB(5));

        ExtHash.insert(7);
        ExtHash.insert(0);
        ExtHash.insert(4);
        ExtHash.insert(5);
        ExtHash.insert(5);
        ExtHash.insert(3);
//        ExtHash.insert(1);
//        ExtHash.insert(1);

//        Random rand=new Random();
//
//        for(int i=0;i<100000;i++){
//            int randint=rand.nextInt(800000);
////            System.out.println("Inserting: "+randint+" | Record No. "+i);
//            try {
//                ExtHash.insert(randint);
//            }
//            catch (Exception e){
//                System.out.println("Inserting: "+randint+" | Record No. "+i);
//                e.printStackTrace();
//                break;
//            }
//        }

//        System.out.println(new BigInteger("10010011110000110111"));

        ExtHash.printBAT();
//        secMem.printMap();
//
//        System.out.println("Total Buckets: "+getBucketCount());

        System.out.println(ExtHash.searchVal(0));

//        secMem.printMap();

    }
}
