package com.ahmedrafat.ordersapp.ui.addorderfragment

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedrafat.ordersapp.model.ApiService
import com.ahmedrafat.ordersapp.model.apimodels.ReviewModel
import com.ahmedrafat.ordersapp.model.apimodels.ShopperModel
import com.ahmedrafat.ordersapp.model.datamodels.ItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit

@HiltViewModel
class AddOrderViewModel @ViewModelInject constructor(private val client: Retrofit) : ViewModel() {
    //Variables LifeData
    val shoppersModelMutableLiveData: MutableLiveData<List<ShopperModel>> = MutableLiveData()
    val reviewMutableLiveData: MutableLiveData<ReviewModel.ReviewModelItem> = MutableLiveData()
    val simulatedSubmitOrder: MutableLiveData<String> = MutableLiveData()
    val errorMutableLiveData: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()

    private val apiClient: ApiService = client.create(ApiService::class.java)

    //Deferred to Receive Response
    private lateinit var deferred: Deferred<Response<List<ShopperModel>>>
    private lateinit var reviewDeferred: Deferred<Response<ReviewModel.ReviewModelItem>>

    //request function
    fun getShoppers() {
        loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                deferred = apiClient.getShoppers()
                val response = deferred.await()
                //handle response on the main thread
                viewModelScope.launch(Dispatchers.Main) {
                    loading.value = false
                    shoppersModelMutableLiveData.value = response.body()
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

    fun submitOrder(params:HashMap<String,String>,listOrderItems:List<ItemModel>){
        loading.value = true
        viewModelScope.launch(Dispatchers.IO){
            delay(1000)
            viewModelScope.launch(Dispatchers.Main){
                loading.value = false
                simulatedSubmitOrder.value = "Done"
            }
        }
    }

    fun submitRate(params: java.util.HashMap<String, String>) {
        loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                reviewDeferred = apiClient.submitReview(params)
                val response = reviewDeferred.await()
                //handle response on the main thread
                viewModelScope.launch(Dispatchers.Main) {
                    loading.value = false
                    reviewMutableLiveData.value = response.body()
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