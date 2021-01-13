package com.cesoft.feature_poi

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cesoft.feature_poi.dummy.DummyContent

class PoiFragment : Fragment() {

    private lateinit var vm: PoiViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            //columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        android.util.Log.e("PoiFrg", "onCreateView----------------------------")
        //vm = ViewModelProvider(this).get(PoiViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_poi_list, container, false)
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(requireContext())
//                    when {
//                    columnCount <= 1 -> LinearLayoutManager(context)
//                    else -> GridLayoutManager(context, columnCount) }
                adapter = PoiRecyclerViewAdapter(DummyContent.ITEMS)
            }
        }
        return view
    }

    companion object {
        fun newInstance(): PoiFragment {
            android.util.Log.e("PoiFrg", "newInstance----------------------------")
            return PoiFragment()
        }

//        const val ARG_COLUMN_COUNT = "column-count"
//        @JvmStatic
//        fun newInstance(columnCount: Int) =
//            PoiFragment2().apply {
//                arguments = Bundle().apply {
//                    putInt(ARG_COLUMN_COUNT, columnCount)
//                }
//            }
    }
}