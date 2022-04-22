package com.sattoholic.weatherdustchecker.activitiy

import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.sattoholic.weatherdustchecker.R
import com.sattoholic.weatherdustchecker.WeatherDustApplication
import com.sattoholic.weatherdustchecker.databinding.ActivityMainBinding
import com.sattoholic.weatherdustchecker.fragments.DustPageFragment
import com.sattoholic.weatherdustchecker.fragments.ViewPagerAdapter
import com.sattoholic.weatherdustchecker.fragments.WeatherPageFragment
import com.sattoholic.weatherdustchecker.viewmodel.FragmentsViewModel
import com.sattoholic.weatherdustchecker.viewmodel.FragmentsViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var requestPermissions: ActivityResultLauncher<Array<String>>
    private lateinit var locationManager: LocationManager

    private val viewModel by lazy {
        ViewModelProvider(this, FragmentsViewModelFactory(repository!!)).get(
            FragmentsViewModel::class.java
        )
    }
    private val adapter = ViewPagerAdapter(this)
    private val repository by lazy {
        WeatherDustApplication.getInstance(
            resources.getString(R.string.OPEN_WEATHER_API),
            resources.getString(R.string.DUST_APP_TOKEN)
        ).repository
    }
    private val fragmentList = mutableListOf<Fragment>()
    private val tabTile = arrayOf("날씨", "미세먼지")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)

        supportActionBar?.hide()

        locationManager = getSystemService(LocationManager::class.java)
        binding.viewPager.adapter = adapter

        if (viewModel.dataLoaded.value!!) {
            binding.progress.visibility = View.GONE
            binding.progressText.visibility = View.GONE
            binding.viewPager.visibility = View.VISIBLE
            binding.tabLayout.visibility = View.VISIBLE
        }

        viewModel.locationUpdated.observe(this) {
            if (it) {
                viewModel.updateWeather()
                viewModel.updateDust()
            }
        }

        viewModel.dataLoaded.observe(this) {
            if (it) {
                binding.progress.visibility = View.GONE
                binding.progressText.visibility = View.GONE
                binding.viewPager.visibility = View.VISIBLE
                binding.tabLayout.visibility = View.VISIBLE
                binding.pullToRefresh.isRefreshing = false
            } else {
                binding.tabLayout.visibility = View.GONE
                binding.viewPager.visibility = View.GONE
                binding.progress.visibility = View.VISIBLE
                binding.progressText.visibility = View.VISIBLE
            }
        }
        fragmentList.add(WeatherPageFragment.getInstance())
        fragmentList.add(DustPageFragment.getInstance())
        adapter.setFragments(fragmentList)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTile[position]
        }.attach()


        binding.pullToRefresh.setOnRefreshListener {
            viewModel.updateLocation(locationManager)
        }

        requestPermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                if (it.all { it.value }) {
                    viewModel.updateLocation(locationManager)
                } else {
                    Toast.makeText(this, "위치정보 권한은 필수 입니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

        if ((checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            if (!viewModel.locationUpdated.value!!) {
                viewModel.updateLocation(locationManager)
            }
        }
    }
}