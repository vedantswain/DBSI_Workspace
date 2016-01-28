import EH.CommonUtils;
import EH.ExtHash;

import java.util.Random;

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
        ExtHash.insert(1);
        ExtHash.insert(1);

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

//        System.out.println(ExtHash.searchVal(0));

//        secMem.printMap();

        int[] dataSet1 = new int[100000];
        int[] dataSet2 = new int[100000];

        Random rand=new Random();

        for(int i=0;i<dataSet1.length;i++){
            int randint=rand.nextInt(800000);
            dataSet1[i] = randint;
        }

        for (int i = 0; i < 60000; i++) {
            int randint=rand.nextInt((800000 - 700000) + 1) + 700000;
            dataSet2[i] = randint;
        }

        for (int i = 0; i < 40000; i++) {
            int randint=rand.nextInt(700000);
            dataSet2[i+60000] = randint;
        }

        System.out.println("DataSet 1:");
        for (int ele: dataSet1) {
            System.out.println(ele);
        }
//        System.out.println();
//        System.out.println("DataSet 2:");
//        for (int ele: dataSet2) {
//            System.out.println(ele);
//        }
        execute(dataSet1);
    }

    public static void execute(int[] dataSet){
        Random rand=new Random();
        int multiplier = 0;
        int[] searchElements;
        int[] searchElementIndices;
        for (int i = 0; i < dataSet.length; i++) {
            if (i % 5000 == 0){
                searchElements = new int[50];
                searchElementIndices = new int[50];
                for (int j = 0; j < 50; j++) {
                    int randIndex = rand.nextInt((5000*(multiplier+1)) - (5000*(multiplier))) + (5000*(multiplier));
                    searchElements[j] = dataSet[randIndex];
                    searchElementIndices[j] = randIndex;
                }
                ++multiplier;
                System.out.println("Elements of searchElements: ");
                for (int j = 0; j < searchElements.length; j++) {
                    System.out.println("element: "+searchElements[j]+", index: "+searchElementIndices[j]+
                            ", multiplier: "+multiplier);
                }
            }
        }
    }
}
