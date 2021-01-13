package com.cesoft.feature_poi.ui

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.cesoft.feature_geocommon.Util
import com.cesoft.feature_poi.R
import com.cesoft.feature_poi.model.Poi
import org.greenrobot.eventbus.EventBus


class PoiRecyclerViewAdapter(
    private val values: List<Poi>
) : RecyclerView.Adapter<PoiRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_poi, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.name.text = item.name
        holder.date.text = Util.getDate(item.timestamp)
        holder.btnEdit.setOnClickListener {
            EventBus.getDefault().post(EditEvent(position))
        }
        holder.btnMap.setOnClickListener {
            EventBus.getDefault().post(MapEvent(position))
        }
    }

    override fun getItemCount(): Int = values.size

    class EditEvent(val position: Int)
    class MapEvent(val position: Int)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.txtName)
        val date = view.findViewById<TextView>(R.id.txtDate)
        val btnEdit = view.findViewById<ImageButton>(R.id.btnEdit)
        val btnMap = view.findViewById<ImageButton>(R.id.btnMap)
    }
}