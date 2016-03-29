package Eviction;

import java.util.HashMap;

/**
 * Created by shubham on 3/29/16.
 */
public class LRU {
    int pageSize;
    HashMap<Integer, Integer> pageSet;

    public LRU (int pgSize){
        pageSize = pgSize;
        pageSet = new HashMap<>(pageSize);
    }

//    public evict(){
//    }
}
