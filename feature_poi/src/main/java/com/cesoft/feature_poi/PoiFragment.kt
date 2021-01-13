package com.cesoft.feature_poi

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

class PoiFragment : Fragment() {
    private lateinit var vm: PoiViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this).get(PoiViewModel::class.java)
        vm.list.observe(this, { list ->
            (requireView() as RecyclerView).adapter = PoiRecyclerViewAdapter(list)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_poi_list, container, false)
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = PoiRecyclerViewAdapter(listOf())
            }
        }
        vm.list()
        return view
    }

    companion object {
        fun newInstance(): PoiFragment {
            return PoiFragment()
        }
    }
}