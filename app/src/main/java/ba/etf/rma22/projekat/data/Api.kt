package ba.etf.rma22.projekat.data

import ba.etf.rma22.projekat.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface Api {
    @GET("anketa/{id}/pitanja")
    suspend fun getPitanja(@Path("id") id:Int): Response<MutableList<Pitanje>>

    @GET("anketa")
    suspend fun getAnkete(@Query("offset") offset: Int): Response<MutableList<Anketa>>

    @GET("anketa/{id}")
    suspend fun getAnketaById(@Path("id") id:Int): Response<Anketa>

    @GET("grupa")
    suspend fun getGrupe(): Response<List<Grupa>>

    @GET("istrazivanje")
    suspend fun getIstrazivanja(@Query("offset") offset: Int): Response<List<Istrazivanje>>

    @GET("istrazivanje/{id}")
    suspend fun getIstrazivanje(@Path("id") id:Int): Response<Istrazivanje>

    @GET("grupa/{id}")
    suspend fun getGrupaById(@Path("id") id:Int): Response<Grupa>

    @POST("grupa/{gid}/student/{id}")
    suspend fun upisiUGrupu(@Path("gid") gid:Int,@Path("id") id:String): Response<UpisPoruka>

    @GET("student/{id}/grupa")
    suspend fun getUpisaneGrupe(@Path("id") hash:String): Response<List<Grupa>>

    @GET("grupa/{id}/ankete")
    suspend fun getAnketeGrupe(@Path("id") id:Int): Response<List<Anketa>>

    @POST("student/{id}/anketa/{kid}")
    suspend fun zapocniOdgovaranje(@Path("id") id:String,@Path("kid") kid:Int): Response<AnketaTaken>

    @GET("student/{id}/anketataken")
    suspend fun getPokusaje(@Path("id") id:String): Response<List<AnketaTaken>>

    @GET("student/{id}/anketataken/{ktid}/odgovori")
    suspend fun getOdgovori(@Path("id") id:String,@Path("ktid") ktid:Int): Response<List<Odgovor1>>

    @POST("student/{id}/anketataken/{ktid}/odgovor")
    suspend fun dodajOdgovor(@Path("id") id:String,@Path("ktid")ktid:Int,@Body json:JsonZaOdgovor): Response<Odgovor1>

    @GET("grupa/{gid}/istrazivanje")
    suspend fun getIstrazivanjeGrupe(@Path("gid") gid:Int): Response<Istrazivanje>

    @GET("anketa/{id}/grupa")
    suspend fun getGrupeAnkete(@Path("id") id:Int): Response<List<Grupa>>
}