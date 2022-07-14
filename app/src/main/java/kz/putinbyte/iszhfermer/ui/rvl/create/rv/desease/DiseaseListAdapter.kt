package kz.putinbyte.iszhfermer.ui.rvl.create.rv.desease

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import iszhfermer.R
import kotlinx.android.synthetic.main.item_disease_list.view.*
import kz.putinbyte.iszhfermer.ui.base.adapter.GenericRecyclerAdapter
import kz.putinbyte.iszhfermer.ui.base.adapter.ViewHolder
import kz.putinbyte.iszhfermer.entities.animals.ModelDisease

class DiseaseListAdapter(var listener: Listener, var list: ArrayList<ModelDisease> = arrayListOf()) :
    GenericRecyclerAdapter<ModelDisease>(list) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_disease_list)
    }

    override fun bind(item: ModelDisease, holder: ViewHolder) = with(holder.itemView) {
        diseaseTextView.text = item.item.nameRu
        diseaseImageView.isVisible = item.value

        holder.itemView.setOnClickListener {

            if (diseaseImageView.isVisible) {
                deleteUpdater(item.position, ModelDisease(item.position, item.item, false))
                diseaseImageView.isVisible = false
                listener.setOnDeleteClickListener(item)
            } else {
                deleteUpdater(item.position, ModelDisease(item.position, item.item, true))
                diseaseImageView.isVisible = true
                listener.setOnAddClickListener(item)
            }
        }
    }

    interface Listener {
        fun setOnAddClickListener(item: ModelDisease)
        fun setOnDeleteClickListener(item: ModelDisease)
    }
}