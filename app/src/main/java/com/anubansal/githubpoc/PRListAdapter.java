package com.anubansal.githubpoc;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PRListAdapter extends RecyclerView.Adapter<PRListAdapter.PRViewHolder> {

    private List<PullRequestModel> pullRequestModels;

    public PRListAdapter(List<PullRequestModel> pullRequestModels) {
        this.pullRequestModels = pullRequestModels;
    }

    @NonNull
    @Override
    public PRViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PRViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pr_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PRViewHolder holder, int position) {
        PullRequestModel prModel = pullRequestModels.get(position);
        holder.title.setText(prModel.url);
        Linkify.addLinks(holder.title, Linkify.WEB_URLS);
    }

    @Override
    public int getItemCount() {
        return pullRequestModels.size();
    }

    public class PRViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public PRViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
        }

    }
}
