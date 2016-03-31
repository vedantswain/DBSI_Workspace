package Objects;

import MyCommons.MyCommons;

import java.util.ArrayList;

/**
 * Created by shubham on 3/29/16.
 */
public class Buffer {
    ArrayList<Page> pageList;

    public Buffer(){
        this.pageList = new ArrayList<>();
    }

    public boolean insertPage(Page page){
        if (this.pageList.size() < MyCommons.getBufferSize()){
            return this.pageList.add(page);
        }
        else {
            return false;
        }
    }

    public boolean removePage(Page page){
        if (this.pageList.size() >= 0){
            return this.pageList.remove(page);
        }
        else {
            return false;
        }
    }

    public boolean isBufferFull(){
//        System.out.println("Buffer size: "+MyCommons.getBufferSize());
        return this.pageList.size() >= MyCommons.getBufferSize();
    }

    public ArrayList<Page> getPageList() {
        return pageList;
    }

    public int getLastOccupiedIndex(){
        return this.pageList.size();
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
