package EH;

import EH.objects.Bucket;
import EH.objects.MainMem;
import EH.objects.SecMem;

import java.math.BigInteger;
import java.util.ArrayList;

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
//        int padding=log2(8);
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


    public static int getManytoOneCount(int address){
        int[] BAT=mainMem.getBuckAddressTable();
        ArrayList secBAT=secMem.getBuckAddressTable();

        int mainMemLength= mainMem.getGlobalDepth()<=10? (int) Math.pow(2, mainMem.getGlobalDepth()) :CommonUtils.dirSize;

        int countPtr=0;

        for(int i=0;i<mainMemLength;i++){
//            System.out.println(BAT[i]+" v/s "+address);
            if(BAT[i]==address) {
                countPtr++;
            }
        }

        for(int i=0;i<secBAT.size();i++)
        {
//            System.out.println((int)secBAT.get(i)+" v/s "+address);
            if((int)secBAT.get(i)==address)
                countPtr++;
        }

//        System.out.println("Final "+countPtr);
        return countPtr;
    }

    public static int getMidIndex(int address, int countPtr, int value){
        int midPtr=countPtr/2;
        int mainMemLength= mainMem.getGlobalDepth()<=10? (int) Math.pow(2, mainMem.getGlobalDepth()):CommonUtils.dirSize;

        int[] BAT=mainMem.getBuckAddressTable();
        ArrayList secBAT=secMem.getBuckAddressTable();

//        System.out.println("CountPtr "+countPtr);
        int i=0;
        while (countPtr>0 && i<mainMemLength){
            if(BAT[i]==address)
                countPtr--;
            if(countPtr==midPtr){
                return i+1;
            }
            i++;
        }

        i=0;
        while (countPtr>0 && i<secBAT.size()){
            if((int)secBAT.get(i)==address)
                countPtr--;
            if(countPtr==midPtr){
                return i+1+CommonUtils.dirSize;
            }
            i++;
        }

        return getMSB(value);
    }

    public static void split(Bucket usedBuck, int val, boolean splitOnce){
        int index=getMSB(val);
//        System.out.println("Index: "+index+" Val: "+val);

        int address;
        if(index<CommonUtils.dirSize)
            address=mainMem.getBucketAddressAt(index);
        else
            address=secMem.getBucketAddressAt(index);

//        System.out.println("Bucket to be only split is: "+address+" Pointers to address: "+getManytoOneCount(address));

        usedBuck.setLocalDepth(usedBuck.getLocalDepth()+1); //Original Bucket
        Bucket newBuck=new Bucket(usedBuck.getLocalDepth()); //New Bucket

        //Inserting New Bucket to Secondary Memomry
        int countPtr=getManytoOneCount(address);
        int midX=getMidIndex(address,countPtr,val);

        int newAddress=secMem.putBucket(newBuck);

//        System.out.println("MidX: "+midX);


        if(midX>=CommonUtils.dirSize)
            secMem.insertAddress(midX,newAddress);
        else
            mainMem.insertAddress(midX,newAddress);

        int mainMemLength= mainMem.getGlobalDepth()<=10? (int) Math.pow(2, mainMem.getGlobalDepth()) :CommonUtils.dirSize;

        //Second half of the directory addresses new bucket
        int X=midX;
        if(midX<CommonUtils.dirSize){
            for(int i=midX;i<mainMemLength;i++){
                if(mainMem.getBucketAddressAt(i)==address)
                    mainMem.insertAddress(i,newAddress);
            }
            X=0;
        }
        for(int i=mainMemLength+1;i<secMem.getBuckAddressTable().size();i++)
        {
            if(secMem.getBucketAddressAt(i)==address && i>=X)
                secMem.insertAddress(i,newAddress);
        }

        //Reinserting values in old bucket across both buckets
        int[] oldRecords=usedBuck.popBuck();

        for(int i=0;i<oldRecords.length;i++){
//            System.out.println("Inserting Old: "+oldRecords[i]);
            insertVal(oldRecords[i],splitOnce);
        }
        insertVal(val,splitOnce);
    }

    public static int getMatchAddress(int index){
        if(index/2<CommonUtils.dirSize)
            return mainMem.getBucketAddressAt(index/2);
        else
            return secMem.getBucketAddressAt(index/2);
    }

    public static void resize(){
        mainMem.setGlobalDepth(mainMem.getGlobalDepth()+1);
        int mainMemLength= mainMem.getGlobalDepth()<=10? (int) Math.pow(2, mainMem.getGlobalDepth()) :CommonUtils.dirSize;
        System.out.println("GD: "+mainMem.getGlobalDepth());

        if(mainMem.getGlobalDepth()>10) {
//            System.out.println("Secondary Memory Opening");
            if(secMem.getBuckAddressTable()==null){
                secMem.initBAT(CommonUtils.dirSize);
            }
            else {
                secMem.resizeBAT(mainMem.getGlobalDepth());
            }
        }

        //Two Entries Pointing to the same Bucket
        if(mainMem.getGlobalDepth()>10){
            for(int i=secMem.getBuckAddressTable().size()-1;i>0;i--){
                secMem.insertAddress(i+CommonUtils.dirSize,getMatchAddress(i+CommonUtils.dirSize));
//                System.out.println("Inserting "+getMatchAddress(i+CommonUtils.dirSize)
//                        +" from "+(i+CommonUtils.dirSize)/2+" to "+i+CommonUtils.dirSize);
            }
        }
        for(int i=mainMemLength-1;i>0;i--){
            mainMem.insertAddress(i,mainMem.getBucketAddressAt(i/2));
//            System.out.println("Inserting "+mainMem.getBucketAddressAt(i)
//                    +" from "+i/2+" to "+i);
        }
    }

    public static void collide(Bucket buck,int val){
//        System.out.println("Collision at bucket with LD: "+buck.getLocalDepth()+" GD: "+mainMem.getGlobalDepth());
        if(buck.getLocalDepth()==mainMem.getGlobalDepth() && mainMem.getGlobalDepth()<20){ //Resizing and rehashing takes place
            System.out.println("Resizing");
//            buck.setLocalDepth(buck.getLocalDepth()+1);
//            redist(buck,val,true);
//            repoint();
            resize();
            insertVal(val,false);
        }
        else //Only bucket is split
        {
//            System.out.println("Splitting");
            split(buck,val,true);
        }
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
                if(!splitOnce)
                    collide(usedBuck,val);
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
            int address=secMem.putBucket(newBuck);
//            System.out.println("Inserting Address: "+address+" in Sec Mem at: "+index);
            secMem.insertAddress(index,address);
        }
    }

    public static void insertToMain(int val,int index,boolean splitOnce){
        if (mainMem.getBucketAddressAt(index)!=-1){
            //bucket address exists
//            System.out.println("Address entry for Ind: "+index+" is Address: "+mainMem.getBucketAddressAt(index));
            int buckAddress=mainMem.getBucketAddressAt(index);
            Bucket usedBuck=secMem.getBucketAt(buckAddress);
            if(usedBuck.getIndex()>=CommonUtils.buckLength){
                //splitting
                if(!splitOnce)
                    collide(usedBuck,val);
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
//            System.out.println("New address entry for Ind: "+index);
            Bucket newBuck=new Bucket(mainMem.getGlobalDepth());
            newBuck.insertRecord(val);
            int address=secMem.putBucket(newBuck);
            mainMem.insertAddress(index,address);
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
        int mainMemLength= mainMem.getGlobalDepth()<=10? (int) Math.pow(2, mainMem.getGlobalDepth()) :CommonUtils.dirSize;

        int[] BAT=mainMem.getBuckAddressTable();
        ArrayList<Integer> secBAT=secMem.getBuckAddressTable();
        for(int i=0;i<mainMemLength;i++){
            System.out.println("Ind: "+i+" Bucket: "+BAT[i]+" LD: "+secMem.getBucketAt(BAT[i]).getLocalDepth());
        }
        for(int i=0;i<secBAT.size();i++){
            int j=CommonUtils.dirSize+i;
            if(secBAT.get(i)==-1)
                continue;
            System.out.println("Ind: "+j+" Bucket: "+secBAT.get(i)+" LD: "+secMem.getBucketAt(secBAT.get(i)).getLocalDepth());
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

//    public static int getBucketCount(){
//        int N=0;
//        HashMap buckMap=secMem.getBucketMap();
//        Iterator it = buckMap.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry pair = (Map.Entry)it.next();
//            N+=((Bucket)pair.getValue()).getCount();
////            it.remove(); // avoids a ConcurrentModificationException
//        }
//        return N;
//    }

    public static void insert(int val){
        insertVal(val,false);
    }
}
