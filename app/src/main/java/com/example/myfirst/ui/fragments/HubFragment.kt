package com.example.myfirst.ui.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfirst.R
import com.example.myfirst.adapters.DisciplineAdapter
import com.example.myfirst.data.entity.Discipline1
import com.example.myfirst.data.entity.Discipline2
import com.example.myfirst.databinding.ActivityDisciplineBinding
import com.example.myfirst.databinding.FragmentHomeBinding
import com.example.myfirst.mvvm.DisciplineMVVM
import com.example.myfirst.ui.activities.GradeActivity
import com.example.myfirst.utils.DisciplineCustomFactory
import java.time.LocalDate

class HubFragment: Fragment() {
    private lateinit var binding: ActivityDisciplineBinding
    private lateinit var disciplineMvvm: DisciplineMVVM
    private lateinit var disciplineAdapter: DisciplineAdapter
    private lateinit var sPref: SharedPreferences
    private lateinit var selectedPeriod: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisciplineBinding.inflate(layoutInflater)
        disciplineAdapter = DisciplineAdapter()
        sPref = requireContext().getSharedPreferences("MyPref", AppCompatActivity.MODE_PRIVATE)
        disciplineMvvm = ViewModelProvider(this,
            DisciplineCustomFactory(sPref.getString("saved_token", "").toString())
        )[DisciplineMVVM::class.java]
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        observeDiscipline()
        handlerSpinner()
        disciplineAdapter.setOnClickListener(object : DisciplineAdapter.OnItemClick {
            override fun onItemClick(discipline: Discipline2) {
                val intentNew:Intent?
                intentNew = Intent(requireContext(), GradeActivity::class.java)
                intentNew.putExtra("title", discipline.Title)
                intentNew.putExtra("id", discipline.Id)
                startActivity(intentNew)

            }
        })
        binding.firstSemester.setOnClickListener {
            disciplineMvvm.getDiscipline(selectedPeriod,"1")
        }
        binding.secondSemester.setOnClickListener {
            disciplineMvvm.getDiscipline(selectedPeriod,"2")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handlerSpinner() {
        val periodsArray = resources.getStringArray(R.array.periods_array)
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, periodsArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spinner = binding.kurs
        spinner.adapter = adapter
        val defaultYear = periodsArray.indexOf("${LocalDate.now().year} - ${LocalDate.now().year+1}")
        spinner.setSelection(defaultYear)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                selectedPeriod = periodsArray[position]
                Toast.makeText(context, "Выбран период: $selectedPeriod", Toast.LENGTH_SHORT).show()
                disciplineMvvm.getDiscipline(selectedPeriod,"1")
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }
    }

    private fun observeDiscipline() {
        disciplineMvvm.observeDiscipline().observe(viewLifecycleOwner, object : Observer<Discipline1?> {
            override fun onChanged(t: Discipline1?) {
                if (!t!!.RecordBooks.isEmpty()) {
                    notVisibleEmptyDiscipline()
                    disciplineAdapter.setDisciplineList(t.RecordBooks[0].Disciplines)
                } else {
                    visibleEmptyDiscipline()
                    Toast.makeText(context, "Не удалось получить дисциплины", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun visibleEmptyDiscipline() {
        binding.apply {
            disciplines.visibility = View.INVISIBLE
            emptyDiscipline.visibility = View.VISIBLE
        }
    }
    private fun notVisibleEmptyDiscipline() {
        binding.apply {
            disciplines.visibility = View.VISIBLE
            emptyDiscipline.visibility = View.INVISIBLE
        }
    }
    private fun prepareRecyclerView() {
        binding.recyclerViewDiscipline.apply {
            adapter = disciplineAdapter
            layoutManager = LinearLayoutManager(context) //GridLayoutManager(context,3, GridLayoutManager.VERTICAL,false)
        }
    }
}