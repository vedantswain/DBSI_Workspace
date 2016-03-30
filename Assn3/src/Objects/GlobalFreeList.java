package Objects;

import java.util.ArrayList;

/**
 * Created by shubham on 3/29/16.
 */
public class GlobalFreeList {
    ArrayList<Page> freeBufferList;

    public GlobalFreeList(){
        this.freeBufferList = new ArrayList<>();
    }

    public boolean freeListContains(Page page){
        return freeBufferList.contains(page);
    }

    public void addToFreeList(Page page){
        this.freeBufferList.add(page);
    }
}
