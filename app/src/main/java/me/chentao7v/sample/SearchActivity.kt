package me.chentao7v.sample

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_search.*
import me.chentao7v.widget.R

/**
 * @author leo
 */

class SearchActivity : AppCompatActivity() {

    companion object {

        fun actionStart(context: Context) {
            val intent = Intent(context, SearchActivity::class.java)
            context.startActivity(intent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)

        searchLayout.animatorListener = object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
                if (isReverse) {
                    finish()
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        searchLayout.start()
    }

    override fun onBackPressed() {
//        super.onBackPressed()

        searchLayout.revert()

    }

}