package kz.putinbyte.iszhfermer.ui.views

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import iszhfermer.R
import kotlinx.android.synthetic.main.item_date.view.*

class IszhEditDateCurrent(
    context: Context, attributeSet: AttributeSet
) : ConstraintLayout(
    context, attributeSet
) {

    lateinit var inflater: LayoutInflater

    init {
        initView(context, attributeSet)
    }

    private fun initView(context: Context, attributeSet: AttributeSet) {
        val a: TypedArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.IszhEditDateOptions, 0, 0)

        val titleText = a.getString(R.styleable.IszhEditDateOptions_textTitleDateNumber)

        a.recycle()

        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.addView(inflater.inflate(R.layout.item_date_current, null))
        this.isClickable = true

        itemDateCurrentText.setText(titleText)
    }
}