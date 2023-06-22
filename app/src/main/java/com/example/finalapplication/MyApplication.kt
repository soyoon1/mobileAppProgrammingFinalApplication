package com.example.finalapplication

import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication: MultiDexApplication() {

    companion object{
        lateinit var db : FirebaseFirestore
        lateinit var storage : FirebaseStorage

        lateinit var auth : FirebaseAuth
        var email:String? = null
        fun checkAuth() : Boolean{
            var currentUser = auth.currentUser
            return currentUser?.let{
                email = currentUser.email
                if(currentUser.isEmailVerified) true
                else false
            } ?: false

        }

        var networkService : NetworkService
        var networkService2 : NetworkService
//        var gson: Gson = GsonBuilder()
//            .setLenient()
//            .create()

        val retrofit: Retrofit
            get() = Retrofit.Builder()
                .baseUrl("http://apis.data.go.kr/1390802/AgriFood/FdFoodCkryImage/")
                .addConverterFactory(GsonConverterFactory.create())  // gson
                .build()
        init{
            networkService = retrofit.create(NetworkService::class.java)// 초기화
        }

        val retrofit2: Retrofit
            get() = Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        init{
            networkService2 = retrofit2.create(NetworkService::class.java)
        }

    }
    override fun onCreate() {
        super.onCreate()

        auth = Firebase.auth

        db = FirebaseFirestore.getInstance() // 하나의 컬렉션을 만들겠다.
        storage = Firebase.storage
    }
}