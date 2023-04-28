package pl.ozodbek.sportatamalari.ui.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.review.ReviewManagerFactory
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import pl.ozodbek.sportatamalari.R
import pl.ozodbek.sportatamalari.adapters.BookAdapter
import pl.ozodbek.sportatamalari.data.ChaptersNames
import pl.ozodbek.sportatamalari.databinding.AppExitDialogBinding
import pl.ozodbek.sportatamalari.databinding.FragmentMainBinding
import pl.ozodbek.sportatamalari.utils.hideKeyboard


class MainFragment : Fragment(), SearchView.OnQueryTextListener{

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookAdapter: BookAdapter


    private var list = listOf(
        ChaptersNames("Kirish"),
        ChaptersNames("Sportchi"),
        ChaptersNames("Tartib"),
        ChaptersNames("Mashq,Musobaqa"),
        ChaptersNames("Sport turlari.."),
        ChaptersNames("Stadion"),
        ChaptersNames("Alteizm"),
        ChaptersNames("Yugurish"),
        ChaptersNames("Qilichbozlik"),
        ChaptersNames("Boks"),
        ChaptersNames("Kurash"),
        ChaptersNames("Tennis"),
        ChaptersNames("Ot sporti"),
        ChaptersNames("Velosiped sporti"),
        ChaptersNames("Suzish"),
        ChaptersNames("Eshkak eshish"),
        ChaptersNames("Hokkey"),
        ChaptersNames("Alpinizm"),
        ChaptersNames("Ov"),
        ChaptersNames("Basket"),
        ChaptersNames("Futbol"),
        ChaptersNames("Shaxmat"),
        ChaptersNames("Qishki olimpiada"),


        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMenu()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        hideKeyboard(requireActivity())

        bookAdapter = BookAdapter(list) { chapterDataName, position ->
            val action =
                MainFragmentDirections.actionMainFragmentToDetailFragment(position, chapterDataName)
            findNavController().navigate(action)
        }


        binding.recyclerview.adapter = bookAdapter
        binding.recyclerview.itemAnimator = SlideInUpAnimator().apply {
            bookAdapter.setData(list)
            addDuration = 200
        }


        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (findNavController().currentDestination?.id == R.id.mainFragment) {
                    showExitDialog()
                } else {
                    findNavController().navigateUp()
                }

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


        return binding.root
    }

    private fun showExitDialog() {
        val binding =
            AppExitDialogBinding.inflate(LayoutInflater.from(requireContext()))
        val builder = AlertDialog.Builder(requireContext()).apply {
            setView(binding.root)
        }
        val dialog = builder.create().apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(false)
        }

        binding.apply {
            xaButton.setOnClickListener {
                requireActivity().finish()
            }

            yoqButton.setOnClickListener {
                dialog.dismiss()
            }


            rating.setOnClickListener {
                val reviewManager = ReviewManagerFactory.create(requireActivity())
                val request = reviewManager.requestReviewFlow()
                request.addOnCompleteListener {
                    if (it.isSuccessful) {
                        val reviewInfo = it.result
                        val flow = reviewManager.launchReviewFlow(requireActivity(), reviewInfo)
                        flow.addOnCompleteListener {
                            Snackbar.make(requireView(), "Thank you !", Snackbar.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    private fun setMenu() {
        val menuHost = requireActivity() as MenuHost
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.mainfragment_menu, menu)


                val search = menu.findItem(R.id.search)
                val searchView = search.actionView as? SearchView
                searchView?.isSubmitButtonEnabled = true
                searchView?.setOnQueryTextListener(this@MainFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.about_id -> {
                        findNavController().navigate(R.id.action_mainFragment_to_authorFragment)
                        true
                    }

                    R.id.search -> {
                        true
                    }

                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard(requireActivity())

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let { query ->
            val filteredList = list.filter { chapterNameData ->
                chapterNameData.bookName.contains(query, ignoreCase = true)
            }
            bookAdapter.setData(filteredList)
        } ?: bookAdapter.setData(list)
        return true
    }


}