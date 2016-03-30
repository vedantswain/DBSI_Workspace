package Objects;

/**
 * Created by shubham on 3/29/16.
 */
public class Page {
    String page;
    int priority;
    boolean belongsToLocalitySet;

    public Page(String page) {
        this.page = page;
    }

    public void setBelongsToLocalitySet(boolean belongsToLocalitySet) {
        this.belongsToLocalitySet = belongsToLocalitySet;
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
