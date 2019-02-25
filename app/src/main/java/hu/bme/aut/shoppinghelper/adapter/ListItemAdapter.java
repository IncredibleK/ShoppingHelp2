package hu.bme.aut.shoppinghelper.adapter;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.shoppinghelper.R;
import hu.bme.aut.shoppinghelper.data.ShoppingItem;

public class ListItemAdapter  extends RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder> {

    private final List<ShoppingItem> items;
    private Context context;

    public void setContext(Context context){
        this.context = context;
    }

    public ListItemAdapter(){
        items = new ArrayList<>();
    }
    @NonNull
    @Override
    public ListItemAdapter.ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.tile_list_item, parent, false);
        return new ListItemAdapter.ListItemViewHolder(itemView);
    }

    private void createAlertMessage(final ShoppingItem item){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        builder.setTitle("Empty price")
                .setMessage("You didn't fill out the price, this way the assigned value will be 0!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        removeItem(item,0);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ListItemAdapter.ListItemViewHolder holder, final int position) {
        ShoppingItem item = items.get(position);

        holder.id.setText(Integer.toString(position + 1));
        holder.categoryName.setText(item.getCategory().getName());
        holder.name.setText(item.getName());
        holder.amount.setText(item.getDescription());
        holder.item = item;


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tryParseInt(holder.price.getText().toString())) {
                    int value = Integer.parseInt(holder.price.getText().toString()) ;
                    items.get(position).setPrice(value);
                    Log.d("delete", "delete \"" + value + "\"");
                    removeItem(items.get(position), value);
                }
                else{
                    createAlertMessage(items.get(position));
                }
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

    public void removeItem(ShoppingItem item, int price){
        item.setIncart(true);
        item.setPrice(price);
        item.save();
        items.remove(item);
        notifyDataSetChanged();
    }

    class ListItemViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView name;
        TextView categoryName;
        TextView amount;
        EditText price;
        ImageView delete;
        ShoppingItem item;


        ListItemViewHolder(View itemView) {
            super(itemView);
            if (itemView != null) {
                id = itemView.findViewById(R.id.tv_item_list_id);
                name = itemView.findViewById(R.id.tv_list_item_name);
                price = itemView.findViewById(R.id.et_pricetag);
                delete = itemView.findViewById(R.id.iv_delete_image);
                categoryName = itemView.findViewById(R.id.tv_item_category_name);
                amount = itemView.findViewById(R.id.tv_list_item_descr);
            }
        }
    }
}