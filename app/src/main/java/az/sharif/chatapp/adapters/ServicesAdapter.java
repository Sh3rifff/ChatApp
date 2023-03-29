package az.sharif.chatapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import az.sharif.chatapp.databinding.ServisRecyclerRowItemBinding;
import az.sharif.chatapp.listeners.ServiceInterface;
import az.sharif.chatapp.models.ServiceModel;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServicesViewHolder> {

    private final List<ServiceModel> serviceModels;
    private final ServiceInterface serviceInterface;

    public ServicesAdapter(List<ServiceModel> serviceModels, ServiceInterface serviceInterface) {
        this.serviceModels = serviceModels;
        this.serviceInterface = serviceInterface;
    }

    @NonNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ServisRecyclerRowItemBinding servisRecyclerRowItemBinding = ServisRecyclerRowItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ServicesViewHolder(servisRecyclerRowItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, int position) {
        holder.setServicesData(serviceModels.get(position));
    }

    @Override
    public int getItemCount() {
        return serviceModels.size();
    }

    public class ServicesViewHolder extends RecyclerView.ViewHolder {

        ServisRecyclerRowItemBinding binding;

        public ServicesViewHolder(ServisRecyclerRowItemBinding servisRecyclerRowItemBinding) {
            super(servisRecyclerRowItemBinding.getRoot());
            binding = servisRecyclerRowItemBinding;
        }

        @SuppressWarnings("deprecation")
        void setServicesData(ServiceModel serviceModel) {
            binding.serviceName.setText(serviceModel.getName());
            binding.serviceCategory.setText(serviceModel.getCategory());
            binding.serviceImage.setImageResource(serviceModel.getImage());
            binding.getRoot().setOnClickListener(v -> serviceInterface.onItemClick(getAdapterPosition()));

        }
    }
}
