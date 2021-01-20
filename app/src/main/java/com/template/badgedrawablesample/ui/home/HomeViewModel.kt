package com.template.badgedrawablesample.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    val badgeCount = MutableLiveData(0)
    val badgeCountFab = MutableLiveData(0)
    val removeBadge = MutableLiveData<Unit>()
}