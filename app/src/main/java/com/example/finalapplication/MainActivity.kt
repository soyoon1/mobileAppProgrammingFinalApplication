package com.example.finalapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import com.example.finalapplication.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    lateinit var toolbar : ActionBarDrawerToggle
    lateinit var boardFragment: BoardFragment
    lateinit var retrofitFragment: RetrofitFragment

    var mode = "retrofit"
    var authMenuItem : MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)  // 바인딩에 있는 toolbar가 액션바 역할을 할거야라고 선언해 줌.

        boardFragment = BoardFragment()
        retrofitFragment= RetrofitFragment()


        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, retrofitFragment)  // activity_main.xml에 있는 framelayout을 의미. 여기에 volleyFragment를 넣어줘라. content를 바꾸겠다. 실행할 수 있는 명령어만 준비된 상태
            .commit()  // 실행해 줌.

        supportFragmentManager.beginTransaction()
            .add(R.id.frameLayout, boardFragment)  // activity_main.xml에 있는 framelayout을 의미. 여기에 volleyFragment를 넣어줘라. content를 바꾸겠다. 실행할 수 있는 명령어만 준비된 상태
            .hide(boardFragment) // 화면에 하나만 보이지만 실제로는 두개를 만들어 놓은 상태
            .commit()  // 실행해 줌.

        supportActionBar?.title="식단For당뇨"

        toolbar = ActionBarDrawerToggle(this, binding.drawer, R.string.drawer_opened, R.string.drawer_closed)  // 토글 버튼. 왼쪽 상단 버튼
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // <- 버튼 추가시에 적어줘야함. 업버튼. 토글 버튼
        toolbar.syncState() // 토글 버튼 만들고 액션바에 붙여서 상태까지 나타냄. 토글 버튼 <- 모양에서 = 모양으로 바꿔줌.

        binding.mainDrawer.setNavigationItemSelectedListener(this)



    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        when(item.itemId){
//            R.id.menu_1 -> {
//                supportFragmentManager.beginTransaction()
//                    .show(boardFragment)
//                    .commit()
//            }
//            R.id.menu_2 -> {
//                var intent = Intent(this, BloodSugarActivity::class.java)
//                startActivity(intent)
//            }
//            R.id.menu_3 -> {  }
//            R.id.menu_4 -> {
//                var intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:/119"))
//                startActivity(intent)
//            }
//        }
//        return true
        if(item.itemId === R.id.menu_3 && mode !== "retrofit"){  // retrofit 아닌데 retrofit 클릭 시
            supportFragmentManager.beginTransaction()
                .show(retrofitFragment)
                .commit()
            supportFragmentManager.beginTransaction()
                .hide(boardFragment)
                .commit()
            mode="retrofit"  // 현재는 retrofit 상태 라고 설정
            supportActionBar?.title="식단For당뇨"
        }
        else if(item.itemId === R.id.menu_1 && mode !== "board"){  // retrofit 아닌데 retrofit 클릭 시
            supportFragmentManager.beginTransaction()
                .show(boardFragment)
                .commit()
            supportFragmentManager.beginTransaction()
                .hide(retrofitFragment)
                .commit()
            mode="board"  // 현재는 board 상태 라고 설정
            supportActionBar?.title="식단 메모하기"
        }
        else if(item.itemId == R.id.menu_2){
            var intent = Intent(this, BloodSugarActivity::class.java)
            startActivity(intent)
        }
        else if(item.itemId == R.id.menu_4){
            var intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:/119"))
            startActivity(intent)
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {  // 이 함수로 메뉴를 구성하면 액션바에 오버플로 버튼이 나오고 이 버튼을 누르면 오버플로 메뉴가 나타남
        menuInflater.inflate(R.menu.menu_actionbar, menu) // xml과 연결

        authMenuItem = menu!!.findItem(R.id.menu_auth)
        if(MyApplication.checkAuth()){
            authMenuItem!!.title = "${MyApplication.email}님"
        }else{
            authMenuItem!!.title = "인증"
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onStart() {
        // Intent에서 finish() 돌아올 때 실행
        // onCreate -> onStart -> onCreateOptionsMenu
        super.onStart()
        if(authMenuItem != null){
            if(MyApplication.checkAuth()){
                authMenuItem!!.title = "${MyApplication.email}님"
            }else{
                authMenuItem!!.title = "인증"
            }
        }

    }

    // 토글 버튼
    override fun onOptionsItemSelected(item: MenuItem): Boolean { // 오버플로 메뉴 선택 시 이벤트 처리 토글 버튼
        if(toolbar.onOptionsItemSelected(item)){  // toolbar 인식 X 내부 변수라. 다른 함수에서 접근해야되어서. 전역변수 형태라 빼주면 됨. 토글 버튼
            return true   // toolbar없으면 생략 가능 이벤트가 토글 버튼에서 발생하면
        }
//        when(item.itemId){
//            R.id.menu1 -> {
//                Log.d("mobileApp", "Menu1")
//            }
//            R.id.menu_auth -> {
//
//            }
//        }

        if(item.itemId == R.id.menu_auth){
            val intent = Intent(this, AuthActivity::class.java)
            if(authMenuItem!!.title!!.equals("인증")){ // null이 아니기 때문에 !! 표시하기.
                intent.putExtra("data", "logout")
            }else{ // 이메일, 구글계정
                intent.putExtra("data", "login")
            }
            startActivity(intent)
            //startActivity(Intent(this,AuthActivity::class.java))
        }

        if(item.itemId == R.id.graph){
            val intent = Intent(this, GraphActivity::class.java)
            startActivity(intent)
        }

        if(item.itemId == R.id.youtube){
            val intent = Intent(this, YoutubeActivity::class.java)
            startActivity(intent)
        }

        if(item.itemId == R.id.zipCode){
            val intent = Intent(this, ZipCodeActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}