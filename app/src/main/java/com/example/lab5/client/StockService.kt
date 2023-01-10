package com.example.lab5.client
import com.example.lab5.model.Stock
import retrofit2.Call
import retrofit2.http.*

interface StockService {
    @GET("/stocks/{id}")
    fun retrieveStock(@Path("id") id: Int) : Call<Stock>
    @GET("/stocks")
    fun retrieveAllStories() : Call<List<Stock>>
    @DELETE("/stocks/{id}")
    fun deleteStock(@Path("id") id: Long) : Call<Stock>
    @POST("/stocks")
    fun createStock(@Body stock: Stock) : Call<Stock>
    @PUT("/stocks/{id}")
    fun updateStock(@Path("id") id: Long, @Body stock: Stock) : Call<Stock>
}