package Objects;

import java.util.HashMap;

/**
 * Created by shubham on 3/30/16.
 */
public class PageTable {
    HashMap<String, Integer> pageMap;

    public PageTable(){
        this.pageMap = new HashMap<>();
    }

    public void addToPageTable(Page page,int index){
        this.pageMap.put(page.getPage(),index);
    }

    public void removeFromPageTable(Page page){
        this.pageMap.remove(page.getPage());
    }

    public int getPageTableIndex(Page page){
        try {
            return pageMap.get(page.getPage());
        }
        catch (NullPointerException ex){
//            System.out.println("Page not found");
            return -1;
        }
    }
}
