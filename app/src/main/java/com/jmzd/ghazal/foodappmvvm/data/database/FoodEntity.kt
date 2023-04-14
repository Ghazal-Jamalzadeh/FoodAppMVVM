package com.jmzd.ghazal.foodappmvvm.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jmzd.ghazal.foodappmvvm.utils.FOOD_DB_TABLE

@Entity(tableName = FOOD_DB_TABLE)
data class FoodEntity(
    @PrimaryKey
    var id: Int = 0,
    var title: String = "",
    var img: String = ""
)