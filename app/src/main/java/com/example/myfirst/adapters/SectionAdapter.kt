package com.example.myfirst.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirst.R
import com.example.myfirst.data.entity.Section

class SectionAdapter() :
    RecyclerView.Adapter<SectionAdapter.ViewHolder>() {

    private var sectionList: List<Section> = ArrayList()

    fun setSectionList(sectionList: List<Section>){
        this.sectionList = sectionList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_rating_section, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sectionItem = sectionList[position]
        holder.sectionNameTextView.text = sectionItem.Title

        val controlPointAdapter = ControlPointAdapter()
        controlPointAdapter.setControlPointList(sectionItem.ControlDots)
        holder.controlPointRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.controlPointRecyclerView.adapter = controlPointAdapter
    }

    override fun getItemCount(): Int {
        return sectionList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sectionNameTextView: TextView = itemView.findViewById(R.id.sectionNameTextView)
        var controlPointRecyclerView: RecyclerView = itemView.findViewById(R.id.recyclerViewСontrolPoint)
    }
}