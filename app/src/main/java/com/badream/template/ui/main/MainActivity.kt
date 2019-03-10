package com.badream.template.ui.main

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.badream.template.BaseActivity
import com.badream.template.R
import com.badream.template.databinding.ActivityMainBinding
import com.badream.template.dto.RepoDto

class MainActivity : BaseActivity() {

    lateinit var binding : ActivityMainBinding
    lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.view = this

        viewModel = MainViewModel(this)
        viewModel.getRepoList("test")
    }


    fun setData( data : RepoDto){
        binding.model = data
    }
}
