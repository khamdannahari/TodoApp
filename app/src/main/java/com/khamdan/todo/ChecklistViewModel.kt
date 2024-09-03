package com.khamdan.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response

class ChecklistViewModel(private val repository: ChecklistRepository) : ViewModel() {

    private val _checklists = MutableLiveData<List<Checklist>>()
    val checklists: LiveData<List<Checklist>> get() = _checklists

    private val _inputData = MutableLiveData<InputData>()
    val inputData: LiveData<InputData> get() = _inputData

    private val _triggerToast = MutableLiveData<Pair<Boolean, String?>?>(null)
    val triggerToast: LiveData<Pair<Boolean, String?>?> get() = _triggerToast

    fun isLogin() = repository.isLogin()

    fun checkIfLogin(onResult: () -> Unit) {
        if (repository.isLogin()) {
            onResult()
        }
    }

    fun login(username: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val response = repository.login(username, password)
            onResult(response.isSuccessful)
        }
    }

    fun openRegisterPage(navigateRegisterPage: () -> Unit) {
        navigateRegisterPage()
    }

    fun register(username: String, email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val response = repository.register(username, email, password)
            onResult(response.isSuccessful)
        }
    }

    fun getChecklists() {
        viewModelScope.launch {
            val response = repository.getChecklists()
            handleApiResponse(response) {
                _checklists.postValue(it.data ?: emptyList())
            }
        }
    }

    fun actionSave(text: String) {
        val input = _inputData.value ?: return
        when (input.operation) {
            Operation.ADD_CHECKLIST -> createChecklist(text)
            Operation.ADD_CHECKLIST_ITEM -> input.checklistId?.let { createChecklistItem(it, text) }
            Operation.EDIT_CHECKLIST_ITEM -> input.checklistId?.let { checklistId ->
                input.checklistItemId?.let { itemId ->
                    editChecklistItem(
                        checklistId,
                        itemId,
                        text
                    )
                }
            }
        }
    }

    fun actionCancel() {
        resetInputData()
    }

    fun actionAddChecklist() {
        _inputData.postValue(
            InputData(
                inputActive = true,
                operation = Operation.ADD_CHECKLIST
            )
        )
    }

    fun actionAddChecklistItem(checklistId: Int) {
        _inputData.postValue(
            InputData(
                inputActive = true,
                checklistId = checklistId,
                operation = Operation.ADD_CHECKLIST_ITEM
            )
        )
    }

    fun actionEditChecklistItem(checklistId: Int, checklistItemId: Int, text:String) {
        _inputData.postValue(
            InputData(
                inputActive = true,
                text = text,
                checklistId = checklistId,
                checklistItemId = checklistItemId,
                operation = Operation.EDIT_CHECKLIST_ITEM
            )
        )
    }

    private fun createChecklist(name: String) {
        viewModelScope.launch {
            val response = repository.createChecklist(name)
            handleApiResponse(response) { getChecklists() }
        }
    }

    fun deleteChecklist(checklistId: Int) {
        viewModelScope.launch {
            val response = repository.deleteChecklist(checklistId)
            handleApiResponse(response) { getChecklists() }
        }
    }

    private fun createChecklistItem(checklistId: Int, itemName: String) {
        viewModelScope.launch {
            val response = repository.addChecklistItem(checklistId, itemName)
            handleApiResponse(response) { getChecklists() }
        }
    }

    fun editChecklistItemStatus(checklistId: Int, itemId: Int) {
        viewModelScope.launch {
            val response = repository.updateItemStatus(checklistId, itemId)
            handleApiResponse(response) { getChecklists() }
        }
    }

    private fun editChecklistItem(checklistId: Int, itemId: Int, name: String) {
        viewModelScope.launch {
            val response = repository.updateItemName(checklistId, itemId, name)
            handleApiResponse(response) { getChecklists() }
        }
    }

    fun deleteChecklistItem(checklistId: Int, itemId: Int) {
        viewModelScope.launch {
            val response = repository.deleteChecklistItem(checklistId, itemId)
            handleApiResponse(response) { getChecklists() }
        }
    }

    private fun <T> handleApiResponse(response: Response<T>, onSuccess: (T) -> Unit) {
        if (response.isSuccessful) {
            response.body()?.let { onSuccess(it) }
        } else {
            _triggerToast.postValue(
                Pair(!(_triggerToast.value?.first ?: false), response.message())
            )
        }
    }

    private fun resetInputData() {
        _inputData.postValue(
            InputData(
                inputActive = false,
                text = "",
                operation = Operation.ADD_CHECKLIST,
                checklistId = null,
                checklistItemId = null
            )
        )
    }
}
