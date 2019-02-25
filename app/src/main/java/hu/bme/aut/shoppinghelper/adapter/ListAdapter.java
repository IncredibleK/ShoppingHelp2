package hu.bme.aut.shoppinghelper.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.shoppinghelper.R;
import hu.bme.aut.shoppinghelper.activities.MainActivity;
import hu.bme.aut.shoppinghelper.data.ShoppingItem;
import hu.bme.aut.shoppinghelper.data.ShoppingList;
import hu.bme.aut.shoppinghelper.fragments.CategoryFragment;
import hu.bme.aut.shoppinghelper.fragments.CategoryItemFragment;
import hu.bme.aut.shoppinghelper.fragments.ListFragment;
import hu.bme.aut.shoppinghelper.fragments.ListItemFragment;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private final List<ShoppingList> items;

    public ListAdapter(){
        items = new ArrayList<>();
    }
    @NonNull
    @Override
    public ListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.tile_list, parent, false);
        return new ListAdapter.ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ListViewHolder holder, int position) {
        ShoppingList item = items.get(position);
        holder.id.setText(Integer.toString(position + 1));
        holder.name.setText(item.getName());
        holder.item = item;
    }

    public void addItem(ShoppingList item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    private void deleteList(ShoppingList list){
        items.remove(list);
        notifyDataSetChanged();
        List<ShoppingItem> items = ShoppingItem.find(ShoppingItem.class, "shoppinglist = ?", new String (list.getId().toString()));
        for(ShoppingItem item : items){
            item.delete();
        }
        list.delete();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView name;
        ImageView iv_delete;
        ShoppingList item;

        ListViewHolder(View itemView) {
            super(itemView);
            if (itemView != null) {
                id = itemView.findViewById(R.id.tv_list_tile_id);
                name = itemView.findViewById(R.id.tv_list_name);
                iv_delete = itemView.findViewById(R.id.tv_delete_list);

                iv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteList(item);
                    }
                });

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentManager fragmentManager = MainActivity.fragmentManager;
                        ListItemFragment listItemFragment = ListItemFragment.newInstance(ListFragment.getGivenContext(),item);
                        fragmentManager.beginTransaction().replace(R.id.content_main, listItemFragment).addToBackStack(null).commit();
                    }
                });
            }
        }
    }
}