package pl.ozodbek.sportatamalari.ui.fragments


import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pl.ozodbek.sportatamalari.R
import pl.ozodbek.sportatamalari.databinding.FragmentAuthorBinding


class AuthorFragment : Fragment() {

    private var _binding: FragmentAuthorBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthorBinding.inflate(inflater, container, false)

        binding.ratingSend.setOnClickListener {
            val packageName = requireActivity().packageName
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
        }


        binding.emailSend.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.type = "text/html"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("ozodbekkarimov100@gmail.com"))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject of the email")
            emailIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Good Afternoon, .."
            )
            startActivity(Intent.createChooser(emailIntent, "Send Email"))

            startActivity(emailIntent)

        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}