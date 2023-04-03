package com.jmzd.ghazal.foodappmvvm.data.repository

import com.jmzd.ghazal.foodappmvvm.data.model.home.ResponseFoodsList
import com.jmzd.ghazal.foodappmvvm.data.server.ApiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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

}