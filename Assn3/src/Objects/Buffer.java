package Objects;

import java.util.ArrayList;

/**
 * Created by shubham on 3/29/16.
 */
public class Buffer {
    ArrayList<Page> pageList;
    int bufferSize;

    public Buffer(int bSize){
        this.bufferSize = bSize;
        this.pageList = new ArrayList<>(bufferSize);
    }

    public ArrayList<Page> getPageList() {
        return pageList;
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
