package com.example.myapplication.diary

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * Хранит данные в том же файле, что и SessionManager: "beauty_tips_session"
 * Ключи вида: diary_<dateKey>
 */
class DiaryPrefsRepository private constructor(ctx: Context) {

    private val prefs: SharedPreferences =
        ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Небольшой "шлюз" событий для подписок
    private val updates = MutableSharedFlow<String>(extraBufferCapacity = 64)

    fun observeDay(dateKey: String): Flow<DayBucket> =
        updates
            .filter { it == dateKey }
            .onStart { emit(dateKey) }
            .map { loadBucket(dateKey) }

    suspend fun add(
        dateKey: String,
        slot: Slot,
        title: String,
        note: String?,
        reaction: Reaction
    ): Long {
        val list = loadList(dateKey).toMutableList()
        val item = DiaryItem(
            dateKey = dateKey,
            slot = slot,
            title = title,
            note = note,
            reaction = reaction
        )
        list.add(item)
        saveList(dateKey, list)
        updates.tryEmit(dateKey)
        return item.id
    }

    suspend fun remove(dateKey: String, id: Long) {
        val list = loadList(dateKey).toMutableList()
        val newList = list.filterNot { it.id == id }
        if (newList.size != list.size) {
            saveList(dateKey, newList)
            updates.tryEmit(dateKey)
        }
    }

    private fun loadBucket(dateKey: String): DayBucket {
        val items = loadList(dateKey)
        return DayBucket(
            morning = items.filter { it.slot == Slot.MORNING }.sortedBy { it.createdAt },
            day     = items.filter { it.slot == Slot.DAY     }.sortedBy { it.createdAt },
            evening = items.filter { it.slot == Slot.EVENING }.sortedBy { it.createdAt }
        )
    }

    private fun keyFor(dateKey: String) = "diary_$dateKey"

    private fun loadList(dateKey: String): List<DiaryItem> {
        val raw = prefs.getString(keyFor(dateKey), "[]") ?: "[]"
        val arr = JSONArray(raw)
        val result = ArrayList<DiaryItem>(arr.length())
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            result.add(
                DiaryItem(
                    id = o.optLong("id"),
                    dateKey = o.optString("dateKey"),
                    slot = Slot.valueOf(o.optString("slot")),
                    title = o.optString("title"),
                    note = if (o.isNull("note")) null else o.optString("note"),
                    reaction = Reaction.valueOf(o.optString("reaction", Reaction.NONE.name)),
                    createdAt = o.optLong("createdAt")
                )
            )
        }
        return result
    }

    private fun saveList(dateKey: String, list: List<DiaryItem>) {
        val arr = JSONArray()
        list.forEach { item ->
            arr.put(
                JSONObject().apply {
                    put("id", item.id)
                    put("dateKey", item.dateKey)
                    put("slot", item.slot.name)
                    put("title", item.title)
                    if (item.note != null) put("note", item.note) else put("note", JSONObject.NULL)
                    put("reaction", item.reaction.name)
                    put("createdAt", item.createdAt)
                }
            )
        }
        prefs.edit().putString(keyFor(dateKey), arr.toString()).apply()
    }

    suspend fun update(dateKey: String, id: Long, title: String, note: String?, reaction: Reaction): Boolean {
        val list = loadList(dateKey).toMutableList()
        val ix = list.indexOfFirst { it.id == id }
        if (ix == -1) return false
        val old = list[ix]
        list[ix] = old.copy(title = title, note = note, reaction = reaction)
        saveList(dateKey, list)
        updates.tryEmit(dateKey)
        return true
    }


    companion object {
        private const val PREFS_NAME = "beauty_tips_session"

        @Volatile private var INSTANCE: DiaryPrefsRepository? = null
        fun get(context: Context): DiaryPrefsRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: DiaryPrefsRepository(context.applicationContext).also { INSTANCE = it }
            }
    }
}
