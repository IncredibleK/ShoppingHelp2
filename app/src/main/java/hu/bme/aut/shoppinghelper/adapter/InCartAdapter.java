package hu.bme.aut.shoppinghelper.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.shoppinghelper.R;
import hu.bme.aut.shoppinghelper.data.ShoppingItem;

public class InCartAdapter extends RecyclerView.Adapter<InCartAdapter.CartViewHolder> {

    private final List<ShoppingItem> items;
    private InCartAdapter.CartItemClickListener listener;
    private Context context;

    public interface CartItemClickListener{
        public void onItemRemoved(int price);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public InCartAdapter() {
        items = new ArrayList<>();
    }

    public InCartAdapter(InCartAdapter.CartItemClickListener listener) {
        this.listener = listener;
        items = new ArrayList<>();
    }

    @NonNull
    @Override
    public InCartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.tile_in_cart_item, parent, false);
        return new InCartAdapter.CartViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final InCartAdapter.CartViewHolder holder,final int position) {
        final ShoppingItem item = items.get(position);
        holder.id.setText(Integer.toString(position + 1));
        holder.categoryName.setText(item.getCategory().getName());
        holder.name.setText(item.getName());
        holder.amount.setText(item.getDescription());
        holder.item = item;
        holder.price.setText(Integer.toString(item.getPrice()));


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    removeItem(items.get(position));
                    listener.onItemRemoved(item.getPrice());
            }
        });
    }

    public void addItem(ShoppingItem item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void removeItem(ShoppingItem item) {
        item.setIncart(false);
        item.setPrice(0);
        item.save();
        items.remove(item);
        notifyDataSetChanged();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView name;
        TextView categoryName;
        TextView amount;
        TextView price;
        ImageView delete;
        ShoppingItem item;

        CartViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.tv_item_list_id);
            name = itemView.findViewById(R.id.tv_list_item_name);
            price = itemView.findViewById(R.id.tv_pricetag);
            delete = itemView.findViewById(R.id.iv_in_cart_redo);
            categoryName = itemView.findViewById(R.id.tv_item_category_name);
            amount = itemView.findViewById(R.id.tv_list_item_descr);
        }
    }
}