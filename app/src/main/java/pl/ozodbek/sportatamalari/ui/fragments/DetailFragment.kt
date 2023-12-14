package pl.ozodbek.sportatamalari.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import pl.ozodbek.sportatamalari.R
import pl.ozodbek.sportatamalari.databinding.FragmentDetailBinding
import pl.ozodbek.sportatamalari.utils.Constants.Companion.DEFAULT_ZOOM
import pl.ozodbek.sportatamalari.utils.Constants.Companion.KIRISH
import pl.ozodbek.sportatamalari.utils.Constants.Companion.MAX_ZOOM
import pl.ozodbek.sportatamalari.utils.Constants.Companion.MIN_ZOOM
import pl.ozodbek.sportatamalari.utils.Constants.Companion.SAVED_PAGE_LAST_BOOK
import pl.ozodbek.sportatamalari.utils.Constants.Companion.SET1
import pl.ozodbek.sportatamalari.utils.Constants.Companion.SET10
import pl.ozodbek.sportatamalari.utils.Constants.Companion.SET11
import pl.ozodbek.sportatamalari.utils.Constants.Companion.SET12
import pl.ozodbek.sportatamalari.utils.Constants.Companion.SET13
import pl.ozodbek.sportatamalari.utils.Constants.Companion.SET14
import pl.ozodbek.sportatamalari.utils.Constants.Companion.SET15
import pl.ozodbek.sportatamalari.utils.Constants.Companion.SET16
import pl.ozodbek.sportatamalari.utils.Constants.Companion.SET17
import pl.ozodbek.sportatamalari.utils.Constants.Companion.SET18
import pl.ozodbek.sportatamalari.utils.Constants.Companion.SET19
import pl.ozodbek.sportatamalari.utils.Constants.Companion.SET2
import pl.ozodbek.sportatamalari.utils.Constants.Companion.SET20
import pl.ozodbek.sportatamalari.utils.Constants.Companion.SET21
import pl.ozodbek.sportatamalari.utils.Constants.Companion.SET22
import pl.ozodbek.sportatamalari.utils.Constants.Companion.SET3
import pl.ozodbek.sportatamalari.utils.Constants.Companion.SET4
import pl.ozodbek.sportatamalari.utils.Constants.Companion.SET5
import pl.ozodbek.sportatamalari.utils.Constants.Companion.SET6
import pl.ozodbek.sportatamalari.utils.Constants.Companion.SET7
import pl.ozodbek.sportatamalari.utils.Constants.Companion.SET8
import pl.ozodbek.sportatamalari.utils.Constants.Companion.SET9
import pl.ozodbek.sportatamalari.utils.Constants.Companion.UMUMIY_NEW


class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val args: DetailFragmentArgs by navArgs()
    private var pdfFileNames: Map<Int, String>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        /**  DEFAULT PAGE SET UP  */
        horizontalPageSetUp()

        /**   MENU SET UP  */
        setupMenu()

        /**  SET UP NAVIGATE BUTTONS  */
        setUpNavigateButton(binding.verticalButton)
        setUpNavigateButton(binding.horizontalButton)


        return binding.root
    }


    private fun setUpNavigateButton(button: ImageView) {
        button.setOnClickListener {
            binding.pdfViewer.resetZoom()
            binding.pdfViewer.jumpTo(binding.pdfViewer.currentPage - 1, true)
        }

        button.setOnLongClickListener {
            binding.pdfViewer.resetZoom()
            binding.pdfViewer.jumpTo(0, true)
            true
        }
    }

    private fun setUpPdfViewer(swipeHorizontal: Boolean) {

        pdfFileNames = mapOf(
            0 to KIRISH,
            1 to SET1,
            2 to SET2,
            3 to SET3,
            4 to SET4,
            5 to SET5,
            6 to SET6,
            7 to SET7,
            8 to SET8,
            9 to SET9,
            10 to SET10,
            11 to SET11,
            12 to SET12,
            13 to SET13,
            14 to SET14,
            15 to SET15,
            16 to SET16,
            17 to SET17,
            18 to SET18,
            19 to SET19,
            20 to SET20,
            21 to SET21,
            22 to SET22,
            23 to UMUMIY_NEW
        )

        binding.apply {
            horizontalButton.visibility = if (swipeHorizontal) View.VISIBLE else View.GONE
            verticalButton.visibility = if (swipeHorizontal) View.GONE else View.VISIBLE

            pdfFileNames?.get(args.chapterPostion)?.let {
                (requireActivity() as AppCompatActivity).supportActionBar?.title =
                    args.chapterdata.bookName
                val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
                val savedPageNumber = sharedPref.getInt(SAVED_PAGE_LAST_BOOK, 0)

                pdfViewer.fromAsset(it)
                    .enableSwipe(true)
                    .swipeHorizontal(swipeHorizontal)
                    .enableAnnotationRendering(true)
                    .defaultPage(savedPageNumber)
                    .pageFling(true)
                    .autoSpacing(true)
                    .pageSnap(true)
                    .onPageScroll { currentPageIndex, dx ->

                        horizontalButton.visibility =
                            if (swipeHorizontal && currentPageIndex > 0 && dx > 0) {
                                pdfViewer.useBestQuality(true)
                                View.VISIBLE
                            } else {
                                View.GONE
                            }

                        verticalButton.visibility =
                            if (!swipeHorizontal && currentPageIndex > 0 && dx > 0) {
                                pdfViewer.useBestQuality(true)
                                View.VISIBLE
                            } else {
                                View.GONE
                            }

                    }
                    .onPageChange { page, pageCount ->
                        pageCounterTv.text =
                            getString(R.string.page_counter, page + 1, pageCount)
                        with(sharedPref.edit()) {
                            putInt(SAVED_PAGE_LAST_BOOK, page)
                            apply()
                        }
                    }
                    .load()
            }
        }
    }

    private fun horizontalPageSetUp() {
        setUpPdfViewer(true)
    }

    private fun verticalPageSetUp() {
        setUpPdfViewer(false)
    }

    private fun setupMenu() {
        val menuHost = requireActivity() as MenuHost
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.detail_menu, menu)

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                var zoom = DEFAULT_ZOOM
                return when (menuItem.itemId) {
                    R.id.zoom_in -> {
                        if (zoom < MAX_ZOOM) {
                            zoom += 1f
                            binding.pdfViewer.zoomWithAnimation(zoom)
                        } else {
                            zoom = DEFAULT_ZOOM
                            binding.pdfViewer.zoomWithAnimation(zoom)
                        }
                        true
                    }

                    R.id.zoom_out -> {
                        if (zoom > MIN_ZOOM) {
                            zoom -= 1f
                            binding.pdfViewer.zoomWithAnimation(zoom)
                        } else {
                            zoom = DEFAULT_ZOOM
                            binding.pdfViewer.zoomWithAnimation(zoom)
                        }
                        true
                    }

                    R.id.vertical -> {
                        binding.pdfViewer.invalidate()

                        verticalPageSetUp()
                        true
                    }

                    R.id.horizontal -> {
                        binding.pdfViewer.invalidate()
                        horizontalPageSetUp()
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
}