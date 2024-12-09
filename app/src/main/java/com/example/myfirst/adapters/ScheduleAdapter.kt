package com.example.myfirst.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirst.R
import com.example.myfirst.data.entity.Lesson
import com.example.myfirst.data.entity.Schedule

class ScheduleAdapter : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {
    private var scheduleList: List<Lesson> = ArrayList()
    private lateinit var onItemClick: OnItemScheduleClicked
    private lateinit var onLongCategoryClick: OnLongCategoryClick

    fun setScheduleList(scheduleList: List<Lesson>){
        this.scheduleList = scheduleList
        notifyDataSetChanged()
    }

    // Метод для очистки списка
    fun clearScheduleList() {
        scheduleList = emptyList()  // Очищаем список
        notifyDataSetChanged()  // Обновляем RecyclerView
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textOrder: TextView = itemView.findViewById(R.id.textOrder)
        val textLesson: TextView = itemView.findViewById(R.id.textLesson)
        val textTeacher: TextView = itemView.findViewById(R.id.textTeacher)
        val textAuditorium: TextView = itemView.findViewById(R.id.textAuditorium)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_schedule, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (scheduleList.isNotEmpty()) {
            val item = scheduleList[position]
            holder.textOrder.text = item.Number.toString()
            holder.textLesson.text = item.Disciplines[0].Title
            holder.textAuditorium.text = item.Disciplines[0].Auditorium.Number
            holder.textTeacher.text = item.Disciplines[0].Teacher.FIO
        } else {
            // В случае пустого списка можно очистить значения или показать "нет данных"
            holder.textOrder.text = "-"
            holder.textLesson.text = "-"
            holder.textAuditorium.text = "-"
            holder.textTeacher.text = "-"
        }
    }

    override fun getItemCount(): Int {
        return if (scheduleList.isNotEmpty()) {
            scheduleList.size
        } else {
            // Показываем один элемент для пустого расписания, если хотите сделать сообщение
            1
        }
    }

    interface OnItemScheduleClicked{
        fun onClickListener(item: List<Lesson>)
    }

    fun onItemClicked(onItemClick: OnItemScheduleClicked){
        this.onItemClick = onItemClick
    }

    interface OnLongCategoryClick{
        fun onCategoryLongCLick(schedule: Schedule)
    }
}
