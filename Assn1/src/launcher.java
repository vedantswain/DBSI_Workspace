import EH.CommonUtils;
import EH.ExtHash;
import LH.main.CostMetrics;
import LH.main.LinearHash;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by vedantdasswain on 19/01/16.
 */
public class launcher {

    static int buckSize=10;
    static LinearHash linHash;

    public static void generateData(){
        int[] dataSet1 = new int[100000];
        int[] dataSet2 = new int[100000];

        Random rand=new Random();

        //Data Set 1
        for(int i=0;i<dataSet1.length;i++){
            int randint=rand.nextInt(800000);
            dataSet1[i] = randint;
        }

        writeDataSet(dataSet1,1);

        //Data Set 2
        for (int i = 0; i < 60000; i++) {
            int randint=rand.nextInt((800000 - 700000) + 1) + 700000;
            dataSet2[i] = randint;
        }
        for (int i = 0; i < 40000; i++) {
            int randint=rand.nextInt(700000);
            dataSet2[i+60000] = randint;
        }

        writeDataSet(dataSet2,2);
    }

    public static void writeDataSet(int[] dataSet,int setNo){
        String filename="dataset"+setNo+".txt";
        try {
            FileWriter fw=new FileWriter(filename);
            for(int i:dataSet){
                fw.append(i+"\n");
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[] readData(int setNo){
        String filename="dataset"+setNo+".txt";
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            ArrayList<Integer> lines = new ArrayList<Integer>();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(Integer.valueOf(line));
            }
            bufferedReader.close();

            int dataSet[]=new int[lines.size()];
            for(int i=0;i<lines.size();i++){
                dataSet[i]=lines.get(i);
            }
            return dataSet;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new int[0];
    }

    public static void main(String[] args){
//          generateData();

          int[] dataSet1=readData(1);
          int[] dataSet2=readData(2);

//        System.out.println("DataSet 1:");
//        for (int ele: dataSet1) {
//            System.out.println(ele);
//        }
//        System.out.println();
//        System.out.println("DataSet 2:");
//        for (int ele: dataSet2) {
//            System.out.println(ele);
//        }

        //Following lines must be run one-at-a-time to avoid synchronisation issues
//        execute(dataSet1,1,10);
//        execute(dataSet1,1,40);
//        execute(dataSet2,2,10);
        execute(dataSet2,2,40);
    }


    public static void search(int[] dataSet, int multiplier,FileWriter ehSearchFW, FileWriter lhSearchFW){
        Random rand=new Random();
        int[] searchElements = new int[50];
        int[] searchElementIndices = new int[50];

        LH.main.CommonUtils.setSuccessSearchNum(0);
        LH.main.CommonUtils.setSuccessSearchBucketNum(0);

        try {
            for (int j = 0; j < 50; j++) {
                int randIndex = rand.nextInt((5000 * (multiplier + 1)) - (5000 * (multiplier))) + (5000 * (multiplier));
                searchElements[j] = dataSet[randIndex];
                searchElementIndices[j] = randIndex;
            }
            ++multiplier;

            int ehHits = 0;
            int ehSearchCost = 0;

//        System.out.println("Elements of searchElements: ");
            for (int j = 0; j < searchElements.length; j++) {
//            System.out.println("element: "+searchElements[j]+", index: "+searchElementIndices[j]+
//                    ", multiplier: "+multiplier);
                linHash.search(searchElements[j]);

                int ehSearchVal = ExtHash.searchVal(searchElements[j]);
                ehSearchCost += ehSearchVal;
                if (ehSearchVal > 0) {
                    ehHits++;
                }
            }

            double ehAveSearch=ehSearchCost/(ehHits*1.0);
            ehSearchFW.append(ehAveSearch+"\n");
            lhSearchFW.append(CostMetrics.getAvgSuccessCost()+"\n");
//        System.out.println("EH Average Search Cost: "+(ehSearchCost/ehHits));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void execute(int[] dataSet, int setNo,int buckSize){
        int multiplier = 0;
        CommonUtils.setBuckLength(buckSize);
        linHash=new LinearHash(buckSize,1);

        String ehFileName="EH_Set"+setNo+"_Buck"+buckSize+".csv";
        String HEADER="Split Cost"+","+"Storage Use"+"\n";
        String ehSearchFileName="EH_Set"+setNo+"_Buck"+buckSize+"_search"+".csv";
        String SEARCH_HEADER="Search Cost"+"\n";

        String lhFileName="LH_Set"+setNo+"_Buck"+buckSize+".csv";
        String lhSearchFileName="LH_Set"+setNo+"_Buck"+buckSize+"_search"+".csv";

        try {
            FileWriter ehFW=new FileWriter(ehFileName);
            ehFW.append(HEADER);
            FileWriter ehSearchFW = new FileWriter(ehSearchFileName);
            ehSearchFW.append(SEARCH_HEADER);

            FileWriter lhFW=new FileWriter(lhFileName);
            lhFW.append(HEADER);
            FileWriter lhSearchFW = new FileWriter(lhSearchFileName);
            lhSearchFW.append(SEARCH_HEADER);

            for (int i = 0; i < dataSet.length; i++) {
                int ehSplitCost=ExtHash.insert(dataSet[i]);
                int ehNumBucks=ExtHash.getBucketCount();
                double ehStoreUse=(i+1)/(ehNumBucks*buckSize*1.0);

                int lhSplitCost=linHash.insert(dataSet[i]);
                double lhStoreUse= (double) CostMetrics.getStorageUtil();

    //            System.out.println("EH Splitting cost: "+ehSplitCost);
                if (i>0 && i % 5000 == 0){
    //                System.out.println("EH Storage Utilisation: "+ehStoreUse);
                    search(dataSet,multiplier,ehSearchFW,lhSearchFW);
                }

                ehFW.append(ehSplitCost+","+ehStoreUse+"\n");
                lhFW.append(lhSplitCost+","+lhStoreUse+"\n");
            }

            ehFW.close();
            ehSearchFW.close();
            lhFW.close();
            lhSearchFW.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        System.out.println("DataSet: "+setNo+", BuckSize: "+buckSize+", Fin");
    }

}