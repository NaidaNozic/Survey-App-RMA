package ba.etf.rma22.projekat.data

import ba.etf.rma22.projekat.data.repositories.ApiConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiAdapter {
    val retrofit : Api = Retrofit.Builder()
        .baseUrl(ApiConfig.getBaseUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Api::class.java)
}