package com.cpr.ads.interfaces;

import com.cpr.ads.Utils.FolderPath;
import com.cpr.ads.model.ListData;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IGetApi {
    @GET(FolderPath.URL_API)
    Call<ListData> getListData();
}
