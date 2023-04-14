package com.jmzd.ghazal.foodappmvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmzd.ghazal.foodappmvvm.data.model.home.ResponseFoodsList
import com.jmzd.ghazal.foodappmvvm.data.repository.DetailRepository
import com.jmzd.ghazal.foodappmvvm.utils.MyResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: DetailRepository) : ViewModel(){

    val foodDetailData = MutableLiveData<MyResponse<ResponseFoodsList>>()
    fun loadFoodDetail(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.foodDetail(id).collect { foodDetailData.postValue(it) }
    }

}