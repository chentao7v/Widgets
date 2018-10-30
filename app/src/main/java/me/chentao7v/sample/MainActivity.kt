package me.chentao7v.sample

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_search.*
import me.chentao7v.widget.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabLayout.setupWithViewPager(viewPager)
        viewPager.adapter = SimpleAdapter(supportFragmentManager)


        searchLayout.animatorListener = object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
                if (isReverse) {
                    searchContainer.visibility = View.GONE
                } else {
                    searchContainer.setBackgroundColor(Color.parseColor("#80000000"))
                }
            }
        }
        ibtnSearch.setOnClickListener {
            searchContainer.visibility = View.VISIBLE
            searchLayout.start()
        }
    }

    private class SimpleAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

        private val fragments = mutableListOf<Fragment>()

        init {
            fragments.add(SampleWatchFragment())
            fragments.add(SampleProgressFragment())
            fragments.add(SampleWeekendFragment())
        }

        override fun getItem(position: Int) = fragments[position]

        override fun getCount() = fragments.size

        override fun getPageTitle(position: Int) = when (position) {
            0 -> "手表"
            1 -> "进度条"
            else -> "打卡控件"
        }
    }

    override fun onBackPressed() {
        if (!searchContainer.isShown) {
            super.onBackPressed()
            return
        }

        searchLayout.revert()

    }
}
