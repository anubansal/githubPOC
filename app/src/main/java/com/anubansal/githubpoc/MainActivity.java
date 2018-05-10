package com.anubansal.githubpoc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    EditText ownerEditText;
    EditText repoEditText;
    DataSource mDataSource;
    Button loadReposButtons;
    RecyclerView mRecyclerView;
    PRListAdapter prListAdapter;
//    MainActivityBinding mBinding;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        ownerEditText = findViewById(R.id.owner_edittext);
        repoEditText = findViewById(R.id.repo_edittext);
        loadReposButtons = findViewById(R.id.loadRepos_button);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setupDataSource();
    }

    private void setupDataSource() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .registerTypeAdapter(PullRequestModel.class, new PRObjectDeserializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DataSource.ENDPOINT)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mDataSource = retrofit.create(DataSource.class);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loadRepos_button:
                compositeDisposable.add(mDataSource.getPRs(ownerEditText.getText().toString(), repoEditText.getText().toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(getRepositoriesObserver()));
                break;
        }
    }

    private DisposableSingleObserver<List<PullRequestModel>> getRepositoriesObserver() {
        return new DisposableSingleObserver<List<PullRequestModel>>() {
            @Override
            public void onSuccess(List<PullRequestModel> value) {
                if (!value.isEmpty()) {
                    prListAdapter = new PRListAdapter(value);
                    mRecyclerView.setAdapter(prListAdapter);
                } else {
//                    TODO : show error case text
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Error occurred! Please check input!", Toast.LENGTH_SHORT).show();

            }
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }
}
