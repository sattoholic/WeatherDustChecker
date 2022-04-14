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
import com.sattoholic.weatherdustchecker.databinding.FragmentWeatherPageBinding
import com.sattoholic.weatherdustchecker.viewmodel.FragmentsViewModel
import com.sattoholic.weatherdustchecker.viewmodel.FragmentsViewModelFactory

class WeatherPageFragment : Fragment() {
    lateinit var binding: FragmentWeatherPageBinding
    lateinit var viewModel: FragmentsViewModel

    private val repository = WeatherDustApplication.getInstance().repository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather_page, container, false)
        viewModel = ViewModelProvider(requireActivity(), FragmentsViewModelFactory(repository!!)).get(FragmentsViewModel::class.java)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.dataLoaded.observe(requireActivity()){
            if(it){
                binding.weatherStatusText.text =
                    when{
                        viewModel.weatherData.id.startsWith("2") ->{
                            binding.weatherIcon.setImageResource(R.drawable.ic_flash)
                            "천둥, 번개"
                        }
                        viewModel.weatherData.id.startsWith("3") ->{
                            binding.weatherIcon.setImageResource(R.drawable.ic_rain)
                            "이슬비"
                        }
                        viewModel.weatherData.id.startsWith("5") ->{
                            binding.weatherIcon.setImageResource(R.drawable.ic_rain)
                            "비"
                        }
                        viewModel.weatherData.id.startsWith("6") ->{
                            binding.weatherIcon.setImageResource(R.drawable.ic_snow)
                            "눈"
                        }
                        viewModel.weatherData.id.startsWith("7") ->{
                            binding.weatherIcon.setImageResource(R.drawable.ic_cloud)
                            "흐림"
                        }
                        viewModel.weatherData.id.equals("800") ->{
                            binding.weatherIcon.setImageResource(R.drawable.ic_sun)
                            "맑음"
                        }
                        viewModel.weatherData.id.startsWith("8") ->{
                            binding.weatherIcon.setImageResource(R.drawable.ic_cloudy)
                            "구름 낌"
                        }
                        else -> "알 수 없음"
                    }

                binding.invalidateAll()
            }
        }
        viewModel.dataFailed.observe(requireActivity()){
            if(it){
                Toast.makeText(activity, "에러 발생 : ${viewModel.dataFailThrow.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object{
        private var INSTANCE: WeatherPageFragment? = null

        fun getInstance(): WeatherPageFragment{
            if(INSTANCE == null){
                this.INSTANCE = WeatherPageFragment()
            }
            return INSTANCE!!
        }
    }
}