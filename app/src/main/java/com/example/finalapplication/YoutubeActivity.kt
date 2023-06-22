package com.example.finalapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalapplication.databinding.ActivityYoutubeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class YoutubeActivity : AppCompatActivity() {
    lateinit var binding: ActivityYoutubeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_youtube)
        binding = ActivityYoutubeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.title="요리법 영상 검색하기"

        binding.searchButton.setOnClickListener{
            var call: Call<SearchListResponse> = MyApplication.networkService2.getList(
                "AIzaSyCupp4Dn0kkhpbM_rQv3f4NIFRQsjz07fI",
                binding.input1.text.toString(),
                "video",
                "snippet"
            )
            call?.enqueue(object: Callback<SearchListResponse> {
                override fun onResponse(
                    call: Call<SearchListResponse>,
                    response: Response<SearchListResponse>
                ) {
                    if(response.isSuccessful){
                        binding.recyclerView.layoutManager = LinearLayoutManager(this@YoutubeActivity)
                        binding.recyclerView.adapter = MyYoutubeAdapter(this@YoutubeActivity, response.body()?.items)
                    }
                }

                override fun onFailure(call: Call<SearchListResponse>, t: Throwable) {
                    Log.d("mobileApp", "error.......")
                }
            }
            )
        }

    }
}