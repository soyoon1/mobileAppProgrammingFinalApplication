package com.example.finalapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.finalapplication.databinding.ActivityZipCodeBinding

class ZipCodeActivity : AppCompatActivity() {
    lateinit var binding : ActivityZipCodeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_zip_code)
        binding = ActivityZipCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.title="우편 주소 검색하기"

        val requestLauncher: ActivityResultLauncher<Intent>
            = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                it.data!!.getStringExtra("result")?.let{
                    binding.etAddress.setText(it!!)
                }
        }
        binding.button.setOnClickListener{
            val intent = Intent(this@ZipCodeActivity, WebViewActivity::class.java)
            requestLauncher.launch(intent)
        }

    }
}