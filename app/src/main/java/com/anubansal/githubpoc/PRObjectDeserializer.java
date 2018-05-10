package com.anubansal.githubpoc;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class PRObjectDeserializer implements JsonDeserializer<PullRequestModel> {

    @Override
    public PullRequestModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        PullRequestModel pullRequestModel = new PullRequestModel();
        JsonObject repoObject = json.getAsJsonObject();
        pullRequestModel.state = repoObject.get("state").getAsString();
        pullRequestModel.url = repoObject.get("url").getAsString();
        return pullRequestModel;
    }
}
