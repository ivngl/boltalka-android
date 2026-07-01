package com.example.boltalka.data.remote.api

import com.example.boltalka.data.remote.dto.ContactDto
import com.example.boltalka.data.remote.dto.CreateContactRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ContactApi {
    @GET("api/contacts")
    suspend fun getContacts(): List<ContactDto>

    @POST("api/contacts")
    suspend fun createContact(@Body request: CreateContactRequest): Map<String, Long>

    @DELETE("api/contacts/{id}")
    suspend fun deleteContact(@Path("id") id: Long): Response<Unit>
}
