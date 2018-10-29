package me.chentao7v.sample

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import me.chentao7v.widget.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabLayout.setupWithViewPager(viewPager)
        viewPager.adapter = SimpleAdapter(supportFragmentManager)

        ibtnSearch.setOnClickListener { SearchActivity.actionStart(this@MainActivity) }
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
}
