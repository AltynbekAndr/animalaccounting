package kz.putinbyte.iszhfermer.ui.rvl.create.rv.instruments

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import iszhfermer.R
import kotlinx.android.synthetic.main.item_instruments_list.view.*
import kz.putinbyte.iszhfermer.ui.base.adapter.GenericRecyclerAdapter
import kz.putinbyte.iszhfermer.ui.base.adapter.ViewHolder
import kz.putinbyte.iszhfermer.entities.animals.ModelDisease
import kz.putinbyte.iszhfermer.extensions.afterTextChanged

class InstrumentsListAdapter(var listener: Listener, var list: ArrayList<ModelDisease> = arrayListOf()) :
    GenericRecyclerAdapter<ModelDisease>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_instruments_list)
    }

    override fun bind(item: ModelDisease, holder: ViewHolder) = with(holder.itemView) {
        instrumentsTextView.text = (item.item.nameRu ?: "").plus(" (${item.item.code})" ?: "")
        instrumentsImageView.isVisible = item.value
        instrumentsText.isVisible = item.value

        instrumentsText.afterTextChanged { item.item.nameKz = it }


        instrumentsTextView.setOnClickListener {

            if (instrumentsImageView.isVisible) {
                deleteUpdater(item.position, ModelDisease(item.position, item.item, false))
                instrumentsImageView.isVisible = false
                instrumentsText.isVisible = false
                listener.setOnDeleteClickListener(item)
            } else {
                deleteUpdater(item.position, ModelDisease(item.position, item.item, true))
                instrumentsImageView.isVisible = true
                instrumentsText.isVisible = true
                listener.setOnAddClickListener(item)
            }
        }
    }

    interface Listener {
        fun setOnAddClickListener(item: ModelDisease)
        fun setOnDeleteClickListener(item: ModelDisease)
    }
}