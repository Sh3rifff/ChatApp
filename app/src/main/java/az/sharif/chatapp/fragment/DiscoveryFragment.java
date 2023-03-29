package az.sharif.chatapp.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import az.sharif.chatapp.R;
import az.sharif.chatapp.adapters.CategoryAdapter;
import az.sharif.chatapp.adapters.ServicesAdapter;
import az.sharif.chatapp.databinding.FragmentDiscoveryBinding;
import az.sharif.chatapp.listeners.ServiceInterface;
import az.sharif.chatapp.models.CategoriesModel;
import az.sharif.chatapp.models.ServiceModel;
import az.sharif.chatapp.utilities.Constants;
import az.sharif.chatapp.utilities.PreferenceManager;

public class DiscoveryFragment extends Fragment implements ServiceInterface {

    FragmentDiscoveryBinding binding;
    ArrayList<ServiceModel> servicesList;
    ArrayList<CategoriesModel> categoriesHelperClasses;
    LocationManager locationManager;
    LocationListener locationListener;
    ActivityResultLauncher<String> permissionLauncher;
    private NavController navController;
    private PreferenceManager preferenceManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discovery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentDiscoveryBinding.bind(view);
        navController =  Navigation.findNavController(view);
        preferenceManager = new PreferenceManager(requireContext());

        registerLauncher();
        LocationUtils();
        localeName();
        ServiceRecycler();
        CategoriesRecycler();
        setListeners();

    }

    private void setListeners() {
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);

        binding.imageProfile.setOnClickListener(view -> navController.navigate(DiscoveryFragmentDirections.discoveryToInfo()));
    }

    private void CategoriesRecycler() {
        binding.categoryRecycler.setLayoutManager(new LinearLayoutManager(getContext()
                , LinearLayoutManager.HORIZONTAL, false));

        categoriesHelperClasses = new ArrayList<>();

        categoriesHelperClasses.add(new CategoriesModel(R.drawable.cp1, "Motors"));
        categoriesHelperClasses.add(new CategoriesModel(R.drawable.cp2, "Motors"));
        categoriesHelperClasses.add(new CategoriesModel(R.drawable.cp3, "Motors"));
        categoriesHelperClasses.add(new CategoriesModel(R.drawable.cp4, "Book"));
        categoriesHelperClasses.add(new CategoriesModel(R.drawable.cp1, "Pencil "));

        final CategoryAdapter adapter = new CategoryAdapter(categoriesHelperClasses);
        binding.categoryRecycler.setAdapter(adapter);

    }


    private void ServiceRecycler() {

        binding.serviceRecycler.setLayoutManager(new LinearLayoutManager
                (getContext(), LinearLayoutManager.VERTICAL, false));

        servicesList = new ArrayList<>();

        servicesList.add(new ServiceModel("Nurgul Motors", "Car garage", R.drawable.nurgun));
        servicesList.add(new ServiceModel("Nurgul Motors", "Car garage", R.drawable.cs));
        servicesList.add(new ServiceModel("Nurgul Motors", "Car garage", R.drawable.cs2));
        servicesList.add(new ServiceModel("Nurgul Motors", "Car garage", R.drawable.cs3));
        servicesList.add(new ServiceModel("Nurgul Motors", "Car garage", R.drawable.cs2));
        servicesList.add(new ServiceModel("Nurgul Motors", "Car garage", R.drawable.cs3));

        final ServicesAdapter adapter = new ServicesAdapter(servicesList, this);
        binding.serviceRecycler.setAdapter(adapter);
    }

    private void LocationUtils() {
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = location -> { };
    }

    private void registerLauncher() {
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if (result) {
                //Permission granted
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            } else {
                //Permission denied
                Toast.makeText(getContext(), "Permission needed!", Toast.LENGTH_SHORT).show();
            }
        });

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                Snackbar.make(binding.getRoot(), "Permission Needed for maps", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", v -> permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)).show();
            } else {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        }
    }

    private Location getLastKnownLocation() {

        if (ContextCompat.checkSelfPermission(requireActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            //Permission denied
            Toast.makeText(getContext(), "Permission needed!", Toast.LENGTH_SHORT).show();
        }
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

    }

    @SuppressLint("SetTextI18n")
    private void mapString() {

        double latitude = getLastKnownLocation().getLatitude();
        double longitude = getLastKnownLocation().getLongitude();

        try {
            Geocoder geo = new Geocoder(requireContext(), Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geo.getFromLocation(latitude, longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses == null) return;
            if (addresses.isEmpty()) {
                Toast.makeText(getContext(), "Waiting for Location", Toast.LENGTH_SHORT).show();
            } else {
                String name = addresses.get(0).getLocality() + "," + addresses.get(0).getCountryName();
                binding.currentLocation.setText(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void localeName() {

        new CountDownTimer(10, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                mapString();
            }
        }.start();
    }

    @Override
    public void onItemClick(int position) {
        navController.navigate(DiscoveryFragmentDirections.discoveryToServices(servicesList.get(position)));
    }
}