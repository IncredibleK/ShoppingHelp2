package hu.bme.aut.shoppinghelper.data;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

public class ShoppingList extends SugarRecord<ShoppingList> {
    private String name;

    public ShoppingList(){

    }

    public ShoppingList(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
