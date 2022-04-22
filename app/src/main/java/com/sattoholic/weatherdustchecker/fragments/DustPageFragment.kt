package com.sattoholic.weatherdustchecker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sattoholic.weatherdustchecker.R
import com.sattoholic.weatherdustchecker.WeatherDustApplication
import com.sattoholic.weatherdustchecker.databinding.FragmentDustPageBinding
import com.sattoholic.weatherdustchecker.viewmodel.FragmentsViewModel
import com.sattoholic.weatherdustchecker.viewmodel.FragmentsViewModelFactory


class DustPageFragment : Fragment() {
    lateinit var binding: FragmentDustPageBinding
    lateinit var viewModel: FragmentsViewModel

    private var repository = WeatherDustApplication.getInstance().repository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dust_page, container, false)
        viewModel =
            ViewModelProvider(requireActivity(), FragmentsViewModelFactory(repository!!)).get(
                FragmentsViewModel::class.java
            )
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.dataLoaded.observe(requireActivity()) {
            if (it) {
                binding.dustStatusIcon.setImageResource(
                    when (viewModel.dustData.pm25Status) {
                        "좋음" -> R.drawable.ic_good
                        "보통" -> R.drawable.ic_normal
                        "나쁨" -> R.drawable.ic_bad
                        else -> R.drawable.ic_very_bad
                    }
                )
                binding.invalidateAll()
            }
        }
        viewModel.dataFailed.observe(requireActivity()) {
            if (it) {
                Toast.makeText(
                    activity,
                    "에러 발생 : ${viewModel.dataFailThrow.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        private var INSTANCE: DustPageFragment? = null

        fun getInstance(): DustPageFragment {
            if (INSTANCE == null) {
                INSTANCE = DustPageFragment()
            }
            return INSTANCE!!
        }
    }
}