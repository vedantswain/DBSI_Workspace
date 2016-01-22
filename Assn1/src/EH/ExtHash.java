package EH;

import EH.objects.Bucket;
import EH.objects.MainMem;
import EH.objects.SecMem;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by vedantdasswain on 22/01/16.
 */
public class ExtHash {
    static MainMem mainMem=new MainMem();
    static SecMem secMem=new SecMem();

    public static int log2(int n)
    {
        return (int)(Math.ceil(Math.log(n) / Math.log(2)));
    }

    public static int getMSB(int val){
        BigInteger v = BigInteger.valueOf(val);
        int padding=log2(800000);
        String byteString = String.format("%0"+padding+"d",new BigInteger(v.toString(2)));
        String prefix=byteString.substring(0,mainMem.getGlobalDepth());
//        System.out.println("For Val: "+val+" Index: "+msbV.intValue());
        if(prefix.equals(""))
            return 0;
        return Integer.parseInt(prefix, 2);
    }

    public static String getPrefix(int val,int depth){
        BigInteger v = BigInteger.valueOf(val);
        String byteString = String.format("%0"+mainMem.getGlobalDepth()+"d",new BigInteger(v.toString(2)));
        String prefix=byteString.substring(0,depth);
        return prefix;
    }


    public static void redist(Bucket usedBuck, int val, boolean splitOnce){
        mainMem.setGlobalDepth(mainMem.getGlobalDepth()+1);
//        System.out.println("GD: "+mainMem.getGlobalDepth());
        if(mainMem.getGlobalDepth()>10) {
//            System.out.println("Secondary Memory Opening");
            if(secMem.getBuckAddressTable()==null){
                secMem.initBAT(CommonUtils.dirSize);
            }
            else {
                secMem.resizeBAT(mainMem.getGlobalDepth());
            }
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
//                        System.out.println("Repointing: Main "+j+" ->"+" Main "+i);
                    }
                }
            }

            for(int j=0;j<secMem.getIndex();j++){
                Bucket checkBuck=secMem.getBucketAt((int)secBAT.get(j));
                if(checkBuck==null || checkBuck.isEmpty()){
                    if(prefix.equals(getPrefix(j,ld))){
                        secMem.deleteBucketAt((int)secBAT.get(j));
                        secBAT.add(j,BAT[i]);
//                        System.out.println("Repointing: Sec "+(CommonUtils.dirSize+j)+" ->"+" Main "+i);
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
//                        System.out.println("Repointing: Main "+j+" ->"+" Sec "+(CommonUtils.dirSize+i));
                    }
                }
            }

            for (int j = 0; j < secMem.getIndex(); j++) {
                Bucket checkBuck = secMem.getBucketAt((int) secBAT.get(j));
                if (checkBuck == null || checkBuck.isEmpty()) {
                    if (prefix.equals(getPrefix(j, ld))) {
                        secMem.deleteBucketAt((int) secBAT.get(j));
                        secBAT.add(j, (int) secBAT.get(i));
//                        System.out.println("Repointing: Sec "+(CommonUtils.dirSize+j)+" ->"+" Sec "+(CommonUtils.dirSize+i));
                    }
                }
            }
        }
    }

    public static void split(Bucket buck,int val){
//        System.out.println("Splitting");
        buck.setLocalDepth(buck.getLocalDepth()+1);
        redist(buck,val,true);
        repoint();
    }

    public static void insertToSec(int val,int index,boolean splitOnce){
//        System.out.println("Inserting in Sec at Index: "+index);
        if(secMem.getBuckAddressTable()==null){
            secMem.initBAT(CommonUtils.dirSize);
        }
        if (secMem.getBucketAddressAt(index)!=-1){
            //bucket address exists
            int buckAddress=secMem.getBucketAddressAt(index);
            Bucket usedBuck=secMem.getBucketAt(buckAddress);
            if(usedBuck.getIndex()>=CommonUtils.buckLength){
                //splitting
                if(!splitOnce && mainMem.getGlobalDepth()<20)
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
//            System.out.println("Inserting Address: "+address+" in Sec Mem at: "+index);
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
//        System.out.println("Inserting Val: "+val+" at Index: "+index);
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

    public static int searchVal(int val){
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
            return 0;
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

    public static void insert(int val){
        insertVal(val,false);
    }
}
