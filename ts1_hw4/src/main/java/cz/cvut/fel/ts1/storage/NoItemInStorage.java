package cz.cvut.fel.ts1.storage;

public class NoItemInStorage extends Exception{
    public NoItemInStorage() {
        super("No item in storage");
    }
}
