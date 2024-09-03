package com.khamdan.todo

class ChecklistRepository(
    private val apiService: ApiService,
    private val tokenPreference: TokenPreference
) {

    fun isLogin() = tokenPreference.getToken()?.isNotBlank() ?: false

    suspend fun login(username: String, password: String) =
        apiService.login(LoginRequestDto(username, password)).also { response ->
            response.body()?.data?.token?.let {
                tokenPreference.saveToken(it)
            }
        }

    suspend fun register(username: String, email: String, password: String) =
        apiService.register(UserAccountRequestDto(username, email, password))

    suspend fun getChecklists() = apiService.getChecklists(tokenPreference.getToken())

    suspend fun createChecklist(name: String) =
        apiService.createChecklist(
            tokenPreference.getToken(),
            ChecklistRequestDto(name)
        )

    suspend fun deleteChecklist(checklistId: Int) =
        apiService.deleteChecklist(tokenPreference.getToken(), checklistId)

    suspend fun addChecklistItem(checklistId: Int, itemName: String) =
        apiService.addChecklistItem(
            tokenPreference.getToken(),
            checklistId,
            ChecklistItemRequestDto(itemName)
        )

    suspend fun updateItemStatus(checklistId: Int, itemId: Int) =
        apiService.updateItemStatus(tokenPreference.getToken(), checklistId, itemId)

    suspend fun updateItemName(checklistId: Int, itemId: Int, name: String) =
        apiService.updateItemName(
            tokenPreference.getToken(),
            checklistId,
            itemId,
            ChecklistItemRequestDto(itemName = name)
        )

    suspend fun deleteChecklistItem(checklistId: Int, itemId: Int) =
        apiService.deleteChecklistItem(tokenPreference.getToken(), checklistId, itemId)

}
