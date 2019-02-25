package hu.bme.aut.shoppinghelper.data;

import com.orm.SugarRecord;

public class ShoppingItem extends SugarRecord<ShoppingItem> {
    private int price;
    private String name;
    private ShoppingList shoppinglist;
    private Category category;
    private boolean incart;
    private String description;

    public int getPrice() {
        return price;
    }

    public ShoppingItem(){
        incart = false;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ShoppingList getShoppingList() {
        return shoppinglist;
    }

    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppinglist = shoppingList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isIncart() {
        return incart;
    }

    public void setIncart(boolean incart) {
        this.incart = incart;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
