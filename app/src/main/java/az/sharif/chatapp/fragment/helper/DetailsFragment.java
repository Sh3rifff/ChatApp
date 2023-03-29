package az.sharif.chatapp.fragment.helper;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import az.sharif.chatapp.R;
import az.sharif.chatapp.databinding.FragmentDetailsBinding;
import az.sharif.chatapp.models.Article;

public class DetailsFragment extends Fragment {

    FragmentDetailsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentDetailsBinding.bind(view);

        setArguments();

    }
    @SuppressLint("SetJavaScriptEnabled")
    private void setArguments(){
        if (getArguments() != null) {
            final Article article = DetailsFragmentArgs.fromBundle(getArguments()).getArticle();
            binding.tvTitle.setText(article.getTitle());
            binding.tvSource.setText(article.getSource().getName());
            binding.tvDesc.setText(article.getDescription());
            binding.tvDate.setText(article.getPublishedAt());

            Picasso.with(binding.imageView.getContext()).load(article.getUrlToImage()).into(binding.imageView);

            binding.webView.getSettings().setDomStorageEnabled(true);
            binding.webView.getSettings().setJavaScriptEnabled(true);
            binding.webView.getSettings().setLoadsImagesAutomatically(true);
            binding.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            binding.webView.setWebViewClient(new WebViewClient());
            binding.webView.loadUrl(article.getUrl());
        }
    }
}