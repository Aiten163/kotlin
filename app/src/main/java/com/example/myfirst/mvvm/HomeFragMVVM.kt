package com.example.myfirst.mvvm

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myfirst.data.entity.MyProf
import com.example.myfirst.data.entity.Student
import com.example.myfirst.data.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.MathContext

class HomeFragMVVM(val token: String, val context: Context?) : ViewModel() {
    private val TAG = "HomeMVVM"
    private val mutableMyProf = MutableLiveData<MyProf>()
    private val mutableStudent = MutableLiveData<Student>()
    init {
        getMyPref()
        getStudent()
    }

    fun getStudent() {
        RetrofitInstance.mrsuApi.getStudent("Bearer $token").enqueue(object : Callback<Student> {
            override fun onResponse(call: Call<Student>, response: Response<Student>) {
                println("value2 ${response.body()}")
                if(response.body() != null){
                    mutableStudent.value = response.body()
                }else {
                    Toast.makeText(context?.applicationContext,
                        "Ошибка получения данных: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Student>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }
    private fun getMyPref() {
        RetrofitInstance.mrsuApi.getStudenInfo("Bearer $token").enqueue(object : Callback<MyProf> {
            override fun onResponse(call: Call<MyProf>, response: Response<MyProf>) {
                println("value1 ${response.body()}")
                if(response.body() != null){
                    mutableMyProf.value = response.body()
                }else {
                    Toast.makeText(context?.applicationContext,
                        "Ошибка получения данных: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MyProf>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }
    fun observeRandomMeal(): LiveData<MyProf> {
        return mutableMyProf
    }
    fun observeStudent(): LiveData<Student> {
        return mutableStudent
    }
}