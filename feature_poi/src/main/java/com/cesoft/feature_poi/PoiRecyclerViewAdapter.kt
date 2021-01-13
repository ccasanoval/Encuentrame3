package com.cesoft.feature_poi

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cesoft.feature_geocommon.Util
import com.cesoft.feature_poi.model.Poi


class PoiRecyclerViewAdapter(
    private val values: List<Poi>
) : RecyclerView.Adapter<PoiRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_poi, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.name.text = item.name
        holder.date.text = Util.getDate(item.timestamp, "dd/MM/yyyy hh:mm")
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.txtName)
        val date: TextView = view.findViewById(R.id.txtDate)
    }
}