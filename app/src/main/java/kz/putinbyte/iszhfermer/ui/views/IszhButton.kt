package kz.putinbyte.iszhfermer.ui.views

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import iszhfermer.R
import kotlinx.android.synthetic.main.item_iszh_button.view.*

class IszhButton(
    context: Context, attributeSet: AttributeSet
) : ConstraintLayout(
    context, attributeSet
) {

    var titleText: String? = null
        set(value) {
            field = value
            invalidate()
        }

    var topText: String? = null
        set(value) {
            field = value
            invalidate()
        }

    var leftText: String? = null
        set(value) {
            field = value
            invalidate()
        }

    var rightText: String? = null
        set(value) {
            field = value
            invalidate()
        }

    lateinit var inflater: LayoutInflater

    init {
        initView(context, attributeSet)
    }

    private fun initView(context: Context, attributeSet: AttributeSet) {
        val a: TypedArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.IszhButtonOptions, 0, 0)

        titleText = a.getString(R.styleable.IszhButtonOptions_textTitleBtn)
        topText = a.getString(R.styleable.IszhButtonOptions_textTopBtn)
        leftText = a.getString(R.styleable.IszhButtonOptions_textLeftBtn)
        rightText = a.getString(R.styleable.IszhButtonOptions_textRightBtn)

        a.recycle()

        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.addView(inflater.inflate(R.layout.item_iszh_button, null))
        this.isClickable = true
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        itemIszhButtonTitleText.text = (titleText)
        itemIszhButtonTopText.text = (topText)
        itemIszhButtonLeftText.text = (leftText)
        itemIszhButtonRightText.text = (rightText)

        itemIszhButtonTopText.visibility = if (topText == null) GONE else VISIBLE
        itemIszhButtonLeftText.visibility = if (leftText == null) GONE else VISIBLE
        itemIszhButtonRightText.visibility = if (rightText == null) GONE else VISIBLE
    }
}