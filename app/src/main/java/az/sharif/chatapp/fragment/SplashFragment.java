package az.sharif.chatapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import az.sharif.chatapp.R;
import az.sharif.chatapp.activities.MainActivity;
import az.sharif.chatapp.databinding.FragmentSplashBinding;
import az.sharif.chatapp.utilities.Constants;
import az.sharif.chatapp.utilities.PreferenceManager;

public class SplashFragment extends Fragment {

    private FragmentSplashBinding binding;
    private NavController navController;
    private PreferenceManager preferenceManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentSplashBinding.bind(view);
        navController = Navigation.findNavController(view);
        preferenceManager = new PreferenceManager(requireContext());

        navigation();
    }

    private void navigation() {
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            Intent intent = new Intent(requireContext(), MainActivity.class);
            startActivity(intent);
        }
        binding.startButton.setOnClickListener(view -> navController.navigate(SplashFragmentDirections.splashToSignIn()));
    }
}