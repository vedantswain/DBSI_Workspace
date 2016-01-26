package EH.objects;

import EH.CommonUtils;

import java.util.ArrayList;

/**
 * Created by vedantdasswain on 19/01/16.
 */
public class SecMem {
    ArrayList<Integer> buckAddressTable;
    ArrayList<Bucket> bucketList;
//    HashMap<Integer,Bucket> bucketMap;
    int index;

    public SecMem(){
        bucketList=new ArrayList<Bucket>();
        buckAddressTable=new ArrayList<Integer>();
//        bucketMap=new HashMap<Integer, Bucket>();
        index=0;
    }

//    public HashMap<Integer, Bucket> getBucketMap() {
//        return bucketMap;
//    }

    public int getIndex() {
        return index;
    }

    public void initBAT(int size){
//        System.out.println("Initialising SecMem");
        buckAddressTable=new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            buckAddressTable.add(-1);
        }
    }

    public void resizeBAT(int GD){
        long toAdd= (long) (Math.pow(2,GD)-(CommonUtils.dirSize+buckAddressTable.size()));
        int oldSize=buckAddressTable.size();
//        System.out.println("Resizing SecMem: "+Math.pow(2,GD)+" Current Size: "+oldSize+" toAdd: "+toAdd);
        for (int i = 0;i< toAdd;i++){
            buckAddressTable.add(-1);
        }
    }

    public void insertAddress(int i,int address){
        i= i-CommonUtils.dirSize;
        buckAddressTable.set(i,address);
        index++;
    }

    public ArrayList<Integer> getBuckAddressTable() {
        return buckAddressTable;
    }

    public int getBucketAddressAt(int i){
        i=i-CommonUtils.dirSize;
        return buckAddressTable.get(i);
    }

//    public int putBucket(int key, Bucket buck){
//        bucketMap.put(key,buck);
//        return key;
//    }
//
//    public Bucket getBucketAt(int key){
//        return bucketMap.get(key);
//    }

    public int putBucket(Bucket buck){
        bucketList.add(buck);
        return bucketList.size()-1;
    }

    public Bucket getBucketAt(int index){
        return bucketList.get(index);
    }

//    public void deleteBucketAt(int key){
//        bucketMap.remove(bucketMap.get(key));
//    }

//    public void printMap() {
//        Map mp=this.bucketMap;
//        Iterator it = mp.entrySet().iterator();
//        if(!it.hasNext()){
//            System.out.println("No buckets");
//        }
//        while (it.hasNext()) {
//            Map.Entry pair = (Map.Entry)it.next();
//            System.out.println("Bucket key "+pair.getKey() + " - CurrentInd: " + ((Bucket)pair.getValue()).getIndex());
////            it.remove(); // avoids a ConcurrentModificationException
//        }
//    }
}
