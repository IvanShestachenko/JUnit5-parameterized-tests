package cz.cvut.fel.ts1.shop;

import java.util.ArrayList;
import cz.cvut.fel.ts1.storage.*;
import cz.cvut.fel.ts1.archive.*;


public class EShopController {

    private static Storage storage;
    private static PurchasesArchive archive;
    private static ArrayList<ShoppingCart> carts;

    public EShopController() {
    }

    

    public static void purchaseShoppingCart(ShoppingCart cart, String customerName, String customerAddress) throws NoItemInStorage {
        if (cart.getCartItems().isEmpty()) {
            System.out.println("Error: shopping cart is empty");
            return;
        }
        Order order = new Order(cart, customerName, customerAddress);
        storage.processOrder(order);
        archive.putOrderToPurchasesArchive(order);
    }

    public static ShoppingCart newCart() {
        ShoppingCart newCart = new ShoppingCart();
        carts.add(newCart);
        return newCart;
    }

    public static void startEShop() {
        if (storage == null) {
            storage = new Storage();
            archive = new PurchasesArchive();
            carts = new ArrayList();
        }
    }







public static void main(String[] args) throws NoItemInStorage {
        EShopController.startEShop();
        
        /* make up an artificial data */
        
        int[] itemCount = {10,10,4,5,10,2};
               
        
        Item[] storageItems = {
            new StandardItem(1, "Dancing Panda v.2", 5000, "GADGETS", 5),
            new StandardItem(2, "Dancing Panda v.3 with USB port", 6000, "GADGETS", 10),
            new StandardItem(3, "Screwdriver", 200, "TOOLS", 5),
            new DiscountedItem(4, "Star Wars Jedi buzzer", 500, "GADGETS", 30, "1.8.2013", "1.12.2013"),
            new DiscountedItem(5, "Angry bird cup", 300, "GADGETS", 20, "1.9.2013", "1.12.2013"),
            new DiscountedItem(6, "Soft toy Angry bird (size 40cm)", 800, "GADGETS", 10, "1.8.2013", "1.12.2013")
        };
        
        // insert data to the storage
        for (int i = 0; i < storageItems.length; i++) {
            storage.insertItems(storageItems[i], itemCount[i]);
        }
        
        
        storage.printListOfStoredItems();
        
        System.out.println();
        
        System.out.println("TEST RUN:   Fill and purchase a shopping cart");
        ShoppingCart newCart = EShopController.newCart();
        newCart.addItem(storageItems[0]);
        newCart.addItem(storageItems[1]);
        newCart.addItem(storageItems[2]);
        newCart.addItem(storageItems[4]);
        newCart.addItem(storageItems[5]);
        purchaseShoppingCart(newCart, "Libuse Novakova","Kosmonautu 25, Praha 8");
        archive.printItemPurchaseStatistics();
        storage.printListOfStoredItems();
        
        
        System.out.println();
        
                
        System.out.println("TEST RUN:    Trying to purchase an empty shopping cart");
        
        ShoppingCart newEmptyCart = EShopController.newCart();
        purchaseShoppingCart(newEmptyCart, "Jarmila Novakova", "Spojovaci 23, Praha 3");
        
        
        
    }



}

