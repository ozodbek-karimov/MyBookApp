package pl.ozodbek.sportatamalari.ui.fragments


import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import pl.ozodbek.sportatamalari.R
import pl.ozodbek.sportatamalari.databinding.FragmentDetailBinding
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
    private lateinit var pdfFileNames: Map<Int, String>


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        horizontalPageSetUp()
        setupMenu()
        scrollButtonSetUp()

        binding.pdfViewer.minZoom = 5.0f
        binding.pdfViewer.maxZoom = 3.0f
        binding.pdfViewer.midZoom = 2.5f


        return binding.root
    }

    private fun scrollButtonSetUp() {

        binding.apply {

            verticalButton.setOnClickListener {
                pdfViewer.jumpTo(pdfViewer.currentPage - 1, true)
            }

            verticalButton.setOnLongClickListener {
                pdfViewer.jumpTo(0, true)
                true
            }


            horizontalButton.setOnClickListener {
                pdfViewer.jumpTo(pdfViewer.currentPage - 1, true)
            }

            horizontalButton.setOnLongClickListener {
                pdfViewer.jumpTo(0, true)
                true
            }

        }

    }

    private fun horizontalPageSetUp() {
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
        binding.verticalButton.visibility = View.GONE
        binding.horizontalButton.visibility = View.VISIBLE
        pdfFileNames[args.chapterPostion]?.let {
            (requireActivity() as AppCompatActivity).supportActionBar?.title =
                args.chapterdata.bookName
            val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
            val savedPageNumber = sharedPref.getInt(SAVED_PAGE_LAST_BOOK, 0)


            binding.pdfViewer.fromAsset(it)
                .enableSwipe(true)
                .swipeHorizontal(true)
                .defaultPage(savedPageNumber)
                .enableAnnotationRendering(true)
                .pageFling(true)
                .autoSpacing(true)
                .pageSnap(true)
                .onPageScroll { _, dy ->
                    binding.horizontalButton.visibility = if (dy > 0) {
                        binding.pdfViewer.useBestQuality(true)
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }
                .onPageChange { page, pageCount ->

                    binding.textView14.text = "${page + 1} / $pageCount"
                    with(sharedPref.edit()) {
                        putInt(SAVED_PAGE_LAST_BOOK, page)
                        apply()
                    }
                }
                .load()

        }
    }

    private fun verticalPageSetUp() {
        binding.horizontalButton.visibility = View.GONE
        binding.verticalButton.visibility = View.VISIBLE
        pdfFileNames[args.chapterPostion]?.let {
            (requireActivity() as AppCompatActivity).supportActionBar?.title =
                args.chapterdata.bookName
            val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
            val savedPageNumber = sharedPref.getInt(SAVED_PAGE_LAST_BOOK, 0)

            binding.pdfViewer.fromAsset(it)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableAnnotationRendering(true)
                .defaultPage(savedPageNumber)
                .pageFling(true)
                .autoSpacing(true)
                .pageSnap(true)
                .onPageScroll { _, dy ->
                    binding.verticalButton.visibility = if (dy > 0) {
                        binding.pdfViewer.useBestQuality(true)
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }
                .onPageChange { page, pageCount ->

                    binding.textView14.text = "${page + 1} / $pageCount"
                    with(sharedPref.edit()) {
                        putInt(SAVED_PAGE_LAST_BOOK, page)
                        apply()
                    }
                }
                .load()

        }
    }

    private fun setupMenu() {
        val menuHost = requireActivity() as MenuHost
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.detail_menu, menu)

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                val zoom = binding.pdfViewer.zoom
                return when (menuItem.itemId) {
                    R.id.zoom_in -> {
                        if (zoom < MAX_ZOOM) {
                            binding.pdfViewer.zoomWithAnimation(zoom + 1f)
                        }
                        true
                    }

                    R.id.zoom_out -> {
                        if (zoom > MIN_ZOOM) {
                            binding.pdfViewer.zoomWithAnimation(zoom - 1f)
                        }
                        true
                    }

                    R.id.vertical -> {
                        verticalPageSetUp()
                        true
                    }

                    R.id.horizontal -> {
                        horizontalPageSetUp()
                        true
                    }

                    R.id.share -> {
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