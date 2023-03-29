package az.sharif.chatapp.listeners;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsApiClient {

    private static final String BASE_URL = "https://newsapi.org/v2/";
    private static NewsApiClient apiClient;
    private static Retrofit retrofit;

    private NewsApiClient(){
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static synchronized NewsApiClient getInstance(){
        if (apiClient == null){
            apiClient = new NewsApiClient();
        }
        return apiClient;
    }

    public NewsInterface getApi(){
        return retrofit.create(NewsInterface.class);
    }
}


//top-headlines?country=us&apiKey=cb783fc0425c4c71bf69e4a0bb4631c7