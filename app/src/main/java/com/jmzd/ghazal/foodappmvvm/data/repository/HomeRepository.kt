package com.jmzd.ghazal.foodappmvvm.data.repository

import com.jmzd.ghazal.foodappmvvm.data.model.home.ResponseCategoriesList
import com.jmzd.ghazal.foodappmvvm.data.model.home.ResponseFoodsList
import com.jmzd.ghazal.foodappmvvm.data.server.ApiServices
import com.jmzd.ghazal.foodappmvvm.utils.MyResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject


/*فقط api رندوم را ا حالت عادی میزنیم بقیه api ها را با حالت myResponse میزنیم که اصولی تر است */
class HomeRepository @Inject constructor(private val api : ApiServices) {

    suspend fun randomFood(): Flow<Response<ResponseFoodsList>> {
        return flow {
            emit(api.foodRandom())
        }.flowOn(Dispatchers.IO)
    }

    suspend fun categoriesList(): Flow<MyResponse<ResponseCategoriesList>> {
        return flow {
            emit(MyResponse.loading())
            //Response
            when (api.categoriesList().code()) {
                in 200..202 -> {
                    emit(MyResponse.success(api.categoriesList().body()))
                }
                422 -> {
                    emit(MyResponse.error(""))
                }
                in 400..499 -> {
                    emit(MyResponse.error(""))
                }
                in 500..599 -> {
                    emit(MyResponse.error(""))
                }
            }
        }.catch { emit(MyResponse.error(it.message.toString())) }
            .flowOn(Dispatchers.IO)
    }

    suspend fun foodsList(letter: String): Flow<MyResponse<ResponseFoodsList>> {
        return flow {
            emit(MyResponse.loading())
            when (api.foodsList(letter).code()) {
                in 200..202 -> {
                    emit(MyResponse.success(api.foodsList(letter).body()))
                }
            }
        }.catch { emit(MyResponse.error(it.message.toString())) }
            .flowOn(Dispatchers.IO)
    }

    suspend fun foodsBySearch(letter: String): Flow<MyResponse<ResponseFoodsList>> {
        return flow {
            emit(MyResponse.loading())
            when (api.searchFood(letter).code()) {
                in 200..202 -> {
                    emit(MyResponse.success(api.searchFood(letter).body()))
                }
            }
        }.catch { emit(MyResponse.error(it.message.toString())) }
            .flowOn(Dispatchers.IO)
    }

    suspend fun foodsByCategory(letter: String): Flow<MyResponse<ResponseFoodsList>> {
        return flow {
            emit(MyResponse.loading())
            when (api.foodsByCategory(letter).code()) {
                in 200..202 -> {
                    emit(MyResponse.success(api.foodsByCategory(letter).body()))
                }
            }
        }.catch { emit(MyResponse.error(it.message.toString())) }
            .flowOn(Dispatchers.IO)
    }

}