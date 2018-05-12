package com.anubansal.githubpoc;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.anubansal.githubpoc.databinding.MainActivityBinding;

public class MainActivity extends AppCompatActivity {


    PRListAdapter prListAdapter;
    private MainActivityBinding mBinding;
    MainVM mainVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        mainVM = new MainVM(getApplication());
        mBinding.setViewModel(mainVM);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        prListAdapter = new PRListAdapter();
        mBinding.setAdapter(prListAdapter);
        mBinding.recyclerView.setAdapter(prListAdapter);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loadRepos_button:
//                compositeDisposable.add(mDataSource.getPRs(ownerEditText.getText().toString(), repoEditText.getText().toString())
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeWith(getRepositoriesObserver()));
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
