package me.chentao7v.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
    }

}