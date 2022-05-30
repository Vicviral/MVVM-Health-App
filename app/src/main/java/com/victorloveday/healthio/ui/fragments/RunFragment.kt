package com.victorloveday.healthio.ui.fragments

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.victorloveday.healthio.R
import com.victorloveday.healthio.adapters.RunAdapter
import com.victorloveday.healthio.databinding.FragmentRunBinding
import com.victorloveday.healthio.ui.viewmodels.MainViewModel
import com.victorloveday.healthio.utils.SortRunType
import com.victorloveday.healthio.utils.constants.Constant.REQUEST_CODE_LOCATION_PERMISSION
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*

@AndroidEntryPoint
class RunFragment : Fragment(R.layout.fragment_run), EasyPermissions.PermissionCallbacks {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: FragmentRunBinding
    private lateinit var runAdapter: RunAdapter
    private var menu: Menu? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRunBinding.bind(view)

        //request permissions
        requestPermissions()

        //display recycler view
        setUpRunHistoryRecyclerView()

        viewModel.runsSortedByCaloriesBurnt.observe(viewLifecycleOwner, Observer {
            runAdapter.submitList(it)
        })

        binding.newRun.setOnClickListener {
            findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
        }

        //update total runs on toolbar
        activity?.actionBar?.title = "Hello"

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_run_filter, menu)
        this.menu = menu
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.searchRun -> {
                Toast.makeText(requireContext(), "Coming soon..", Toast.LENGTH_SHORT).show()
            }
            R.id.sortRunByDate -> {
                viewModel.runsSortedByDate.observe(viewLifecycleOwner, Observer {
                    runAdapter.submitList(it)
                })
            }
            R.id.sortRunByTime -> {
                viewModel.runsSortedByTime.observe(viewLifecycleOwner, Observer {
                    runAdapter.submitList(it)
                })
            }
            R.id.sortRunByDistance -> {
                viewModel.runsSortedByDistance.observe(viewLifecycleOwner, Observer {
                    runAdapter.submitList(it)
                })
            }
            R.id.sortRunByAvgSpeed -> {
                viewModel.runsSortedByAverageSpeed.observe(viewLifecycleOwner, Observer {
                    runAdapter.submitList(it)
                })
            }
            R.id.sortRunByCaloriesBurned -> {
                viewModel.runsSortedByCaloriesBurnt.observe(viewLifecycleOwner, Observer {
                    runAdapter.submitList(it)
                })
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setUpRunHistoryRecyclerView() = binding.runsHistory.apply {
        runAdapter = RunAdapter()
        adapter = runAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun requestPermissions() {
        if (hasLocationPermissions(requireContext())) {
            return
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "This app needs permission to your location to function properly",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }else {
            EasyPermissions.requestPermissions(
                this,
                "This app needs permission to your location to function properly",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )

        }
    }

    private fun hasLocationPermissions(context: Context) =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
//        TODO("Not yet implemented")
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }else {
            requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}