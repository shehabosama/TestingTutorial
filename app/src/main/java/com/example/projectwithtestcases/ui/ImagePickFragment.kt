package com.example.projectwithtestcases.ui

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectwithtestcases.R
import com.example.projectwithtestcases.adapters.ImageAdapter
import com.example.projectwithtestcases.other.Constants.GRID_SPAN_COUNT
import com.example.projectwithtestcases.other.Constants.SEARCH_TIME_DELAY
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImagePickFragment@Inject constructor(
    var imageAdapter: ImageAdapter
) : Fragment(R.layout.fragment_pick_image) {

    lateinit var viewModel: ShoppingViewModel
    lateinit var rvImages:RecyclerView
    lateinit var etSearch:EditText
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
        rvImages = view.findViewById(R.id.rvImages)
        etSearch = view.findViewById(R.id.etSearch)

        var job: Job? = null
        etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = lifecycleScope.launch {
                delay(SEARCH_TIME_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()) {
                        viewModel.searchForImage(editable.toString())
                    }
                }
            }
        }

        setupRecyclerView()
        imageAdapter.setOnItemClickListener {
            findNavController().popBackStack()
            viewModel.setCurImageUrl(it)
        }
    }

    private fun setupRecyclerView() {

        rvImages.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(requireContext(), GRID_SPAN_COUNT)
        }
    }
}