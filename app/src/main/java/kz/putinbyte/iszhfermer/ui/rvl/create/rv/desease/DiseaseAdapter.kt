package kz.putinbyte.iszhfermer.ui.rvl.create.rv.desease

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iszhfermer.R
import kotlinx.android.synthetic.main.item_disease.view.*
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.ui.base.adapter.GenericRecyclerAdapter
import kz.putinbyte.iszhfermer.ui.base.adapter.ViewHolder

class DiseaseAdapter(var listener: Listener, var list: ArrayList<BaseFormat> = arrayListOf()) :
    GenericRecyclerAdapter<BaseFormat>(list) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_disease)
    }

    override fun bind(item: BaseFormat, holder: ViewHolder) = with(holder.itemView) {
        diseaseGeneralTextView.text = item.nameRu
        diseaseGeneralImageView.setOnClickListener {
            deleteItem(item, holder)
            listener.deleteItem(item)
        }
        this.setOnClickListener {
            listener.srtClickItem()
        }
    }

    interface Listener{
        fun deleteItem(item: BaseFormat)
        fun srtClickItem()
    }
}