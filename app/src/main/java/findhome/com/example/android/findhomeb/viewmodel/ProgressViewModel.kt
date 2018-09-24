package findhome.com.example.android.findhomeb.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import findhome.com.example.android.findhomeb.model.CloudData

class ProgressViewModel: ViewModel(){

    private var arraylistmutablelivedata= MutableLiveData<ArrayList<CloudData>>()



    fun getArrayCloudList( cloudData: ArrayList<CloudData>): MutableLiveData<ArrayList<CloudData>> {

        arraylistmutablelivedata.value=cloudData

        return arraylistmutablelivedata
    }

}