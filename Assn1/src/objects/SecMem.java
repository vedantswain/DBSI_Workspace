package objects;

import main.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vedantdasswain on 19/01/16.
 */
public class SecMem {
    ArrayList<Integer> buckAddressTable;
//    ArrayList<Bucket> bucketList;
    HashMap<Integer,Bucket> bucketMap;
    int index;

    public SecMem(){
//        bucketList=new ArrayList<Bucket>();
        buckAddressTable=new ArrayList<Integer>();
        bucketMap=new HashMap<Integer, Bucket>();
        index=0;
    }

    public HashMap<Integer, Bucket> getBucketMap() {
        return bucketMap;
    }

    public int getIndex() {
        return index;
    }

    public void initBAT(int size){
        buckAddressTable=new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            buckAddressTable.add(-1);
        }
    }

    public void resizeBAT(int GD){
        long toAdd= (long) (Math.pow(2,GD)-(CommonUtils.dirSize+buckAddressTable.size()));
        for (int i = 0;i< toAdd;i++){
            buckAddressTable.add(-1);
        }
    }

    public void insertAddress(int address,int i){
        i= CommonUtils.dirSize-i;
        buckAddressTable.add(i,address);
        index++;
    }

    public ArrayList<Integer> getBuckAddressTable() {
        return buckAddressTable;
    }

    public int getBucketAddressAt(int i){
        i=CommonUtils.dirSize-i;
        return buckAddressTable.get(i);
    }

    public int putBucket(int key, Bucket buck){
        bucketMap.put(key,buck);
        return key;
    }

    public Bucket getBucketAt(int key){
        return bucketMap.get(key);
    }

    public void deleteBucketAt(int key){
        bucketMap.remove(bucketMap.get(key));
    }
}
