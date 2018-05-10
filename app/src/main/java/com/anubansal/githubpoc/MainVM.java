package com.anubansal.githubpoc;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainVM extends AndroidViewModel {

    DataSource githubAPI;

    public MainVM(Application application) {
        super(application);
        createGithubAPI();
    }

    private void createGithubAPI() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .registerTypeAdapter(PullRequestModel.class, new PRObjectDeserializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DataSource.ENDPOINT)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        githubAPI = retrofit.create(DataSource.class);
    }

}
