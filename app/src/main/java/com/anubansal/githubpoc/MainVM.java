package com.anubansal.githubpoc;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ObservableField;
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

public class MainVM extends AndroidViewModel {

    DataSource dataSource;
    public ObservableField<String> owner = new ObservableField<>();
    public ObservableField<String> repo = new ObservableField<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

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

        dataSource = retrofit.create(DataSource.class);
    }

    public void goClicked(PRListAdapter adapter) {
        compositeDisposable.add(dataSource.getPRs(owner.get(), repo.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getRepositoriesObserver(adapter)));
    }

    private DisposableSingleObserver<List<PullRequestModel>> getRepositoriesObserver(final PRListAdapter adapter) {
        return new DisposableSingleObserver<List<PullRequestModel>>() {
            @Override
            public void onSuccess(List<PullRequestModel> value) {
                if (!value.isEmpty()) {
                    adapter.setPullRequestModels(value);
                } else {
//                    TODO : show error case text
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Toast.makeText(getApplication().getApplicationContext(), "Error occurred! Please check input!", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }
}
