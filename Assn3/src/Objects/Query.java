package Objects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by shubham on 3/29/16.
 */
public class Query {
    Set<Page> localitySet;
    ArrayList<PageReferenceString> pageReferenceList;

    public Query() {
        localitySet = new HashSet<>();
        pageReferenceList = new ArrayList<>();
    }

    public void addToLocalitySet(Page page, GlobalTable gTable, GlobalFreeList gfList){
        if (gTable.bufferListContains(page))
        {
            //update usage statistics
        }
        else if (gfList.freeListContains(page))
        {
            localitySet.add(page);
        }
        else
        {
            //page not present in main memory
        }
    }

    public void addToPageReferenceList(PageReferenceString prString){
        pageReferenceList.add(prString);
    }
}
