package com.anubansal.githubpoc;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DataSource {

    String ENDPOINT = "http://api.github.com";

    @GET("/repos/{owner}/{repo}/pulls")
    Single<List<PullRequestModel>> getPRs(@Path("owner") String owner, @Path("repo") String repository);

}
