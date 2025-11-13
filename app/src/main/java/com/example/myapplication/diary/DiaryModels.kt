package com.example.myapplication.diary

enum class Slot { MORNING, DAY, EVENING }
enum class Reaction { NONE, GOOD, NEUTRAL, BAD }

data class DiaryItem(
    val id: Long = System.currentTimeMillis(),
    val dateKey: String,                 // yyyy-MM-dd
    val slot: Slot,
    val title: String,
    val note: String? = null,
    val reaction: Reaction = Reaction.NONE,
    val createdAt: Long = System.currentTimeMillis()
)

data class DayBucket(
    val morning: List<DiaryItem>,
    val day: List<DiaryItem>,
    val evening: List<DiaryItem>
)
