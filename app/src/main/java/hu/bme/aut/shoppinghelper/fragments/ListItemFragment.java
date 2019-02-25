package hu.bme.aut.shoppinghelper.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.shoppinghelper.R;
import hu.bme.aut.shoppinghelper.adapter.ItemAdapter;
import hu.bme.aut.shoppinghelper.adapter.ListAdapter;
import hu.bme.aut.shoppinghelper.adapter.ListItemAdapter;
import hu.bme.aut.shoppinghelper.data.Category;
import hu.bme.aut.shoppinghelper.data.ShoppingItem;
import hu.bme.aut.shoppinghelper.data.ShoppingList;

public class ListItemFragment extends Fragment {

    private ListItemAdapter adapter;
    private RecyclerView recyclerView;
    private static Context context;
    private static ShoppingList currentList;
    private TextView shoplistName;
    private FloatingActionButton fab;
    private View layout;
    private List<Category> categories;
    private List<String> categoryNames;
    private Button bt_in_cart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_item, container, false);
    }

    public ListItemFragment(){}


    private static void setContext(Context givenContext){
        context = givenContext;
    }

    private static void setCurrentList(ShoppingList list){
        currentList = list;
    }

    public static ListItemFragment newInstance(Context context, ShoppingList list) {
        setContext(context);
        setCurrentList(list);
        ListItemFragment fragment = new ListItemFragment();
        return fragment;
    }

    private void initRecylerView(){
        recyclerView = layout.findViewById(R.id.rv_list_items);
        adapter = new ListItemAdapter();
        adapter.setContext(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    private int getCategoryPosition(String value){
        for(int i = 0;i<categories.size();i++){
            if(categories.get(i).getName().equals(value))
                return i;
        }
        return -1;
    }

    private int validate(String name){
        if(name.length()<3||name.length()>12)
            return 1;
        List<ShoppingItem> items = ShoppingItem.listAll(ShoppingItem.class);
        for(ShoppingItem item : items){
            if(item.getName().equals(name)&& item.getShoppingList().getName().equals(currentList.getName()))
                return 2;
        }
        return 0;
    }

    private void createShopItemAlertDialog(){
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_create_item, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setView(view);
        alert.setTitle("Create an item");

        final EditText et_name = view.findViewById(R.id.et_fragment_list_name);
        final Spinner sp_category = view.findViewById(R.id.spinner);
        final EditText et_amount = view.findViewById(R.id.et_create_item_amount);
        ArrayAdapter<String> sp_adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, categoryNames);
        sp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_category.setAdapter(sp_adapter);

        alert.setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String name = et_name.getText().toString();
                String amount = et_amount.getText().toString();
                if(sp_category.getSelectedItem()!=null) {
                    int categoryPosition = getCategoryPosition(sp_category.getSelectedItem().toString());
                    Category category = categories.get(categoryPosition);
                    int validationNumber = validate(name);
                    if (validationNumber == 0) {
                        if (validateAmount(amount)) {
                            ShoppingItem shoppingItem = new ShoppingItem();
                            shoppingItem.setName(name);
                            shoppingItem.setCategory(category);
                            shoppingItem.setShoppingList(currentList);
                            shoppingItem.setDescription(amount);
                            shoppingItem.save();
                            adapter.addItem(shoppingItem);
                        } else
                            Snackbar.make(getActivity().findViewById(R.id.content_main), "The amount of an item must be between 1 and 10 characters!", Snackbar.LENGTH_LONG).show();

                    } else if (validationNumber == 1)
                        Snackbar.make(getActivity().findViewById(R.id.content_main), "The name of an item must be between 3 and 12 characters!", Snackbar.LENGTH_LONG).show();
                    else
                        Snackbar.make(getActivity().findViewById(R.id.content_main), "The item name already exists in the list!", Snackbar.LENGTH_LONG).show();
                }
                else
                    Snackbar.make(getActivity().findViewById(R.id.content_main), "There are no categories, please create a category before adding an item!", Snackbar.LENGTH_LONG).show();
            }
        });

        alert.setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        Dialog dialog = alert.create();
        dialog.show();
    }

    private boolean validateAmount(String word){
        if(word.length()<1||word.length()>10)
            return false;
        return true;
    }

    private void initCategories(){
        categories = Category.listAll(Category.class);
        categoryNames = new ArrayList<>();
        for(int i = 0; i<categories.size();i++)
            categoryNames.add(categories.get(i).getName());
    }

    private void createInCartFragment(){
            FragmentManager fragmentManager = getFragmentManager();
            InCartFragment inCartFragment = InCartFragment.newInstance(getContext(),currentList);
            fragmentManager.beginTransaction().replace(R.id.content_main, inCartFragment).addToBackStack(null).commit();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        layout = this.getView();
        if(layout!=null) {
            initRecylerView();
            updateRecyclerView();
            fab = layout.findViewById(R.id.fab);
            shoplistName = layout.findViewById(R.id.tv_Shoplist_name);
            bt_in_cart = layout.findViewById(R.id.bt_in_cart);

            fab.setBackgroundColor(Color.WHITE);
            shoplistName.setText(currentList.getName()+" not in cart items");

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean exists  =  ShoppingList.find(ShoppingList.class, "name = ?", currentList.getName()).size() != 0;
                    if(exists) {
                        initCategories();
                        createShopItemAlertDialog();
                    }
                    else
                        Snackbar.make(getActivity().findViewById(R.id.content_main), "This list no longer exists!", Snackbar.LENGTH_LONG).show();
                }
            });

            bt_in_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean exists  =  ShoppingList.find(ShoppingList.class, "name = ?", currentList.getName()).size() != 0;
                    if(exists) {
                        createInCartFragment();
                    }
                    else
                        Snackbar.make(getActivity().findViewById(R.id.content_main), "This list no longer exists!", Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    private void updateRecyclerView(){
        List<ShoppingItem> items = ShoppingItem.find(ShoppingItem.class, "shoppinglist = ?", new String (currentList.getId().toString()));
        for(ShoppingItem item : items){
            if(!item.isIncart())
              adapter.addItem(item);
        }
    }
}
