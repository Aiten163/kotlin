package com.example.myfirst.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myfirst.data.entity.Lesson
import com.example.myfirst.data.entity.Schedule
import com.example.myfirst.data.entity.ScheduleItem
import com.example.myfirst.data.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleFragMVVM(val token: String) : ViewModel() {
    private val TAG: String = "ScheduleMVVM"
    private val mutableStudent: MutableLiveData<List<Lesson>> = MutableLiveData()

    // Метод для получения расписания
    fun getSchedule(date: String?) {
        RetrofitInstance.mrsuApi.getSchedule("Bearer $token", date)
            .enqueue(object : Callback<Schedule> {  // Ожидаем объект Schedule
                override fun onResponse(call: Call<Schedule>, response: Response<Schedule>) {
                    if (response.isSuccessful) {
                        val schedule = response.body()
                        if (schedule != null) {
                            // Объединяем все уроки из всех ScheduleItem
                            val allLessons = mergeLessons(schedule)
                            // Присваиваем объединенный список уроков в mutableStudent
                            mutableStudent.value = allLessons
                        }
                    } else {
                        Log.e(TAG, "Response error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Schedule>, t: Throwable) {
                    Log.e(TAG, t.message.toString())
                }
            })
    }

    // Метод для наблюдения за данными
    fun observeSchedule(): LiveData<List<Lesson>> {
        return mutableStudent
    }

    // Функция для объединения всех уроков из Schedule
    private fun mergeLessons(schedule: Schedule): List<Lesson> {
        // schedule является списком, поэтому можем просто пройтись по нему
        return schedule.flatMap { scheduleItem ->
            scheduleItem.TimeTable.Lessons  // Получаем уроки из TimeTable каждого ScheduleItem
        }
    }
}
