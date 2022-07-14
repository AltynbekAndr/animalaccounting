package kz.putinbyte.iszhfermer.ui.offline.detail.rv

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class OfflineOwnersDetailHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        //for override
    }
}