package com.khamdan.todo

data class MainResponse<T>(
    val statusCode: String,
    val message: String,
    val errorMessage: String? = null,
    val data: T? = null
)

data class LoginData(
    val token: String
)

data class LoginRequestDto(
    val username: String,
    val password: String
)

data class UserAccountRequestDto(
    val username: String,
    val email: String,
    val password: String
)

data class ChecklistRequestDto(
    val name: String
)

data class ChecklistItemRequestDto(
    val itemName: String
)

data class Checklist(
    val id: Int,
    val name: String,
    val items: List<ChecklistItem> = emptyList(),
    val checklistCompletionStatus: Boolean
)

data class ChecklistItem(
    val id: Int,
    val name: String,
    val itemCompletionStatus: Boolean
)

data class InputData(
    val inputActive: Boolean = false,
    val text: String = "",
    val operation: Operation = Operation.ADD_CHECKLIST,
    val checklistId: Int? = null,
    val checklistItemId: Int? = null,
)

enum class Operation {
    ADD_CHECKLIST,
    ADD_CHECKLIST_ITEM,
    EDIT_CHECKLIST_ITEM,
}