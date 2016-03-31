package Objects;

import Eviction.Replacement;

import java.util.ArrayList;

/**
 * Created by shubham on 3/29/16.
 */
public class FileInstance {
    ArrayList<Page> localitySet;
    int maxPages;
    int usedPages;
//    ArrayList<PageReferenceString> pageReferenceList;

    public FileInstance(int maxP) {
        this.localitySet = new ArrayList<>();
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

    public boolean addToLocalitySet(Page page, GlobalTable gTable, PageTable pTable){
        ++this.usedPages;
        if (this.usedPages > this.maxPages){
            if (evictPage(gTable, pTable)) {
                this.usedPages = this.maxPages;
                return true;
            }
            else
                return false;
        }
        else
            return this.localitySet.add(page);
    }

    private boolean evictPage(GlobalTable globalTable, PageTable pageTable){
        //TODO: Replacement to be done
        Page evictPage = Replacement.getPageLRU(this.localitySet);
        return this.localitySet.remove(evictPage) && globalTable.removeFromGlobalTable(evictPage, pageTable);
    }

//    public void addToPageReferenceList(PageReferenceString prString){
//        pageReferenceList.add(prString);
//    }
}
