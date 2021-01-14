package com.cesoft.feature_poi.ui

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.cesoft.feature_geocommon.Util
import com.cesoft.feature_poi.R
import com.cesoft.feature_poi.model.Poi


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
        val bundle = bundleOf(Poi::class.java.simpleName to item)
        holder.btnEdit.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_nav_main_to_nav_poi_item, bundle))
        holder.btnMap.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_nav_main_to_nav_poi_maps, bundle))
            //Navigation.createNavigateOnClickListener(R.id.action_nav_poi_list_to_nav_poi_item, bundle))
//        holder.btnMap.setOnClickListener {
//            EventBus.getDefault().post(MapEvent(item))
//        }
    }

    override fun getItemCount(): Int = values.size

    class EditEvent(val poi: Poi)
    class MapEvent(val poi: Poi)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.txtName)
        val date: TextView = view.findViewById(R.id.txtDate)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)
        val btnMap: ImageButton = view.findViewById(R.id.btnMap)
    }
}