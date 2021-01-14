package com.cesoft.feature_poi.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cesoft.feature_poi.R


class PoiListFragment : Fragment() {
    private lateinit var vm: PoiListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this).get(PoiListViewModel::class.java)
        vm.list.observe(this, { list ->
            val rv = requireView().findViewById<RecyclerView>(R.id.list)
            rv.adapter = PoiRecyclerViewAdapter(list)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_poi_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rv = requireView().findViewById<RecyclerView>(R.id.list)
        rv.layoutManager = LinearLayoutManager(requireContext())
        //rv.adapter = PoiRecyclerViewAdapter(listOf())
        vm.list()
    }


//    override fun onStart() {
//        super.onStart()
//        //EventBus.getDefault().register(this)
//    }
//    override fun onStop() {
//        super.onStop()
//        //EventBus.getDefault().unregister(this)
//    }
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onMessageEvent(event: PoiRecyclerViewAdapter.MapEvent) {
//        Log.e(TAG, "MapEvent=${event.poi}")
//        findNavController().navigate(R.id.nav_poi_item)
//    }

    companion object {
        private const val TAG = "PoiFragment"
        fun newInstance(): PoiListFragment {
            return PoiListFragment()
        }
    }
}