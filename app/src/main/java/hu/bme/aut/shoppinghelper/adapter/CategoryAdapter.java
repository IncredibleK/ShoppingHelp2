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
import hu.bme.aut.shoppinghelper.data.Category;
import hu.bme.aut.shoppinghelper.data.ShoppingItem;
import hu.bme.aut.shoppinghelper.fragments.CategoryFragment;
import hu.bme.aut.shoppinghelper.fragments.CategoryItemFragment;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final List<Category> items;

    public CategoryAdapter(){
        items = new ArrayList<>();
    }
    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.tile_category, parent, false);
        return new CategoryAdapter.CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        Category item = items.get(position);
        holder.id.setText(Integer.toString(position + 1));
        holder.name.setText(item.getName());
        holder.item = item;
    }

    public void addItem(Category item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    private void deleteCategory(Category category){
        items.remove(category);
        notifyDataSetChanged();
        List<ShoppingItem> items = ShoppingItem.find(ShoppingItem.class, "category = ?", new String (category.getId().toString()));
        for(ShoppingItem item : items){
            item.delete();
        }
        category.delete();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView name;
        Category item;
        ImageView iv_delete;


        CategoryViewHolder(View itemView) {
            super(itemView);
            if (itemView != null) {
                id = itemView.findViewById(R.id.tv_category_tile_id);
                name = itemView.findViewById(R.id.tv_category_name);
                iv_delete = itemView.findViewById(R.id.tv_delete_category);

                iv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteCategory(item);
                    }
                });

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentManager fragmentManager = MainActivity.fragmentManager;
                        CategoryItemFragment categoryItemFragment = CategoryItemFragment.newInstance(CategoryFragment.getGivenContext(),item);
                        fragmentManager.beginTransaction().replace(R.id.content_main, categoryItemFragment).addToBackStack(null).commit();
                    }
                });
            }
        }
    }
}