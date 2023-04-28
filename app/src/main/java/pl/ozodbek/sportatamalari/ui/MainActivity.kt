package pl.ozodbek.sportatamalari.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import pl.ozodbek.sportatamalari.R
import pl.ozodbek.sportatamalari.data.ChapterNameData
import pl.ozodbek.sportatamalari.databinding.ActivityMainBinding
import pl.ozodbek.sportatamalari.ui.fragments.MainFragmentDirections
import pl.ozodbek.sportatamalari.utils.Constants.Companion.MY_PREFERENCE
import pl.ozodbek.sportatamalari.utils.Constants.Companion.THEME_MODES

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        binding.navigationView.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drwaerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.pdf_show -> {
                    val action = MainFragmentDirections.actionMainFragmentToDetailFragment(
                        23, ChapterNameData("Umumiy kitob")
                    )
                    navController.navigate(action)
                    binding.drwaerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.translator -> {
                    navController.navigate(R.id.action_mainFragment_to_translatorFragment)
                    binding.drwaerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.dictionary -> {
                    navController.navigate(R.id.action_mainFragment_to_dictionaryFragment)
                    binding.drwaerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.franceEnglishDictionary -> {
                    navController.navigate(R.id.action_mainFragment_to_franceEnglishDictionaryFragment)
                    binding.drwaerLayout.closeDrawer(GravityCompat.START)
                    true
                }

//                R.id.theme_modes -> {
//                    sharedPreferences = getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE)
//                    val newMode = when (AppCompatDelegate.getDefaultNightMode()) {
//                        AppCompatDelegate.MODE_NIGHT_NO -> AppCompatDelegate.MODE_NIGHT_YES
//                        AppCompatDelegate.MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_NO
//                        else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
//                    }
//                    AppCompatDelegate.setDefaultNightMode(newMode)
//                    sharedPreferences.edit().putInt(THEME_MODES, newMode).apply()
//                    true
//                }

                R.id.about_id -> {
                    binding.drwaerLayout.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.action_mainFragment_to_authorFragment)
                    true
                }

                R.id.rating -> {
                    binding.drwaerLayout.closeDrawer(GravityCompat.START)
                    val packageName = applicationContext.packageName
                    val intent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    try {
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                            )
                        )
                    }

                    true
                }

                R.id.share -> {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(
                        Intent.EXTRA_SUBJECT,
                        "Ushbu dasturni sizga tavsiya etaman!"
                    )
                    shareIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        "Quyidagi link orqali yuklab oling: \"https://play.google.com/store/apps/details?id=$packageName\""
                    )
                    startActivity(Intent.createChooser(shareIntent, "Dasturni ulashish"))
                    binding.drwaerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.finish ->{
                    finish()
                    true
                }


                else -> false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}