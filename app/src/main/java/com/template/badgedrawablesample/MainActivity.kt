package com.template.badgedrawablesample

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.template.badgedrawablesample.ui.home.HomeViewModel

class MainActivity : AppCompatActivity() {

    private val homeViewModel by viewModels<HomeViewModel>()
    private var navView: BottomNavigationView? = null
    private var badgeFab: BadgeDrawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView?.setupWithNavController(navController)

        setupBadgeFab()
        observeLiveData()
    }

    private fun observeLiveData() {
        homeViewModel.badgeCount.observe(this, Observer {
            setBadgeCount(R.id.navigation_home, it)
        })
        homeViewModel.badgeCountFab.observe(this, Observer {
            val count = it ?: return@Observer
            val fabButton = findViewById<FloatingActionButton>(R.id.floating_action_button)
            badgeFab?.apply {
                isVisible = count != 0
                number = count
                updateBadgeCoordinates(fabButton, null)
            }
        })
        homeViewModel.removeBadge.observe(this, Observer {
            removeBadge(R.id.navigation_home)
        })
    }

    private fun setupBadgeFab() {
        val fabButton = findViewById<FloatingActionButton>(R.id.floating_action_button)
        badgeFab = BadgeDrawable.create(this).apply {
            attachBadgeDrawable(fabButton, this)
        }
    }

    private fun attachBadgeDrawable(anchor: View, badgeDrawable: BadgeDrawable) {
        anchor.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            @SuppressLint("UnsafeExperimentalUsageError")
            override fun onGlobalLayout() {
                badgeDrawable.apply {
                    backgroundColor =
                        ResourcesCompat.getColor(applicationContext.resources, R.color.red, null)
                    number = 0
                    isVisible = false
                    horizontalOffset = 28
                    verticalOffset = 20
                    BadgeUtils.attachBadgeDrawable(this, anchor, null)
                }
                anchor.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    /**
     * 指定したボトムバーのメニューのバッジの数値を更新する
     *
     * @param menuItemId　ボトムバーのメニューId
     * @param value バッジに表示する数値 999以上だと 999+表記になる
     */
    private fun setBadgeCount(menuItemId: Int, value: Int) {
        // https://material.io/develop/android/components/bottom-navigation - Adding badges
        // getOrCreateBadge使って、すでに初期化済みならそのバッジを再利用する
        val badge = navView?.getOrCreateBadge(menuItemId) ?: return
        badge.backgroundColor = getColor(R.color.red)
        badge.badgeTextColor = getColor(R.color.white)

        badge.number = value // 表示する数値

        // 0の場合はバッジを非表示にする
        if (badge.number == 0) {
            if (badge.isVisible) inVisibleBadge(menuItemId)
        } else {
            if (!badge.isVisible) badge.isVisible = true // バッチを表示する
        }
    }

    /**
     * 指定したボトムバーのメニューのバッジを非表示にする
     */
    private fun inVisibleBadge(menuItemId: Int) {
        val badge = navView?.getBadge(menuItemId) ?: return
        badge.clearNumber() // これだけだと、赤い丸ポチが残る
        badge.isVisible = false // バッチを非表示にする
    }

    /**
     * 指定したボトムバーのメニューのバッジを削除する
     */
    private fun removeBadge(menuItemId: Int) {
        navView?.removeBadge(menuItemId)
        val fabButton = findViewById<FloatingActionButton>(R.id.floating_action_button)
        badgeFab?.apply {
            isVisible = false
            number = 0
            updateBadgeCoordinates(fabButton, null)
        }
    }
}