package com.example.finalsih;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.Toast;

import com.example.finalsih.Models.Articles;
import com.example.finalsih.Models.Headlines;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    final String API_KEY="d8f13911ec4b4565867df6c371250a03";
    Adapterxx adapter;
    List<Articles> articles=new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    String country;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipeRefresh);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        country=getCountry();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveJson(country,API_KEY);
            }
        });
        retrieveJson(country,API_KEY);
    }

    public void retrieveJson(String country, String apiKey) {
        //,"CATTLE","ACCIDENT","STRAY","ANIMALS","ACTIVIST","PETA","BLOG","ANIMAL CRUELTY","CATTLE CARE",
        swipeRefreshLayout.setRefreshing(true);
        Call<Headlines> call= null;
        call = ApiClient.getInstance().getApi().getHeadlines("animals","","",apiKey);
        call.enqueue(new Callback<Headlines>() {
            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                if(response.isSuccessful()&&response.body().getArticles()!=null){
                    swipeRefreshLayout.setRefreshing(false);
                    articles.clear();
                    articles=response.body().getArticles();
                    adapter=new Adapterxx(NewsActivity.this,articles);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Headlines> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(NewsActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public String getCountry(){
        Locale locale=Locale.getDefault();
        String country=locale.getCountry();
        //Toast.makeText(this,country,Toast.LENGTH_LONG).show();
        return country.toLowerCase();
    }
}