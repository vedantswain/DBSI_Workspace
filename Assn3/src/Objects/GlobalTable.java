package Objects;

import java.util.ArrayList;

/**
 * Created by shubham on 3/29/16.
 */
public class GlobalTable {
    ArrayList<Buffer> bufferList;

    public GlobalTable(){
        this.bufferList = new ArrayList<>();
    }

    public void addToGlobalTable(Page page, PageTable pageTable){
        int bufferIndex = getNextVacantBufferIndex();
        if (bufferIndex < 0)
        {
            Buffer buffer2 = new Buffer();
            buffer2.insertPage(page);
//            System.out.println("Inserted in first buffer: " + buffer2.insertPage(page));
            this.bufferList.add(buffer2);
            pageTable.addToPageTable(page, 0);
        }
        else {
            Buffer buffer1 = this.bufferList.get(bufferIndex);
            if (!buffer1.isBufferFull()){
                buffer1.insertPage(page);
//                System.out.println("Inserted in existing buffer: " + buffer1.insertPage(page)+", page: "+
//                        page.getPage());
//                System.out.println("existing buffer, bufferIndex: "+bufferIndex);
                pageTable.addToPageTable(page, bufferIndex);
            }
            else {
                Buffer buffer2 = new Buffer();
                buffer2.insertPage(page);
//                System.out.println("Inserted in new buffer: " + buffer2.insertPage(page));
                bufferList.add(buffer2);
                pageTable.addToPageTable(page,bufferList.size()-1);
            }
        }
    }

    public int getNextVacantBufferIndex(){
        for (int i=0; i<this.bufferList.size(); ++i){
            if(!this.bufferList.get(i).isBufferFull()){
                return i;
            }
        }
        return this.bufferList.size()-1;
    }

    public boolean removeFromGlobalTable(Page page, PageTable pageTable){
        int bufferIndex = pageTable.getPageTableIndex(page);
        Buffer buffer1 = this.bufferList.get(bufferIndex);
        if (buffer1.removePage(page)){
            pageTable.removeFromPageTable(page);
            return true;
        }
        else{
            return false;
        }
    }

    public boolean bufferListContains(Page page, PageTable pageTable){
        int bufferIndex = pageTable.getPageTableIndex(page);
        if (bufferIndex >= 0)
        {
            Buffer buffer = bufferList.get(bufferIndex);
            if (buffer.getPageList().contains(page)) {
                int ind = buffer.getPageList().indexOf(page);
                buffer.getPageList().set(ind, page);
                return true;
            }
            else
                return false;
        }
        else {
            return false;
        }
    }

}
