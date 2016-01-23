package EH.objects;

import EH.CommonUtils;

/**
 * Created by vedantdasswain on 19/01/16.
 */
public class MainMem {
    int[] buckAddressTable;
    int globalDepth;
    int index;

    public MainMem(){
        buckAddressTable=new int[CommonUtils.dirSize];
        initBAT();
        globalDepth=0;
        index=0;
    }

    public void initBAT(){
        for(int i=0;i<CommonUtils.dirSize;i++){
            buckAddressTable[i]=-1;
        }
    }

    public int getGlobalDepth(){
        return globalDepth;
    }

    public void setGlobalDepth(int globalDepth) {
        this.globalDepth = globalDepth;
    }

    public int[] getBuckAddressTable() {
        return buckAddressTable;
    }

    public int getBucketAddressAt(int i){
        return buckAddressTable[i];
    }

    public int getIndex() {
        return index;
    }

    public void insertAddress(int address){
        buckAddressTable[index]=address;
        index++;
    }

    public void insertAddress(int newAddress, int midX) {
        buckAddressTable[midX]=newAddress;
    }
}
