package ee.solarlabs.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewClientCompat
import ee.solarlabs.ui.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val connectViewModel = ConnectViewModel()

    private val assetLoader = WebViewAssetLoader.Builder()
        .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(this))
        .build()


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        connectViewModel.connectionState.observe(this) {
            when (it) {
                ConnectionState.CheckPermission -> checkPermission()
                else -> {

                }
            }
        }

        binding.wvUI.apply {
            webViewClient = object : WebViewClientCompat() {
                override fun shouldInterceptRequest(
                    view: WebView,
                    request: WebResourceRequest
                ): WebResourceResponse? {
                    return assetLoader.shouldInterceptRequest(request.url)
                }
            }

            settings.javaScriptEnabled = true
            loadUrl("https://appassets.androidplatform.net/assets/www/index.html")
        }
    }

    private fun checkPermission() {
        val permissionIntent = connectViewModel.vpnCheckIntent(this) ?: let {
            permissionResultConnect(true)
            return
        }
        permissionLauncher.launch(permissionIntent)
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            permissionResultConnect(result.resultCode == Activity.RESULT_OK)
        }

    private fun permissionResultConnect(hasPermission: Boolean) {
        if (hasPermission) {
            connectViewModel.attemptConnection()
        } else {
            // permission refused.
        }
    }
}