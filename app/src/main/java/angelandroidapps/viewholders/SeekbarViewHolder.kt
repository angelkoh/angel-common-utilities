@file:Suppress("unused")

package angelandroidapps.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.Keep
import angelandroidapps.library.R


// * Created by Angel on 11/21/2019 10:31 AM.  
// * Originally created for project "Eycercise".
// * Copyright (c) 2019 Angel. All rights reserved. 
@Keep
open class SeekbarViewHolder(
        parentView: View,
        titleResId: Int,
        private val progressResId: Int,
        val min: Int, max: Int,
        imageResId: Int = 0,
        callback: (newProgress: Int) -> Unit
) {


    var value = min

    private val tvMin: TextView
    private val tvMax: TextView
    private val tvSbTitle: TextView
    private val tvValue: TextView
    private val sb: SeekBar
    private val iv: ImageView

    init {

        parentView.apply {
            tvMin = findViewById(R.id.tv_min)
            tvMax = findViewById(R.id.tv_max)
            tvSbTitle = findViewById(R.id.tv_sb_title)
            tvValue = findViewById(R.id.tv_value)
            sb = findViewById(R.id.sb)
            iv = findViewById(R.id.iv_sb)

//            tvMin.setTextColor(textColor)
//            tvMax.setTextColor(textColor)
//            tvSbTitle.setTextColor(textColor)
//            tvValue.setTextColor(textColor)

            tvMin.text = "$min"
            tvMax.text = "$max"
            tvSbTitle.setText(titleResId)

            if (imageResId != 0) {
                iv.visibility = View.VISIBLE
                iv.setImageResource(imageResId)
            } else {
                iv.visibility = View.GONE
            }

            sb.max = max - min

            sb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                        seekBar: SeekBar?, newProgress: Int, fromUser: Boolean
                ) {
                    val newValue = newProgress + min
                    tvValue.text = "$newValue"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    seekBar?.let {
                        val newValue = it.progress + min
                        if (newValue != value) {
                            value = newValue
                            callback.invoke(value)
                        }
                    }
                }

            })
        }

    }

    open fun setProgress(progress: Int) {

        value = progress

        sb.progress = progress - min

        if (progressResId == 0) {
            tvValue.text = "$progress"
        } else {
            tvValue.text = tvValue.context.getString(progressResId, progress)
        }
    }

    fun setEnabled(isEnabled: Boolean) {
        sb.isEnabled = isEnabled
    }

}