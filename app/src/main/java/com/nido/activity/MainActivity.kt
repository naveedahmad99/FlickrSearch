package com.nido.activity

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.nido.R
import com.nido.databinding.ActivityMainBinding
import com.nido.databinding.ItemImageBinding
import com.nido.imagegrid.GenericAdapter
import com.nido.imagegrid.SpanningGridLayoutManager
import com.nido.viewmodel.MainActivityViewModel
import com.nido.viewmodel.PhotoData


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private val adapter = object: GenericAdapter<PhotoData, ItemImageBinding>() {
        override var resId: Int = R.layout.item_image

        override fun bindToView(data: PhotoData, itemBinding: ItemImageBinding) {
            itemBinding.data = data
            itemBinding.executePendingBindings()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        binding.data = viewModel
        binding.imageButtonSearch.setOnClickListener {
            processSearch()
        }

        binding.editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                processSearch()
            }
            true
        }

        binding.imageList.post{
            binding.imageList.layoutManager = SpanningGridLayoutManager(this,
                SPAN_COUNT, binding.imageList.width)
            binding.imageList.adapter = adapter
        }
        viewModel.photoData.observe(this, Observer {
            adapter.setData(it)
        })
    }

    private fun processSearch() {
        Handler().postDelayed({
            hideSoftKeyboard()
        }, 500)

        if (!isConnectedToInternet(this)) {
            Toast.makeText(this, R.string.no_internet, LENGTH_SHORT).show()
        } else {
            if (binding.editTextSearch.text.toString().isBlank()) {
                Toast.makeText(this, R.string.search_blank_error_message, LENGTH_SHORT).show()
            } else {
                viewModel.getImagesFromFlicker(binding.editTextSearch.text.toString())
            }
        }
    }

    private fun hideSoftKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.editTextSearch.windowToken, 0)
    }

    companion object {
        private const val SPAN_COUNT = 3
    }

    fun isConnectedToInternet(context: Context): Boolean {
        val manager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        //For 3G check
        var is3g = false
        is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            .isConnectedOrConnecting
        //For WiFi Check
        var isWifi = false
        isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            .isConnectedOrConnecting

        println("$is3g net $isWifi")

        return !(!is3g && !isWifi)
    }
}
