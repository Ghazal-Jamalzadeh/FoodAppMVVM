package com.jmzd.ghazal.foodappmvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmzd.ghazal.foodappmvvm.data.database.FoodEntity
import com.jmzd.ghazal.foodappmvvm.data.repository.FavoritesRepository
import com.jmzd.ghazal.foodappmvvm.utils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val repository: FavoritesRepository) : ViewModel() {

    val favoritesListData = MutableLiveData<DataStatus<List<FoodEntity>>>()
    fun loadFavorites() = viewModelScope.launch(Dispatchers.IO) {
        repository.foodsList().collect {
            //it: MutableList<FoodEntity>
            favoritesListData.postValue(DataStatus.success(it, it.isEmpty()))
        }
    }
}