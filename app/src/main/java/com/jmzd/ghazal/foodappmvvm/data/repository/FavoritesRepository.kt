package com.jmzd.ghazal.foodappmvvm.data.repository

import com.jmzd.ghazal.foodappmvvm.data.database.FoodDao
import com.jmzd.ghazal.foodappmvvm.data.database.FoodEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoritesRepository @Inject constructor(private val dao: FoodDao) {
    fun foodsList() : Flow<MutableList<FoodEntity>> = dao.getAllFoods()
}