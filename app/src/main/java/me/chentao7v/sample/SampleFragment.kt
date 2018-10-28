package me.chentao7v.sample

import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.sample_progress.view.*
import kotlinx.android.synthetic.main.sample_wtach.view.*
import me.chentao7v.widget.R

/**
 * @author leo
 */

class SampleWatchFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.sample_wtach, container, false)

        root.btnStartWatch.setOnClickListener {
            root.appleWatch.start()
        }

        return root
    }

}

class SampleProgressFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.sample_progress, container, false)

        root.btnStartProgress.setOnClickListener {
            val animator = ObjectAnimator.ofFloat(root.progressBar, "progress", 0f, 100f)
            animator.duration = 3000
            animator.start()
        }

        return root
    }
}

class SampleWeekendFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sample_weekwend, container, false)
    }
}