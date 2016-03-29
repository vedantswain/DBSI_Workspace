package Objects;

import java.util.ArrayList;

/**
 * Created by shubham on 3/29/16.
 */
public class GlobalFreeList {
    ArrayList<Buffer> freeBufferList;

    public GlobalFreeList(){
        this.freeBufferList = new ArrayList<>();
    }

    public boolean freeListContains(Page page){
        for (Buffer buffer: this.freeBufferList){
            ArrayList<Page> bufferPageList = buffer.getPageList();
            if (bufferPageList.contains(page))
                return true;
        }
        return false;
    }
}
