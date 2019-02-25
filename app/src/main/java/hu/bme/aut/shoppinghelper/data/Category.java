package hu.bme.aut.shoppinghelper.data;

import com.orm.SugarRecord;

public class Category extends SugarRecord<Category> {
    private String name;

    public Category(){

    }

    public Category(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
