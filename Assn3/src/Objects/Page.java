package Objects;

/**
 * Created by shubham on 3/29/16.
 */
public class Page {
    String page;
    long timestamp;
    boolean belongsToLocalitySet;

    public Page(String page, long ts) {
        this.page = page;
        this.timestamp = ts;
    }

    public void setBelongsToLocalitySet(boolean belongsToLocalitySet) {
        this.belongsToLocalitySet = belongsToLocalitySet;
    }

    public String getPage() {
        return page;
    }

    public long getTimestamp() {
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
