package com.template.badgedrawablesample.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.template.badgedrawablesample.R

class HomeFragment : Fragment() {
    private val homeViewModel by activityViewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.button_count_nav_view)
            .setOnClickListener {
                val count = getBadgeCount(view) ?: return@setOnClickListener
                homeViewModel.badgeCount.postValue(count)
            }
        view.findViewById<Button>(R.id.button_count_fab).setOnClickListener {
            val count = getBadgeCount(view) ?: return@setOnClickListener
            homeViewModel.badgeCountFab.postValue(count)
        }
        view.findViewById<Button>(R.id.button_remove_badge)
            .setOnClickListener { homeViewModel.removeBadge.value = Unit }
    }

    private fun getBadgeCount(view: View): Int? {
        val countText = view.findViewById<EditText>(R.id.editTextNumberSigned).text.toString()
        return kotlin.runCatching { Integer.parseInt(countText) }.getOrNull()
    }
}