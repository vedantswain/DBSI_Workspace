package Objects;

import MyCommons.MyCommons;

import java.util.ArrayList;

/**
 * Created by shubham on 3/29/16.
 */
public class Buffer {
    ArrayList<Page> pageList;
    int counter;

    public Buffer(){
        this.counter = 0;
        this.pageList = new ArrayList<>();
    }

    public boolean insertPage(Page page){
        this.counter++;
        if (this.counter < MyCommons.getBufferSize()){
            return this.pageList.add(page);
        }
        else {
            return false;
        }
    }

    public boolean isBufferFull(){
        return this.counter < MyCommons.getBufferSize();
    }

    public ArrayList<Page> getPageList() {
        return pageList;
    }

    public int getLastOccupiedIndex(){
        return this.counter;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == this.getClass())
        {
            Buffer b1 = (Buffer) obj;
            if(b1.pageList.equals(this.pageList)){
                return true;
            }
        }
        return false;
    }
}
