package EH.objects;

import EH.CommonUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by vedantdasswain on 19/01/16.
 */
public class Bucket {
    int[] records;
    int localDepth;
    int prevLD;
    int index;
    ArrayList<Bucket> overflowBucks;

    public Bucket(int localDepth){
        records=new int[CommonUtils.buckLength];
        initRecords();
        this.localDepth=localDepth;
        index=0;
        overflowBucks=new ArrayList<Bucket>();
    }

    public int getCount(){
        return 1+overflowBucks.size();
    }

    public void initRecords(){
        for(int i=0;i<records.length;i++){
            records[i]=-1;
        }
    }

    public int getLocalDepth() {
        return localDepth;
    }

    public void setLocalDepth(int localDepth) {
        prevLD=this.localDepth;
        this.localDepth = localDepth;
    }

    public int[] concat(int[] a, int[] b) {
        int aLen = a.length;
        int bLen = b.length;
        int[] c= new int[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }

    public int[] getRecords() {
        return records;
    }

    public int getIndex() {
        return index;
    }

    public int[] popBuck(){
        int[] finalRecords= Arrays.copyOf(records,records.length);
//        for(int i=0;i<overflowBucks.size();i++)
//            finalRecords=concat(finalRecords,overflowBucks.get(i).popBuck());

        while(true) {
            if(overflowBucks.size()<1)
                break;
            finalRecords = concat(finalRecords, overflowBucks.get(0).popBuck());
            overflowBucks.remove(0);
        }


        index=0;
        for(int i=0;i<CommonUtils.buckLength;i++){
            records[i]=-1;
        }
        this.index=0;
//        System.out.println("Values in collision bucket:");
        for(int i=0;i<finalRecords.length;i++){
            System.out.print(finalRecords[i]+", ");
        }
        System.out.println();

        return finalRecords;
    }

    public boolean isEmpty(){
        if(records[0]==-1)
            return true;
        return false;
    }


    public void chainRecord(int record){
        System.out.println("Chaining: "+record);
        if((!overflowBucks.isEmpty()) && (overflowBucks.get(overflowBucks.size()-1).getIndex()<CommonUtils.buckLength)){
            overflowBucks.get(overflowBucks.size()-1).insertRecord(record);
        }
        else{
            Bucket ofBuck=new Bucket(this.localDepth);
            ofBuck.insertRecord(record);
            overflowBucks.add(ofBuck);
        }
        this.index=overflowBucks.get(overflowBucks.size()-1).getIndex();
    }

    public void insertRecord(int record){
        if(overflowBucks.size()>0){
            overflowBucks.get(overflowBucks.size()-1).insertRecord(record);
            index=overflowBucks.get(overflowBucks.size()-1).getIndex();
        }
        else {
            records[index] = record;
            index++;
        }
//        System.out.println("Inserted: "+record);

    }

    public int searchRecord(int record){
        int searchCost=1;
        for(int i=0;i<this.records.length;i++){
//            System.out.println("checking record: "+this.records[i]);
            if(this.records[i]==record) {
                return searchCost;
            }
        }

        if(overflowBucks.size()>0){
            for (int i=0;i<overflowBucks.size();i++){
                Bucket ofBuck=overflowBucks.get(i);
                searchCost++;
//                System.out.println("checking overflow bucket: "+i);
                for(int j=0;j<ofBuck.getRecords().length;j++){
//                    System.out.println("checking record: "+ofBuck.getRecords()[j]);
                    if(ofBuck.getRecords()[j]==record)
                        return searchCost;
                }
            }
        }
        return 0;
    }
}
