package cz.cvut.fel.ts1.storage;

import cz.cvut.fel.ts1.shop.*;


/**
 * Auxiliary class for item storage
 */
public class ItemStock {
    private Item refItem;
    private int count;
    
    ItemStock(Item refItem) {
        if (refItem == null) {
            throw new NullPointerException();
        }
        this.refItem = refItem;
        count = 0;
    }
    
    @Override
    public String toString() {
        return "STOCK OF ITEM:  "+refItem.toString()+"    PIECES IN STORAGE: "+count;
    }
    
    void increaseItemCount(int x) {
        this.count += x;
    }
    
    void decreaseItemCount(int x) throws NoItemInStorage {
        if (x > this.count) {
            throw new NoItemInStorage();
        }
        count -= x;
    }
    
    int getCount() {
        return count;
    }
    
    Item getItem() {
        return refItem;
    }
}