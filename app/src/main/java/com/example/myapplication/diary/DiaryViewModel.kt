package com.example.myapplication.diary

import androidx.lifecycle.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class UiState(
    val dateKey: String,
    val morning: List<DiaryItem> = emptyList(),
    val day: List<DiaryItem> = emptyList(),
    val evening: List<DiaryItem> = emptyList()
)

class DiaryViewModel(
    private val repo: DiaryPrefsRepository,
    savedState: SavedStateHandle
) : ViewModel() {

    private val _dateKey = MutableStateFlow(savedState["dateKey"] ?: todayKey())
    val dateKey: StateFlow<String> = _dateKey.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<UiState> =
        dateKey.flatMapLatest { key ->
            repo.observeDay(key).map { b -> UiState(key, b.morning, b.day, b.evening) }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, UiState(todayKey()))

    fun setDateKey(key: String) {
        viewModelScope.launch { _dateKey.emit(key) }
    }

    fun update(id: Long, title: String, note: String?, reaction: Reaction) {
        val key = dateKey.value
        viewModelScope.launch { repo.update(key, id, title, note, reaction) }
    }


    fun add(slot: Slot, title: String, note: String?, reaction: Reaction) {
        val key = dateKey.value
        viewModelScope.launch { repo.add(key, slot, title, note, reaction) }
    }

    fun delete(id: Long) {
        val key = dateKey.value
        viewModelScope.launch { repo.remove(key, id) }
    }

    companion object {
        fun todayKey(): String = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            .format(Date())
    }
}

class DiaryViewModelFactory(private val repo: DiaryPrefsRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DiaryViewModel(repo, SavedStateHandle()) as T
    }
}
