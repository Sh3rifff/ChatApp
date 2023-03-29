package az.sharif.chatapp.fragment.helper;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import az.sharif.chatapp.R;
import az.sharif.chatapp.activities.RegistrationActivity;
import az.sharif.chatapp.databinding.FragmentUserInfoBinding;
import az.sharif.chatapp.utilities.Constants;
import az.sharif.chatapp.utilities.PreferenceManager;

public class UserInfoFragment extends Fragment {

    private FragmentUserInfoBinding binding;
    private PreferenceManager preferenceManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentUserInfoBinding.bind(view);

        preferenceManager = new PreferenceManager(requireContext());

        setListeners();
    }

    private void setListeners() {
        binding.signOutImage.setOnClickListener(view -> signOut());
        binding.signOut.setOnClickListener(view -> signOut());
    }

    private void signOut() {
        Toast.makeText(requireActivity(), "Signing out...", Toast.LENGTH_SHORT).show();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID));
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates).addOnSuccessListener(unused -> {
            preferenceManager.clear();
            Intent intent = new Intent(requireActivity(), RegistrationActivity.class);
            startActivity(intent);
        }).addOnFailureListener(e ->
                Toast.makeText(requireActivity(), "unable to Sign out", Toast.LENGTH_SHORT).show()
        );
    }
}