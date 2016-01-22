package main;

import objects.Bucket;
import objects.MainMem;
import objects.SecMem;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by vedantdasswain on 19/01/16.
 */
public class launcher {

    static MainMem mainMem=new MainMem();
    static SecMem secMem=new SecMem();

    public static int getMSB(int val){
        BigInteger v = BigInteger.valueOf(val);
        BigInteger msbV;
        if(v.bitLength() - mainMem.getGlobalDepth()>0) {
            msbV = v.shiftRight(v.bitLength() - mainMem.getGlobalDepth());
        }
        else {
            msbV = v;
        }
//        System.out.println("For Val: "+val+" Index: "+msbV.intValue());
        return msbV.intValue();
    }

    public static String getPrefix(int val,int depth){
        BigInteger v = BigInteger.valueOf(val);
        String byteString = String.format("%0"+mainMem.getGlobalDepth()+"d",Integer.parseInt(v.toString(2)));
        String prefix=byteString.substring(0,depth);
        return prefix;
    }


    public static void redist(Bucket usedBuck,int val,boolean splitOnce){
        mainMem.setGlobalDepth(mainMem.getGlobalDepth()+1);
        System.out.println("GD: "+mainMem.getGlobalDepth());
        if(mainMem.getIndex()>=CommonUtils.dirSize) {
            secMem.resizeBAT(mainMem.getGlobalDepth());
        }
        int[] oldRecords=usedBuck.popBuck();
        for(int i=0;i<oldRecords.length;i++){
//            System.out.println("Inserting Old: "+oldRecords[i]);
            insertVal(oldRecords[i],splitOnce);
        }
        insertVal(val,splitOnce);
    }


    public static void repoint(){
        int[] BAT=mainMem.getBuckAddressTable();
        ArrayList secBAT=secMem.getBuckAddressTable();
        //Traverses BAT through MainMem
        for(int i=0;i<mainMem.getIndex();i++){
            int ld=secMem.getBucketAt(BAT[i]).getLocalDepth();
            String prefix=getPrefix(i,ld);
//            System.out.println("Checking for "+prefix);
            for(int j=0;j<mainMem.getIndex();j++){
                Bucket checkBuck=secMem.getBucketAt(BAT[j]);
                if(checkBuck==null || checkBuck.isEmpty()){
                    if(prefix.equals(getPrefix(j,ld))){
                        secMem.deleteBucketAt(BAT[j]);
                        BAT[j]=BAT[i];
                        System.out.println("Repointing: Main "+j+" ->"+" Main "+i);
                    }
                }
            }

            for(int j=0;j<secMem.getIndex();j++){
                Bucket checkBuck=secMem.getBucketAt((int)secBAT.get(j));
                if(checkBuck==null || checkBuck.isEmpty()){
                    if(prefix.equals(getPrefix(j,ld))){
                        secMem.deleteBucketAt((int)secBAT.get(j));
                        secBAT.add(j,BAT[i]);
                        System.out.println("Repointing: Sec "+(CommonUtils.dirSize+j)+" ->"+" Main "+i);
                    }
                }
            }
        }

        //Traverses BAT through SecMem
        for(int i=0;i<secMem.getIndex();i++) {
            int ld = secMem.getBucketAt((int) secBAT.get(i)).getLocalDepth();
            String prefix = getPrefix(CommonUtils.dirSize+i, ld);
//            System.out.println("Checking for "+prefix);
            for (int j = 0; j < mainMem.getIndex(); j++) {
                Bucket checkBuck = secMem.getBucketAt(BAT[j]);
                if (checkBuck == null || checkBuck.isEmpty()) {
                    if (prefix.equals(getPrefix(j, ld))) {
                        secMem.deleteBucketAt(BAT[j]);
                        BAT[j] = (int) secBAT.get(i);
                        System.out.println("Repointing: Main "+j+" ->"+" Sec "+(CommonUtils.dirSize+i));
                    }
                }
            }

            for (int j = 0; j < secMem.getIndex(); j++) {
                Bucket checkBuck = secMem.getBucketAt((int) secBAT.get(j));
                if (checkBuck == null || checkBuck.isEmpty()) {
                    if (prefix.equals(getPrefix(j, ld))) {
                        secMem.deleteBucketAt((int) secBAT.get(j));
                        secBAT.add(j, (int) secBAT.get(i));
                        System.out.println("Repointing: Sec "+(CommonUtils.dirSize+j)+" ->"+" Sec "+(CommonUtils.dirSize+i));
                    }
                }
            }
        }
    }

    public static void split(Bucket buck,int val){
        System.out.println("Splitting");
        buck.setLocalDepth(buck.getLocalDepth()+1);
        redist(buck,val,true);
        repoint();
    }

    public static void insertToSec(int val,int index,boolean splitOnce){
//        System.out.println("Inserting in Sec at Index: "+index);
        if(secMem.getBuckAddressTable()==null){
            secMem.initBAT(2*CommonUtils.dirSize);
        }
        if (secMem.getBucketAddressAt(index)!=-1){
            //bucket address exists
            int buckAddress=secMem.getBucketAddressAt(index);
            Bucket usedBuck=secMem.getBucketAt(buckAddress);
            if(usedBuck.getIndex()>=CommonUtils.buckLength){
                //splitting
                if(!splitOnce)
                    split(usedBuck,val);
                    //chain
                else
                    usedBuck.chainRecord(val);
            }
            else{
//                System.out.println("Space in bucket");
                usedBuck.insertRecord(val);
            }
        }
        else{
            //add new address here and add value to bucket
            Bucket newBuck=new Bucket(mainMem.getGlobalDepth());
            newBuck.insertRecord(val);
            int address=secMem.putBucket(index,newBuck);
            System.out.println("Inserting Address: "+address+" in Sec Mem at: "+index);
            secMem.insertAddress(address,index);
        }
    }

    public static void insertToMain(int val,int index,boolean splitOnce){
        if (mainMem.getBucketAddressAt(index)!=-1){
            //bucket address exists
            int buckAddress=mainMem.getBucketAddressAt(index);
            Bucket usedBuck=secMem.getBucketAt(buckAddress);
            if(usedBuck.getIndex()>=CommonUtils.buckLength){
                //splitting
                if(!splitOnce)
                    split(usedBuck,val);
                else    //chain
                    usedBuck.chainRecord(val);
            }
            else{
//                System.out.println("Space in bucket");
                usedBuck.insertRecord(val);
            }
        }
        else{
            //add new address here and add value to bucket
            Bucket newBuck=new Bucket(mainMem.getGlobalDepth());
            newBuck.insertRecord(val);
            int address=secMem.putBucket(index,newBuck);
            mainMem.insertAddress(address);
        }
    }

    public static void insertVal(int val,boolean splitOnce){
        if(val<0)
            return;
        int index=getMSB(val);
        System.out.println("Inserting Val: "+val+" at Index: "+index);
        if(index<CommonUtils.dirSize) {
            insertToMain(val, index,splitOnce);
        }
        else{
            insertToSec(val,index,splitOnce);
        }
    }

    public static void printBAT(){
        System.out.println("Addresses:-");
        int[] BAT=mainMem.getBuckAddressTable();
        ArrayList<Integer> secBAT=secMem.getBuckAddressTable();
        for(int i=0;i<mainMem.getIndex();i++){
            System.out.println("Ind: "+i+" Addr: "+BAT[i]);
        }
        for(int i=0;i<secMem.getIndex();i++){
            int j=CommonUtils.dirSize+i;
            System.out.println("Ind: "+j+" Addr: "+secBAT.get(i));
        }
    }

    public static boolean searchVal(int val){
        int index=getMSB(val);
        int buckAddress;
        if(index<CommonUtils.dirSize) {
            buckAddress=mainMem.getBucketAddressAt(index);
        }
        else{
            buckAddress=secMem.getBucketAddressAt(index);
        }
//        System.out.println("Searching in Bucket: "+buckAddress);
        Bucket searchBuck=secMem.getBucketAt(buckAddress);
        if(searchBuck==null) {
            return false;
        }
        return searchBuck.searchRecord(val);
    }

    public static int getBucketCount(){
        int N=0;
        HashMap buckMap=secMem.getBucketMap();
        Iterator it = buckMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            N+=((Bucket)pair.getValue()).getCount();
//            it.remove(); // avoids a ConcurrentModificationException
        }
        return N;
    }

    public static void main(String[] args){
        CommonUtils.setBuckLength(2);
        insertVal(2,false);
        insertVal(2,false);
        insertVal(3,false);
        insertVal(1,false);
        insertVal(1,false);
//        insertVal(7,false);
//        insertVal(0);
//        insertVal(0);
//        insertVal(0);
//        insertVal(2);
//        insertVal(1);

//        printBAT();
//        secMem.printMap();
//
//        System.out.println("Total Buckets: "+getBucketCount());

        System.out.println(searchVal(2));
        System.out.println(searchVal(3));
        System.out.println(searchVal(1));
        System.out.println(searchVal(0));

//        secMem.printMap();

    }
}
