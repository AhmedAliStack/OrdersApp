package com.ahmedrafat.ordersapp.ui.mainfragment

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedrafat.ordersapp.model.ApiService
import com.ahmedrafat.ordersapp.model.apimodels.OrdersModel
import com.ahmedrafat.ordersapp.model.apimodels.ShopperModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit

@HiltViewModel
class MainViewModel @ViewModelInject constructor(private val client: Retrofit): ViewModel() {
    //Variables LifeData
    val ordersLiveData: MutableLiveData<List<OrdersModel>> = MutableLiveData()
    val errorMutableLiveData: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()

    private val apiClient: ApiService = client.create(ApiService::class.java)

    //Deferred to Receive Response
    private lateinit var ordersDeferred: Deferred<Response<List<OrdersModel>>>

    init {
        loading.value = true
    }

    fun getMainOrders(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                ordersDeferred = apiClient.getOrders()
                val response = ordersDeferred.await()
                //handle response on the main thread
                viewModelScope.launch(Dispatchers.Main) {
                    loading.value = false
                    ordersLiveData.value = response.body()
                }
            } catch (e: Exception) {
                //handle exception on the main thread
                viewModelScope.launch(Dispatchers.Main) {
                    loading.value = false
                    errorMutableLiveData.value = e.localizedMessage
                }
            }
        }
    }
}