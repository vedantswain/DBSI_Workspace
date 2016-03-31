package Objects;

/**
 * Created by shubham on 3/29/16.
 */
public class Page {
    String page;
    int timestamp;
    boolean belongsToLocalitySet;

    public Page(String page) {
        this.page = page;
    }

    public void setBelongsToLocalitySet(boolean belongsToLocalitySet) {
        this.belongsToLocalitySet = belongsToLocalitySet;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isBelongsToLocalitySet() {
        return belongsToLocalitySet;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == this.getClass())
        {
            Page page1 = (Page) obj;
            if(page1.page.equals(this.page)){
                return true;
            }
        }
        return false;
    }

}
