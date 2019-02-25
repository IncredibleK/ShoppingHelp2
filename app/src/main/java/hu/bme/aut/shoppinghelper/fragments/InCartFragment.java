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
import android.widget.TextView;

import java.util.List;

import hu.bme.aut.shoppinghelper.R;
import hu.bme.aut.shoppinghelper.adapter.InCartAdapter;
import hu.bme.aut.shoppinghelper.adapter.ListItemAdapter;
import hu.bme.aut.shoppinghelper.data.ShoppingItem;
import hu.bme.aut.shoppinghelper.data.ShoppingList;

public class InCartFragment extends Fragment implements InCartAdapter.CartItemClickListener{

    private InCartAdapter adapter;
    private RecyclerView recyclerView;
    private static Context context;
    private static ShoppingList currentList;
    private TextView shoplistName;
    private TextView price;
    private static int totalPrice;
    private View layout;

    public InCartFragment() {
        totalPrice = 0;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_in_cart, container, false);
    }

    private static void setContext(Context givenContext){
        context = givenContext;
    }

    private static void setCurrentList(ShoppingList list){
        currentList = list;
    }

    public static InCartFragment newInstance(Context context, ShoppingList list) {
        setContext(context);
        setCurrentList(list);
        InCartFragment fragment = new InCartFragment();
        return fragment;
    }

    private void initRecylerView(){
        recyclerView = layout.findViewById(R.id.rv_in_cart);
        adapter = new InCartAdapter(this);
        adapter.setContext(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        totalPrice = 0;
        price.setText(Integer.toString(totalPrice));
    }


    @Override
    public void onResume() {
        super.onResume();
        initRecylerView();
        updateRecyclerView();
        price.setText(Integer.toString(totalPrice));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        layout = this.getView();
        if(layout!=null) {



            shoplistName = layout.findViewById(R.id.tv_in_cart_name);
            price = layout.findViewById(R.id.tv_in_cart_price_amount);

            shoplistName.setText(currentList.getName()+" in cart items");
        }
    }

    private void updateRecyclerView(){
        List<ShoppingItem> items = ShoppingItem.find(ShoppingItem.class, "shoppinglist = ?", new String (currentList.getId().toString()));
        for(ShoppingItem item : items){
            if(item.isIncart()) {
                adapter.addItem(item);
                totalPrice+=item.getPrice();
            }
        }
    }


    @Override
    public void onItemRemoved(int value) {
        totalPrice-=value;
        price.setText(Integer.toString(totalPrice));
    }
}
