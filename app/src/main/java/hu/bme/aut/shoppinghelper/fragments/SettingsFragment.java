package hu.bme.aut.shoppinghelper.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import hu.bme.aut.shoppinghelper.R;
import hu.bme.aut.shoppinghelper.data.Category;
import hu.bme.aut.shoppinghelper.data.ShoppingItem;
import hu.bme.aut.shoppinghelper.data.ShoppingList;


public class SettingsFragment extends Fragment {

    private Button bt_clear_categories;
    private Button bt_clear_items;
    private Button bt_clear_shopping_lists;


    public SettingsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View layout = this.getView();
        bt_clear_categories = layout.findViewById(R.id.bt_clear_category);
        bt_clear_items = layout.findViewById(R.id.bt_clear_items);
        bt_clear_shopping_lists = layout.findViewById(R.id.bt_clear_shopping_lists);


        bt_clear_categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShoppingItem.deleteAll(ShoppingItem.class);
                Category.deleteAll(Category.class);
            }
        });

        bt_clear_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShoppingItem.deleteAll(ShoppingItem.class);
            }
        });

        bt_clear_shopping_lists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShoppingItem.deleteAll(ShoppingItem.class);
                ShoppingList.deleteAll(ShoppingList.class);
            }
        });
    }
}
