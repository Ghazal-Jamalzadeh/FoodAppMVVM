package com.jmzd.ghazal.foodappmvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmzd.ghazal.foodappmvvm.data.model.home.ResponseCategoriesList
import com.jmzd.ghazal.foodappmvvm.data.model.home.ResponseFoodsList
import com.jmzd.ghazal.foodappmvvm.data.model.home.ResponseFoodsList.*
import com.jmzd.ghazal.foodappmvvm.data.repository.HomeRepository
import com.jmzd.ghazal.foodappmvvm.utils.MyResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    val randomFoodData = MutableLiveData<List<Meal>>()
    fun loadFoodRandom() = viewModelScope.launch(Dispatchers.IO) {
        repository.randomFood().collect {
            //it : Response<ResponseFoodsList>
            randomFoodData.postValue(it.body()?.meals!!)
        }
    }

    val filtersListData = MutableLiveData<MutableList<Char>>()
    fun loadFilterList() = viewModelScope.launch(Dispatchers.IO) {
        val letters = listOf('A'..'Z').flatten().toMutableList()
        filtersListData.postValue(letters)
    }

    val categoriesListData = MutableLiveData<MyResponse<ResponseCategoriesList>>()
    fun loadCategoriesList() = viewModelScope.launch(Dispatchers.IO) {
        repository.categoriesList().collect {
            //it : MyResponse<ResponseCategoriesList>
            categoriesListData.postValue(it) }
    }

}