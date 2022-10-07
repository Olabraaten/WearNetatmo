package solutions.silly.wearnetatmo.api

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import solutions.silly.wearnetatmo.model.NetatmoToken
import solutions.silly.wearnetatmo.model.StationsData

interface NetatmoService {

    @FormUrlEncoded
    @POST("/oauth2/token")
    suspend fun getToken(
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("scope") scope: String,
    ): NetatmoToken

    @FormUrlEncoded
    @POST("/oauth2/token")
    suspend fun refreshToken(
        @Field("grant_type") grantType: String,
        @Field("refresh_token") refreshToken: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String
    ): NetatmoToken

    @GET("/api/getstationsdata")
    suspend fun getStationsData(
        @Header("Authorization") bearerToken: String
    ): StationsData
}