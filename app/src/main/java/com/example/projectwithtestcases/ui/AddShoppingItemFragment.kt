package com.example.projectwithtestcases.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.example.projectwithtestcases.R
import com.example.projectwithtestcases.other.Status
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddShoppingItemFragment @Inject constructor(val glide:RequestManager): Fragment(R.layout.fragment_add_shopping_item) {

    lateinit var viewModel: ShoppingViewModel
    lateinit var ivShoppingImage:ImageView
    lateinit var btnAddShoppingItem:Button
    lateinit var etShoppingItemName:EditText
    lateinit var etShoppingItemAmount:EditText
    lateinit var etShoppingItemPrice:EditText
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
        btnAddShoppingItem = view.findViewById(R.id.btnAddShoppingItem)
        ivShoppingImage = view.findViewById(R.id.ivShoppingImage)
        etShoppingItemName = view.findViewById(R.id.etShoppingItemName)
        etShoppingItemAmount = view.findViewById(R.id.etShoppingItemAmount)
        etShoppingItemPrice = view.findViewById(R.id.etShoppingItemPrice)
        btnAddShoppingItem.setOnClickListener {
            viewModel.insertShoppingItem(
                etShoppingItemName.text.toString(),
                etShoppingItemAmount.text.toString(),
                etShoppingItemPrice.text.toString()
            )
        }
        ivShoppingImage.setOnClickListener {
            val action = AddShoppingItemFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment()
            findNavController().navigate(action)
        }
        val callBack = object:OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                viewModel.setCurImageUrl("")
                findNavController().popBackStack()
            }
        }
        subscribeToObservers()
        requireActivity().onBackPressedDispatcher.addCallback(callBack)
    }
    private fun subscribeToObservers(){
        viewModel.curImageUrl.observe(viewLifecycleOwner, Observer {
            glide.load(it).into(ivShoppingImage)
        })
        viewModel.insertShoppingItemStatus.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {result->
                when(result.status){
                    Status.SUCCESS->{
                     Toast.makeText(requireContext() ,"Added Shopping item",Toast.LENGTH_LONG ).show()
                    }
                    Status.ERROR->{
                        Toast.makeText(requireContext() ,result.message?:"An unknown error occured",Toast.LENGTH_LONG ).show()

                    }
                    Status.LOADING->{

                    }
                }
            }
        })
    }

    fun onSNACK(view: View){
        //Snackbar(view)
        val snackbar = Snackbar.make(view, "Replace with your own action",
            Snackbar.LENGTH_LONG).setAction("Action", null)
        snackbar.setActionTextColor(Color.BLUE)
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(Color.LTGRAY)
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.BLUE)
        textView.textSize = 28f
        snackbar.show()
    }
}