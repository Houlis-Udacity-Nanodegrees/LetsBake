package com.xaris.xoulis.letsbake.data.api;

import android.arch.lifecycle.LiveData;

import com.xaris.xoulis.letsbake.data.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UdactiyService {
    @GET()
    LiveData<ApiResponse<List<Recipe>>> getRecipeList();
}
