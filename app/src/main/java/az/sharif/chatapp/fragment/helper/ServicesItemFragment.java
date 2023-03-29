package az.sharif.chatapp.fragment.helper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import az.sharif.chatapp.R;
import az.sharif.chatapp.databinding.FragmentServicesItemBinding;
import az.sharif.chatapp.models.ServiceModel;

public class ServicesItemFragment extends Fragment {

    private FragmentServicesItemBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_services_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentServicesItemBinding.bind(view);

        setArguments();
    }

    private void setArguments(){
        if (getArguments() != null) {
            final ServiceModel serviceModel = ServicesItemFragmentArgs.fromBundle(getArguments()).getService();
            binding.collapsingToolbar.setTitle(serviceModel.getName());
            binding.nurgunText.setText(serviceModel.getCategory());
            binding.serviceFragmentImage.setImageResource(serviceModel.getImage());
        }
    }
}