package com.example.finalapplication

import android.content.Context
import android.graphics.Color

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView

import com.example.finalapplication.databinding.ItemRecyclerviewBinding

class MyViewHolder(val binding: ItemRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)  // 레이아웃 파일 가져옴.

class MyAdapter(val datas: MutableList<String>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    lateinit var context: Context

    override fun getItemCount(): Int{
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {    // 3개의 파일은 반드시 있어야 함. 기존 거 연결 뷰홀더를 만들어서 리턴해줌. parent: MainActivity
        context = parent.context
        return MyViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {    // 아이템 리사이클러에 값을 채워줌.
        val binding=(holder as MyViewHolder).binding
        binding.itemData.text= datas!![position]

        var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val txColor:String? = sharedPreferences.getString("tx_color", "#000000")
        binding.itemData.setTextColor(Color.parseColor(txColor)) // 텍스트 색 변경
    }
}