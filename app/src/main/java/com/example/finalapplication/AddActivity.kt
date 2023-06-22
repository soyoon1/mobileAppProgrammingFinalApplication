package com.example.finalapplication

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import com.example.finalapplication.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val mystr = intent.getStringExtra("data")
        binding.myData.text = mystr
    }
    // 적절한 비율로 이미지 크기 줄이기
    private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int { // 사진 이미지 보기
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true  // 옵션만 설정하고자 함.
        try {
            var inputStream = contentResolver.openInputStream(fileUri)

            //inJustDecodeBounds 값을 true 로 설정한 상태에서 decodeXXX() 를 호출.
            //로딩 하고자 하는 이미지의 각종 정보가 options 에 설정 된다.
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream!!.close()
            inputStream = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //비율 계산........................
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1
        //inSampleSize 비율 계산
        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //add............................
    override fun onOptionsItemSelected(item: MenuItem): Boolean { // 적은 거 들고 오기
        if (item.itemId == R.id.menu_add_save){ // menuadd.xml 저장 파일 가져올 때 아이디 등록되어있어서 이 메뉴를 의미하는 것.
            intent.putExtra("result", binding.addEditView.text.toString())
            setResult(Activity.RESULT_OK, intent)  // 돌아갈 것이라고 값을 설정해 줌.
            finish()
            return true
        }
        return false
    }

}