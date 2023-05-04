package pl.ozodbek.sportatamalari.ui.fragments.webviews

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import pl.ozodbek.sportatamalari.R
import pl.ozodbek.sportatamalari.databinding.FragmentTranslatorBinding
import pl.ozodbek.sportatamalari.utils.Constants.Companion.MY_PREFERENCE
import pl.ozodbek.sportatamalari.utils.Constants.Companion.SAVED_PAGE_GOOGLE
import pl.ozodbek.sportatamalari.utils.Constants.Companion.WEB_VIEW_1
import pl.ozodbek.sportatamalari.utils.hideKeyboard

class GoogleTranslatorFragment : Fragment() {

    private var _binding: FragmentTranslatorBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences

    private val connectivityReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            networkSetUp()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTranslatorBinding.inflate(inflater, container, false)

        webviewSetUp()
        menuSetUp()
        hideKeyboard(requireActivity())
        binding.refreshLayout.isEnabled = false

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.webview.canGoBack()) {
                    binding.webview.goBack()
                } else {
                    findNavController().navigateUp()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        return binding.root
    }

    private fun networkSetUp() {
        if (isConnected()) {
            val lastUrl = sharedPreferences.getString(SAVED_PAGE_GOOGLE, WEB_VIEW_1)
            binding.webview.visibility = View.VISIBLE
            binding.webview.loadUrl(lastUrl ?: WEB_VIEW_1)
            binding.animationView.visibility = View.GONE
            binding.networkTv.visibility = View.GONE
            binding.networkToggleButton.visibility = View.GONE
        } else {
            binding.webview.visibility = View.GONE
            binding.animationView.visibility = View.VISIBLE
            binding.networkTv.visibility = View.VISIBLE
            binding.networkToggleButton.visibility = View.VISIBLE
            binding.networkToggleButton.setOnClickListener {
                val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(intent)
            }
        }
    }

    private fun menuSetUp() {
        val menuHost = requireActivity() as MenuHost
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.webview_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.refresh -> {
                        binding.refreshLayout.isRefreshing = true
                        binding.webview.reload()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun webviewSetUp() {
        sharedPreferences =
            requireContext().getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE)
        binding.webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                _binding?.let {
                    binding.refreshLayout.isRefreshing = false
                }
            }
        }
        val lastUrl = sharedPreferences.getString(SAVED_PAGE_GOOGLE, WEB_VIEW_1)
        binding.webview.loadUrl(lastUrl ?: WEB_VIEW_1)
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
    }


    private fun isConnected(): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        return connectivityManager?.let { manager ->
            val networkCapabilities = manager.getNetworkCapabilities(manager.activeNetwork)
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } ?: false
    }


    override fun onPause() {
        super.onPause()
        val currentUrl = binding.webview.url ?: WEB_VIEW_1
        sharedPreferences.edit().putString(SAVED_PAGE_GOOGLE, currentUrl).apply()
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context?.registerReceiver(connectivityReceiver, filter)
        networkSetUp()
    }


    override fun onStop() {
        super.onStop()
        context?.unregisterReceiver(connectivityReceiver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
