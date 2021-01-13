package com.cesoft.feature_poi.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cesoft.feature_poi.Repo
import com.cesoft.feature_poi.model.Poi
import kotlinx.coroutines.launch

class PoiListViewModel : ViewModel() {
    private val _list = MutableLiveData<List<Poi>>()
    val list: LiveData<List<Poi>> = _list

    private val _msg = MutableLiveData<String>()
    val msg: LiveData<String> = _msg

    fun list() {
        viewModelScope.launch {
            val res = Repo.list()
            if(res.first != null) {
                _list.postValue(res.first)
            }
            if(res.second != null) {
                Log.e(TAG, "list:e: ${res.second}")
                _msg.postValue(res.second.toString())
            }
        }
    }

    companion object {
        private const val TAG = "PoiVM"
    }
}