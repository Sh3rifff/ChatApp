package az.sharif.chatapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import az.sharif.chatapp.databinding.CategoryRecyclerViewItemBinding;
import az.sharif.chatapp.models.CategoriesModel;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{


    ArrayList<CategoriesModel> categoriesLocations;

    public CategoryAdapter(ArrayList<CategoriesModel> categoriesLocations) {
        this.categoriesLocations = categoriesLocations;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CategoryRecyclerViewItemBinding categoryRecyclerViewItemBinding = CategoryRecyclerViewItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new CategoryViewHolder(categoryRecyclerViewItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        final CategoriesModel helperClass = categoriesLocations.get(position);
        holder.binding.motorImage.setImageResource(helperClass.getImage());
        holder.binding.motorName.setText(helperClass.getTitle());
    }

    @Override
    public int getItemCount() {
        return categoriesLocations.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        CategoryRecyclerViewItemBinding binding;

        public CategoryViewHolder(CategoryRecyclerViewItemBinding categoryRecyclerViewItemBinding) {
            super(categoryRecyclerViewItemBinding.getRoot());
            binding = categoryRecyclerViewItemBinding;
        }
    }
}
