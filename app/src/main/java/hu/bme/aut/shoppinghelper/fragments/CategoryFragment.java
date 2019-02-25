package hu.bme.aut.shoppinghelper.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hu.bme.aut.shoppinghelper.R;
import hu.bme.aut.shoppinghelper.adapter.CategoryAdapter;
import hu.bme.aut.shoppinghelper.adapter.ListAdapter;
import hu.bme.aut.shoppinghelper.data.Category;
import hu.bme.aut.shoppinghelper.data.DataListener;
import hu.bme.aut.shoppinghelper.data.ShoppingList;


public class CategoryFragment extends Fragment implements DataListener {

    private CategoryAdapter adapter;
    private RecyclerView recyclerView;
    private static Context context;
    private View layout;

    public CategoryFragment() {

    }

    private static void setContext(Context givenContext){
        context = givenContext;
    }

    public static CategoryFragment newInstance(Context context) {
        setContext(context);
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }

    private void initRecylerView(){
        recyclerView = layout.findViewById(R.id.rv_categories);
        adapter = new CategoryAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        layout = getView();
        initRecylerView();
        updateRecyclerView();
    }

    private void updateRecyclerView(){
        List<Category> lists = Category.listAll(Category.class);
        for(Category category : lists){
            adapter.addItem(category);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    public static Context getGivenContext(){
        return context;
    }

    @Override
    public void update() {
        initRecylerView();
        updateRecyclerView();
    }
}
