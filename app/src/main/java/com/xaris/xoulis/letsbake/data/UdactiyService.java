package com.xaris.xoulis.letsbake.data;

import com.xaris.xoulis.letsbake.data.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UdactiyService {
    String RECIPES_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    @GET()
    Call<List<Recipe>> getRecipeList();
}
