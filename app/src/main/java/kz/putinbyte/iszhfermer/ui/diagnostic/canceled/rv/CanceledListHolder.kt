package kz.putinbyte.iszhfermer.ui.diagnostic.canceled.rv

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class CanceledListHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        //for override
    }
}