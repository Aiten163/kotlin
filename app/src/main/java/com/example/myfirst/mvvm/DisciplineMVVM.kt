package com.example.myfirst.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myfirst.data.entity.Discipline1
import com.example.myfirst.data.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DisciplineMVVM(val token: String) : ViewModel() {
    private val TAG: String = "DisciplineMVVM"
    private val mutableDiscipline: MutableLiveData<Discipline1> = MutableLiveData<Discipline1>()

    fun getDiscipline(year: String, period: String) {
        RetrofitInstance.mrsuApi.getDiscipline("Bearer $token", year, period)
            .enqueue(object : Callback<Discipline1> {
                override fun onResponse(call: Call<Discipline1>, response: Response<Discipline1>) {
                    println(response.body())
                    mutableDiscipline.value = response.body()
                }

                override fun onFailure(call: Call<Discipline1>, t: Throwable) {
                    Log.e(TAG, t.message.toString())
                }

            })
    }

    fun observeDiscipline(): LiveData<Discipline1> {
        return mutableDiscipline
    }
}