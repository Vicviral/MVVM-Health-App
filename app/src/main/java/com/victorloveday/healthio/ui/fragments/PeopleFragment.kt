package com.victorloveday.healthio.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.victorloveday.healthio.R
import com.victorloveday.healthio.ui.viewmodels.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PeopleFragment: Fragment(R.layout.fragment_people) {

    private val viewModel: StatisticsViewModel by viewModels()
}