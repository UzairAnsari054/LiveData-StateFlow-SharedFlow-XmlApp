package com.example.livedatastateflowsharedflowxmlapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.livedatastateflowsharedflowxmlapp.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.lifecycleOwner = this
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Live Data
        viewModel.liveData.observe(this) {
            binding.liveDataTv.text = it.toString()
        }
        binding.liveDataBtn.setOnClickListener {
            viewModel.triggerLiveData()
        }

        // State Flow
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collectLatest {
                    binding.stateFlowTv.text = it
                }
            }
        }
        binding.stateFlowBtn.setOnClickListener {
            viewModel.triggerStateFlow()
        }

        // Normal Flow
        binding.normalFlowBtn.setOnClickListener {
            lifecycleScope.launch {
                viewModel.triggerNormalFlow().collectLatest {
                    binding.normalFlowTv.text = it
                }
            }

        }

        //SharedFlow
        binding.sharedFlowBtn.setOnClickListener {
            viewModel.triggerSharedFlow()
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sharedFlow.collect {
                    Log.d("SharedFlow", "Collected: $it")
                    binding.sharedFlowTv.text = it
                }
            }
        }
    }

}