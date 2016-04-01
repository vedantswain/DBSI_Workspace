package Objects;

import Eviction.Replacement;

import java.util.ArrayList;

/**
 * Created by shubham on 3/29/16.
 */
public class FileInstance {
    ArrayList<Page> localitySet;
    int maxPages;
//    ArrayList<PageReferenceString> pageReferenceList;

    public FileInstance(int maxP) {
        this.localitySet = new ArrayList<>();
        this.maxPages = maxP;
//        pageReferenceList = new ArrayList<>();
    }

    public boolean checkInLocalitySet(Page page){
        return this.localitySet.contains(page);
    }

    public boolean addToLocalitySet(Page page, GlobalTable gTable, PageTable pTable){
//        System.out.println("Number of Used Pages: "+this.usedPages);
        if (this.localitySet.size() >= this.maxPages){
            if (evictPage(gTable, pTable)) {
                this.localitySet.add(page);
                return true;
            }
            else {
                return false;
            }
        }
        this.localitySet.add(page);
//        System.out.println("Number of Used Pages after insertion: "+this.usedPages+", Page: "+page.getPage());
        return true;
    }

    private boolean evictPage(GlobalTable globalTable, PageTable pageTable){
        //TODO: Replacement to be done
        Page evictPage = Replacement.getPageLRU(this.localitySet);
//        Page evictPage = Replacement.getPageMRU(this.localitySet);
//        System.out.println("Evicting Page : " + evictPage.getPage());
        return this.localitySet.remove(evictPage) && globalTable.removeFromGlobalTable(evictPage, pageTable);
    }
}
