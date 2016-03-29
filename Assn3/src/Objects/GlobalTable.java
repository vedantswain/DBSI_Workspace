package Objects;

import java.util.ArrayList;

/**
 * Created by shubham on 3/29/16.
 */
public class GlobalTable {
    ArrayList<Buffer> bufferList;
    int globalTableSize;

    public GlobalTable(int gTSize){
        this.globalTableSize = gTSize;
        this.bufferList = new ArrayList<>(globalTableSize);
    }

    public boolean bufferListContains(Page page){
        for (Buffer buffer: this.bufferList){
            ArrayList<Page> bufferPageList = buffer.getPageList();
            if (bufferPageList.contains(page))
                return true;
        }
        return false;
    }

}
