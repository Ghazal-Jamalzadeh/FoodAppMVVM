package com.jmzd.ghazal.foodappmvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmzd.ghazal.foodappmvvm.data.database.FoodEntity
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

    fun saveFood(entity: FoodEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveFood(entity)
    }

    fun deleteFood(entity: FoodEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteFood(entity)
    }

    val isFavoriteData = MutableLiveData<Boolean>()
    fun existsFood(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.existsFood(id).collect { isFavoriteData.postValue(it) }
    }
}