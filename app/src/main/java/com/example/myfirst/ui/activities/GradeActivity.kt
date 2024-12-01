package com.example.myfirst.ui.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfirst.adapters.SectionAdapter
import com.example.myfirst.data.entity.Grade
import com.example.myfirst.data.entity.Section
import com.example.myfirst.databinding.ActivityGradeBinding
import com.example.myfirst.mvvm.GradeMVVM
import com.example.myfirst.utils.GradeCustomFactory

class GradeActivity: AppCompatActivity() {
    private lateinit var binding: ActivityGradeBinding
    private lateinit var gradeMVVM: GradeMVVM
    private lateinit var sectionAdapter: SectionAdapter
    private lateinit var sPref: SharedPreferences
    private var disciplineId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGradeBinding.inflate(layoutInflater)
        disciplineId = intent.getIntExtra("id",1400122)
        sectionAdapter = SectionAdapter()
        binding.nameDiscipline.text = intent.getStringExtra("title")
        setContentView(binding.root)
        sPref = this.getSharedPreferences("MyPref", AppCompatActivity.MODE_PRIVATE)
        gradeMVVM = ViewModelProvider(this,
            GradeCustomFactory(sPref.getString("saved_token", "").toString())
        )[GradeMVVM::class.java]

        InitView()
    }

    private fun InitView() {
        prepareRecyclerView()
        observeGrade()
        gradeMVVM.getGrade(disciplineId.toString())
        binding.buttonBack.setOnClickListener {
            finish()
        }
    }
    private fun observeGrade() {
        gradeMVVM.observeGrade().observe(this, object : Observer<Grade?> {
            override fun onChanged(t: Grade?) {
                if (!t!!.Sections.isEmpty()) {
                    sectionAdapter.setSectionList(t.Sections)
                    recalculateSumBall(t.Sections)
                } else {
                    Toast.makeText(applicationContext, "Не удалось получить оценки", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun prepareRecyclerView() {
        binding.recyclerViewRating.apply {
            adapter = sectionAdapter
            layoutManager = LinearLayoutManager(context) //GridLayoutManager(context,3, GridLayoutManager.VERTICAL,false)
        }
    }

    private fun recalculateSumBall(sectionList: List<Section>) {
        var sumHaveBall = 0.0F
        var sumMaxBall = 0.0F
        for (i in sectionList) {
            for (j in i.ControlDots) {
                if (j.Mark != null) {
                    sumHaveBall += j.Mark.Ball.toFloat()
                }
                sumMaxBall += j.MaxBall.toFloat()
            }
        }
        binding.sumBall.text = "Количество баллов: $sumHaveBall / $sumMaxBall"
    }

}