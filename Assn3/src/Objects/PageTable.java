package Objects;

import java.util.HashMap;

/**
 * Created by shubham on 3/30/16.
 */
public class PageTable {
    HashMap<Page, Integer> pageMap;

    public PageTable(){
        this.pageMap = new HashMap<>();
    }

    public void addToPageTable(Page page,int index){
        this.pageMap.put(page,index);
    }

    public void removeFromPageTable(Page page){
        this.pageMap.remove(page);
    }

    public int getPageTableIndex(Page page){
        return pageMap.get(page);
    }
}
