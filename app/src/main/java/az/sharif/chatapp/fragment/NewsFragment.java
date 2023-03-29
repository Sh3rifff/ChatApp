package az.sharif.chatapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;
import java.util.Locale;

import az.sharif.chatapp.R;
import az.sharif.chatapp.adapters.NewsAdapter;
import az.sharif.chatapp.databinding.FragmentNewsBinding;
import az.sharif.chatapp.listeners.NewsApiClient;
import az.sharif.chatapp.models.Article;
import az.sharif.chatapp.models.Headlines;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment implements NewsAdapter.ItemClickListener {

    NewsAdapter newsAdapter;
    private FragmentNewsBinding binding;
    final String API_KEY = "cb783fc0425c4c71bf69e4a0bb4631c7";
    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentNewsBinding.bind(view);
        navController = Navigation.findNavController(view);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        newsAdapter = new NewsAdapter(this);
        binding.recyclerView.setAdapter(newsAdapter);

        String country = getCountry();

        binding.swipeRefresh.setOnRefreshListener(() -> retrieveJson("", country, API_KEY));
        retrieveJson("", country, API_KEY);

        binding.buttonSearch.setOnClickListener(v -> {
            final String query = binding.editTextQuery.getText().toString();
            binding.swipeRefresh.setOnRefreshListener(() -> retrieveJson(query, country, API_KEY));
            retrieveJson(query, country, API_KEY);
        });
    }

    public void retrieveJson(String query, String country, String apiKey) {

        binding.swipeRefresh.setRefreshing(true);
        Call<Headlines> call;
        if (!binding.editTextQuery.getText().toString().equals("")) {
            call = NewsApiClient.getInstance().getApi().getSpecificData(query, apiKey);
        } else {
            call = NewsApiClient.getInstance().getApi().getHeadlines(country, apiKey);
        }
        call.enqueue(new Callback<Headlines>() {
            @Override
            public void onResponse(@NonNull Call<Headlines> call, @NonNull Response<Headlines> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getArticles() != null) {
                        binding.swipeRefresh.setRefreshing(false);
                        final List<Article> articles = response.body().getArticles();
                        newsAdapter.setArticles(articles);
                        newsAdapter.notifyItemRangeChanged(0, articles.size());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Headlines> call, @NonNull Throwable t) {
                binding.swipeRefresh.setRefreshing(false);
                Toast.makeText(requireContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getCountry() {
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }

    @Override
    public void onItemClicked(Article article) {
        navController.navigate(NewsFragmentDirections.newsToDetails(article));
    }
}