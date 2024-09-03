package com.khamdan.todo

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequestDto
    ): Response<MainResponse<LoginData>>

    @POST("register")
    suspend fun register(
        @Body registerRequest: UserAccountRequestDto
    ): Response<MainResponse<Unit>>

    @GET("checklist")
    suspend fun getChecklists(
        @Header("Authorization") authorization: String?
    ): Response<MainResponse<List<Checklist>>>

    @POST("checklist")
    suspend fun createChecklist(
        @Header("Authorization") authorization: String?,
        @Body checklistRequest: ChecklistRequestDto
    ): Response<MainResponse<Unit>>

    @DELETE("checklist/{checklistId}")
    suspend fun deleteChecklist(
        @Header("Authorization") authorization: String?,
        @Path("checklistId") checklistId: Int
    ): Response<MainResponse<Unit>>

    @POST("checklist/{checklistId}/item")
    suspend fun addChecklistItem(
        @Header("Authorization") authorization: String?,
        @Path("checklistId") checklistId: Int,
        @Body itemRequest: ChecklistItemRequestDto
    ): Response<MainResponse<Unit>>

    @PUT("checklist/{checklistId}/item/{id}")
    suspend fun updateItemStatus(
        @Header("Authorization") authorization: String?,
        @Path("checklistId") checklistId: Int,
        @Path("id") itemId: Int
    ): Response<MainResponse<Unit>>

    @PUT("checklist/{checklistId}/item/rename/{id}")
    suspend fun updateItemName(
        @Header("Authorization") authorization: String?,
        @Path("checklistId") checklistId: Int,
        @Path("id") itemId: Int,
        @Body itemRequest: ChecklistItemRequestDto
    ): Response<MainResponse<Unit>>

    @DELETE("checklist/{checklistId}/item/{id}")
    suspend fun deleteChecklistItem(
        @Header("Authorization") authorization: String?,
        @Path("checklistId") checklistId: Int,
        @Path("id") itemId: Int
    ): Response<MainResponse<Unit>>
}
