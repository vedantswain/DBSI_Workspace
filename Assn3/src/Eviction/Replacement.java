package Eviction;

import Objects.Page;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by shubham on 3/29/16.
 */
public class Replacement {
    int pageSize;
    HashMap<Integer, Integer> pageSet;

    public Replacement(int pgSize){
        pageSize = pgSize;
        pageSet = new HashMap<>(pageSize);
    }

    public static Page getPageLRU(ArrayList<Page> pageList){
        Page oldPage = pageList.get(0);
        for (int i=1; i < pageList.size(); ++i) {
            if (pageList.get(i).getTimestamp() < oldPage.getTimestamp()){
                oldPage = pageList.get(i);
            }
        }
        return oldPage;
    }

    public  static Page getPageMRU(ArrayList<Page> pageList){
        Page newPage = pageList.get(0);
        for (int i=1; i < pageList.size(); ++i) {
            if (pageList.get(i).getTimestamp() > newPage.getTimestamp()){
                newPage = pageList.get(i);
            }
        }
        return newPage;
    }
}
