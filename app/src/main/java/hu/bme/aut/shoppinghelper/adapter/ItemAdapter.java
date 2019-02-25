package hu.bme.aut.shoppinghelper.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.shoppinghelper.R;
import hu.bme.aut.shoppinghelper.data.Category;
import hu.bme.aut.shoppinghelper.data.ShoppingItem;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private final List<ShoppingItem> items;

    public ItemAdapter(){
        items = new ArrayList<>();
    }
    @NonNull
    @Override
    public ItemAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.tile_item, parent, false);
        return new ItemAdapter.ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ItemViewHolder holder, int position) {
        ShoppingItem item = items.get(position);
        holder.id.setText(Integer.toString(position + 1));
        holder.name.setText(item.getName());
        holder.listName.setText(item.getShoppingList().getName());
        holder.amount.setText(item.getDescription());
    }

    public void addItem(ShoppingItem item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView name;
        TextView listName;
        TextView amount;


        ItemViewHolder(View itemView) {
            super(itemView);
            if (itemView != null) {
                id = itemView.findViewById(R.id.tv_item_tile_id);
                name = itemView.findViewById(R.id.tv_item_name);
                listName = itemView.findViewById(R.id.tv_item_list_name);
                amount = itemView.findViewById(R.id.tv_tile_item_descr);
            }
        }
    }
}