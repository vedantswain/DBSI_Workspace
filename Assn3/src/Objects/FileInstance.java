package Objects;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by shubham on 3/29/16.
 */
public class FileInstance {
    Set<Page> localitySet;
    int maxPages;
    int usedPages;
//    ArrayList<PageReferenceString> pageReferenceList;

    public FileInstance(int maxP) {
        this.localitySet = new HashSet<>();
        this.maxPages = maxP;
        this.usedPages = 0;
//        pageReferenceList = new ArrayList<>();
    }

    public int getUsedPages() {
        return this.usedPages;
    }

    public void setUsedPages(int usedBuffers) {
        this.usedPages = usedBuffers;
    }

    public boolean checkInLocalitySet(Page page){
        return this.localitySet.contains(page);
    }

    public void addToLocalitySet(Page page){
        ++this.usedPages;
        this.localitySet.add(page);
        if (this.usedPages > this.maxPages){
            evictPage();
            this.usedPages = this.maxPages;
        }
    }

    private void evictPage(){

    }

//    public void addToPageReferenceList(PageReferenceString prString){
//        pageReferenceList.add(prString);
//    }
}
