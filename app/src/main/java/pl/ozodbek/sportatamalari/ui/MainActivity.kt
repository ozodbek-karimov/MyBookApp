package pl.ozodbek.sportatamalari.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import pl.ozodbek.sportatamalari.R
import pl.ozodbek.sportatamalari.data.ChaptersNames
import pl.ozodbek.sportatamalari.databinding.ActivityMainBinding
import pl.ozodbek.sportatamalari.ui.fragments.MainFragmentDirections


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
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.pdf_show -> {
                    val action = MainFragmentDirections.actionMainFragmentToDetailFragment(
                        23, ChaptersNames("Umumiy kitob")
                    )
                    navController.navigate(action)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.translator -> {
                    navController.navigate(R.id.action_mainFragment_to_translatorFragment)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.dictionary -> {
                    navController.navigate(R.id.action_mainFragment_to_dictionaryFragment)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.franceEnglishDictionary -> {
                    navController.navigate(R.id.action_mainFragment_to_franceEnglishDictionaryFragment)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.about_id -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.action_mainFragment_to_authorFragment)
                    true
                }

                R.id.rating -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)

                    val packageName = applicationContext.packageName
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$packageName")
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    val marketIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                    )

                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                    } else {
                        startActivity(marketIntent)
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
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.finish -> {
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