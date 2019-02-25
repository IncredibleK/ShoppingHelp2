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

import java.util.List;

import hu.bme.aut.shoppinghelper.R;
import hu.bme.aut.shoppinghelper.adapter.ListAdapter;
import hu.bme.aut.shoppinghelper.data.DataListener;
import hu.bme.aut.shoppinghelper.data.ShoppingList;


public class ListFragment extends Fragment implements DataListener {

    private ListAdapter adapter;
    private RecyclerView recyclerView;
    private static Context context;
    private View layout;

    private static void setContext(Context givenContext){
        context = givenContext;
    }


    public static ListFragment newInstance(Context context) {
        setContext(context);
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    private void initRecylerView(){
        recyclerView = layout.findViewById(R.id.rv_shopping_list);
        adapter = new ListAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    public ListFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        layout = getView();
        initRecylerView();
        updateRecyclerView();
    }

    private void updateRecyclerView(){
        List<ShoppingList> lists = ShoppingList.listAll(ShoppingList.class);
        for(ShoppingList list : lists){
            adapter.addItem(list);
        }
    }

    public static Context getGivenContext(){
        return context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_list, container, false);
    }

    @Override
    public void update() {
        initRecylerView();
        updateRecyclerView();
    }
}
