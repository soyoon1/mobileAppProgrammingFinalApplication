package com.example.finalapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.finalapplication.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {
    lateinit var binding : ActivityWebViewBinding
    inner class KakaoJavaScriptInterface{
        @JavascriptInterface
        fun processDATA(data: String?){
            val intent = Intent()
            intent.putExtra("result", data)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_web_view)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.title="우편 주소 검색하기"

        binding.webView.apply{
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.setSupportMultipleWindows(true)
            settings.setDomStorageEnabled(true)
            addJavascriptInterface(KakaoJavaScriptInterface(), "Android")
            webViewClient = object:WebViewClient(){
                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    request: String
                ): Boolean {
                    view.loadUrl(request)
                    return true
                }
            }
        }
        binding.webView.loadUrl("https://myfinalproject-14925.web.app/daum3.html")
    }
}