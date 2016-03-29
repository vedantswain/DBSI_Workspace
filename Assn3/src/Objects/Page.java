package Objects;

/**
 * Created by shubham on 3/29/16.
 */
public class Page {
    String page;

    public Page(String page) {
        this.page = page;
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
