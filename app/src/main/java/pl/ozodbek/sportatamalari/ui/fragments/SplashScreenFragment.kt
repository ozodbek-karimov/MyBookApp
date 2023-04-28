package pl.ozodbek.sportatamalari.ui.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import pl.ozodbek.sportatamalari.R

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        Handler(Looper.getMainLooper()).postDelayed({
//            findNavController().navigate(R.id.action_splashScreenFragment_to_mainFragment)
//        }, 2500)

        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }


}