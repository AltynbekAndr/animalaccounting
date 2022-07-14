package kz.putinbyte.iszhfermer.ui.views

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import iszhfermer.R
import kotlinx.android.synthetic.main.item_spinner.view.*
import kz.putinbyte.iszhfermer.entities.BaseFormat

class IsjSpinner(
    context: Context, attributeSet: AttributeSet
) : ConstraintLayout(
    context, attributeSet
) {

    var titleText: String? = null
        set(value) {
            field = value
            invalidate()
        }

    var items: List<BaseFormat> = ArrayList()
        set(value) {
            field = value

            val newLabels = ArrayList<String>()
            for (item in items) {
                newLabels.add(item.nameRu!!)
            }

            arrayAdapter =
                ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, newLabels)
            itemSpinnerSpinner.adapter = arrayAdapter

            invalidate()
        }

    var labels: List<String> = ArrayList()

    var arrayAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, labels)

    init {
        val a: TypedArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.IszhSpinnerOptions, 0, 0)

        titleText = a.getString(R.styleable.IszhSpinnerOptions_textTitleSpinner)

        a.recycle()

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.addView(inflater.inflate(R.layout.item_spinner, null))

        itemSpinnerSpinner.adapter = arrayAdapter

        itemSpinnerText.setText(titleText)
    }

    fun setSelection(state: String?) {
        if (state != null)
            for ((index, item) in items.withIndex()) {
                if (item.nameRu.equals(state)) {
                    itemSpinnerSpinner.setSelection(index, false)
                }
            }
    }
}