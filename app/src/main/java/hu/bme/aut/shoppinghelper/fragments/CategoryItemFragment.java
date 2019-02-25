package hu.bme.aut.shoppinghelper.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import hu.bme.aut.shoppinghelper.R;
import hu.bme.aut.shoppinghelper.adapter.CategoryAdapter;
import hu.bme.aut.shoppinghelper.adapter.ItemAdapter;
import hu.bme.aut.shoppinghelper.data.Category;
import hu.bme.aut.shoppinghelper.data.ShoppingItem;

public class CategoryItemFragment extends Fragment {

    private ItemAdapter adapter;
    private RecyclerView recyclerView;
    private TextView tv_category;
    private static Context context;
    private static Category currentCategory;
    private View layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_item, container, false);
    }

    public CategoryItemFragment(){

    }

    private static void setContext(Context givenContext){
        context = givenContext;
    }

    private static void setCurrentCategory(Category category){
        currentCategory = category;
    }

    public static CategoryItemFragment newInstance(Context context, Category category) {
        setContext(context);
        setCurrentCategory(category);
        CategoryItemFragment fragment = new CategoryItemFragment();
        return fragment;
    }

    private void initRecylerView(){
        recyclerView = layout.findViewById(R.id.rv_category_items);
        adapter = new ItemAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        layout = this.getView();
        if(layout!=null) {
            initRecylerView();
            updateRecyclerView();

            tv_category = layout.findViewById(R.id.tv_category_item);
            tv_category.setText(currentCategory.getName()+" items not in cart");
        }
    }

    private void updateRecyclerView(){
        List<ShoppingItem> items = ShoppingItem.find(ShoppingItem.class, "category = ?", new String (currentCategory.getId().toString()));
        for(ShoppingItem item : items){
            if(!item.isIncart())
             adapter.addItem(item);
        }
    }
}
