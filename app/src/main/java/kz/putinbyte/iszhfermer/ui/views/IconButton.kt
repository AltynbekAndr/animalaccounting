package kz.putinbyte.iszhfermer.ui.views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class IconButton(context: Context, attrs: AttributeSet?) : AppCompatButton(context, attrs) {

    init {
        initView()
    }

    private fun initView() {
        val tf = Typeface.createFromAsset(
            context.assets,
            "fa-solid-900.ttf"
        )
        typeface = tf
    }
}