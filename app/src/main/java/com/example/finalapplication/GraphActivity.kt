package com.example.finalapplication

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalapplication.databinding.ActivityGraphBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class GraphActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_graph)
        val binding = ActivityGraphBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.title="혈당 스파이크란?"

       var values:ArrayList<Entry> = ArrayList()
        // values.add(Entry(x, y))
//        for(i in 0 until 10){
//            var v = Math.random() * 10
//            values.add(Entry(i.toFloat(), v.toFloat()))
//        }

        values.add(Entry(0F, 129F))
        values.add(Entry(1F, 127F))
        values.add(Entry(2F, 133F))
        values.add(Entry(3F, 202F))
        values.add(Entry(4F, 221F))
        values.add(Entry(5F, 197F))
        values.add(Entry(6F, 163F))
        values.add(Entry(7F, 140F))
        values.add(Entry(8F, 132F))
        values.add(Entry(9F, 135F))
        values.add(Entry(10F, 130F))

        var dataset = LineDataSet(values, "혈당")
        dataset.color = Color.LTGRAY // 선 색깔
        dataset.setCircleColor(Color.BLUE) // 동그라미 두께
        dataset.lineWidth = 5f // 선의 두께
        var data = LineData(dataset)
        binding.lineChart.data = data



    }
}