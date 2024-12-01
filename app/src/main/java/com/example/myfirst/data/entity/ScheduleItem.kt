package com.example.myfirst.data.entity

data class ScheduleItem(
    val FacultyName: String,
    val Group: String,
    val PlanNumber: String,
    val TimeTable: TimeTable,
    val TimeTableBlockd: Int
)