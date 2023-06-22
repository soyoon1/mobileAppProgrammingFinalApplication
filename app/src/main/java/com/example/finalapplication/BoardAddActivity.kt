package com.example.finalapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.finalapplication.databinding.ActivityBoardAddBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class BoardAddActivity : AppCompatActivity() {
    lateinit var binding: ActivityBoardAddBinding
    lateinit var filePath: String

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.title="식단 메모하기"

        // 이미지 선택 후 반환 -> 전달된 Result가 OK이면 ImageView에 보이기, 업로드를 위해 파일이름 저장
        val requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode === android.app.Activity.RESULT_OK){
                Glide
                    .with(getApplicationContext())
                    .load(it.data?.data)
                    .apply(RequestOptions().override(250, 200))
                    .centerCrop()
                    .into(binding.addImageView)

                val cursor = contentResolver.query(it.data?.data as Uri,
                    arrayOf<String>(MediaStore.Images.Media.DATA), null, null, null);
                cursor?.moveToFirst().let {
                    filePath=cursor?.getString(0) as String
                    Log.d("mobileApp", filePath)
                }
            }
        }
        binding.btnGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK) // Gallaey에서 이미지 선택
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*"
            )
            requestLauncher.launch(intent)
        }

        binding.btnSave.setOnClickListener{

            if(binding.addImageView.drawable !== null && binding.addEditView.text.isNotEmpty()){
                //if(binding.addEditView.text.isNotEmpty()){
                //store 에 먼저 데이터를 저장 후 document id 값으로 업로드 파일 이름 지정
                saveStore()
            }else {
                Toast.makeText(this, "데이터가 모두 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }

    fun dateToString(date: Date): String {
        val format = SimpleDateFormat("yyyy-MM-dd hh:mm")
        // DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREAN);
        //  tz = TimeZone.getTimeZone("Asia/Seoul");  // TimeZone에 표준시 설정
        //  dateFormat.setTimeZone(tz);                    //DateFormat에 TimeZone 설정

        return format.format(date)
    }

    private fun saveStore(){
        //add............................
        val data = mapOf(
            "email" to MyApplication.email,
            "content" to binding.addEditView.text.toString(),
            "date" to dateToString(Date())
        )

        MyApplication.db.collection("news")
            .add(data)// id 발급을 위해 먼저 저장
            .addOnSuccessListener {
                Log.d("mobileApp", "data save ok")

                uploadImage(it.id)
            }
            .addOnFailureListener{
                Log.d("mobileApp", "data save error", it)
            }
    }

    private fun uploadImage(docId: String){
        //add............................
        val storage = MyApplication.storage
        val storageRef = storage.reference
        val imgRef = storageRef.child("images/${docId}.jpg")

        val file = Uri.fromFile(File(filePath))
        Log.d("mobileApp", file.toString())
        imgRef.putFile(file)
            .addOnSuccessListener {
                Toast.makeText(this, "save ok..", Toast.LENGTH_SHORT).show()
                Log.d("mobileApp", "save ok..")
                finish()
            }
            .addOnFailureListener{
                Log.d("mobileApp", "file save error", it)
            }
    }
}