package com.example.finalapplication

import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalapplication.databinding.ActivityBloodSugarBinding
import com.google.android.material.navigation.NavigationView
import java.io.BufferedReader
import java.io.File
import java.io.OutputStreamWriter

class BloodSugarActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {

    lateinit var binding: ActivityBloodSugarBinding
    var datas: MutableList<String>? = null
    lateinit var adapter: MyAdapter

    lateinit var sharedPreferences: SharedPreferences   // 액션바랑 버튼 변경

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBloodSugarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val bgColor:String? = sharedPreferences.getString("bg_color", "#FFFFFF")
        binding.mainRecyclerView.setBackgroundColor(Color.parseColor(bgColor)) // 배경색 변경

        val buttonColor:String? = sharedPreferences.getString("button_color", "")
        binding.mainFab.setBackgroundColor(Color.parseColor(buttonColor))

        val actionbarColor:String? = sharedPreferences.getString("actionbar_color", "")
        binding.toolbar.setBackgroundColor(Color.parseColor(actionbarColor))


        val requestlauncher : ActivityResultLauncher<Intent>
        = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            it.data!!.getStringExtra("result")?.let{
                datas?.add(it)
                adapter.notifyDataSetChanged()

               var db:SQLiteDatabase = DBHelper(this).writableDatabase
                db.execSQL("insert into bloodSugar_tbl (bloodSugar) values (?)", arrayOf<String>(it))
                db.close()
            }
        }
        binding.mainFab.setOnClickListener{
            var intent = Intent(this, AddActivity::class.java)
            intent.putExtra("data", "My BloodSugar List")
            requestlauncher.launch(intent)
        }

        datas = mutableListOf<String>()
        val db:SQLiteDatabase = DBHelper(this).readableDatabase
        var cursor: Cursor = db.rawQuery("select * from bloodSugar_tbl", null)
        while(cursor.moveToNext()){
            datas?.add(cursor.getString(1))
        }
        db.close()

        val layoutManager = LinearLayoutManager(this)
        binding.mainRecyclerView.layoutManager=layoutManager
        adapter = MyAdapter(datas)
        binding.mainRecyclerView.adapter=adapter
        binding.mainRecyclerView.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_1 -> { Log.d("mobileApp", "네비게이션뷰 메뉴 1") }
            R.id.menu_2 -> {
                var intent = Intent(this, BloodSugarActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_3 -> { Log.d("mobileApp", "네비게이션뷰 메뉴 3") }
            R.id.menu_4 -> {
                var intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:/119"))
                startActivity(intent)
            }
        }
        return true
    }

    override fun onResume() {
        super.onResume()

        val bgColor:String? = sharedPreferences.getString("bg_color", "#FFFFFF")
        binding.mainRecyclerView.setBackgroundColor(Color.parseColor(bgColor)) // 배경색 변경

        val buttonColor:String? = sharedPreferences.getString("button_color", "")
        binding.mainFab.setBackgroundColor(Color.parseColor(buttonColor))

        val actionbarColor:String? = sharedPreferences.getString("actionbar_color", "")
        binding.toolbar.setBackgroundColor(Color.parseColor(actionbarColor))

        adapter.notifyDataSetChanged()  // 설정이 바뀌는 것도 바뀔 때마다 이 것을 불러주면 바로바로 반영됨.
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu) // 얀걀.
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val file: File = File(filesDir, "bloodSugar_list.txt")
        when(item.itemId){
            R.id.menu_save_file -> {
                val writeStream: OutputStreamWriter = file.writer() // 쓸 수 있게 만들어 줌.  안드로이드에서 파일에 쓰는 방법
                writeStream.write("hello android!!!\n")
                for(i in datas!!.indices)
                    writeStream.write(datas!![i] + "\n")   // null이 아니다 :!!  버퍼에 저장해놨다가 한꺼번에 파일에 내보냄.
                writeStream.flush()
                return true
            }

            R.id.menu_read_file -> {
                val readStream: BufferedReader = file.reader().buffered() // 읽은 것을 버퍼에 저장하기 위해 buffered 추가 InputStreamReader에서 BufferedReader로 바꿔 줌
                readStream.forEachLine {
                    Log.d("mobileApp", "$it")
                }
                return true
            }

            R.id.menu_main_setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent) // 리턴 값이 없고 단순히 부르기만 하기 때문에 이것을 사용
                return true
            }

        }
        return false
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putStringArrayList("datas", ArrayList(datas))
    }

}