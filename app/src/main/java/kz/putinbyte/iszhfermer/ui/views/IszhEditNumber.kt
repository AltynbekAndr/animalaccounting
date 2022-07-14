package kz.putinbyte.iszhfermer.ui.views

import android.content.Context
import android.content.res.TypedArray
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import iszhfermer.R
import kotlinx.android.synthetic.main.item_number.view.*

class IszhEditNumber(
    context: Context, attributeSet: AttributeSet
) : ConstraintLayout(
    context, attributeSet
) {

    lateinit var inflater: LayoutInflater

    init {
        initView(context, attributeSet)
    }

    var inputTypeEdit: String? = null
        set(value) {
            field = value
            invalidate()
        }

    var titleText: String? = null
        set(value) {
            field = value
            invalidate()
        }

    var editText: String? = null
        set(value) {
            field = value
            invalidate()
        }

    private fun initView(context: Context, attributeSet: AttributeSet) {
        val a: TypedArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.IszhEditNumberOptions, 0, 0)

        titleText = a.getString(R.styleable.IszhEditNumberOptions_textTitleEditNumber)
        inputTypeEdit = a.getString(R.styleable.IszhEditNumberOptions_inputTypeEditNumber)

        a.recycle()

        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.addView(inflater.inflate(R.layout.item_number, null))
        this.isClickable = true


        if (inputTypeEdit.equals("number"))
            itemNumberEdit.inputType = InputType.TYPE_CLASS_NUMBER
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (changed) {
            itemNumberText.text = (titleText)
            itemNumberEdit.setText(editText)
        }
    }
}