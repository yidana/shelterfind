package findhome.com.example.android.findhomeb.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class NotifiactionRecycleVIewmodel:ViewModel(){

    private var arraylistmutablelivedata= MutableLiveData<ArrayList<HashMap<String,String>>>()



    fun getDataArrayList( cloudData: ArrayList<HashMap<String,String>>): MutableLiveData<ArrayList<HashMap<String,String>>> {

        arraylistmutablelivedata.value=cloudData

        return arraylistmutablelivedata
    }

}