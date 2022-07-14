package kz.putinbyte.iszhfermer.ui.offline.detail.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iszhfermer.R
import kotlinx.android.synthetic.main.item_animal_kindl.view.*
import kz.putinbyte.iszhfermer.entities.BaseFormat

open class OfflineOwnersDetailAdapter(
    private val onItemClick: ((items: List<BaseFormat>?, checked: Boolean,position:Int) -> Unit)? = null,
) : RecyclerView.Adapter<OfflineOwnersDetailHolder>() {

    var items: List<BaseFormat>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfflineOwnersDetailHolder {
        val v = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_animal_kindl, parent, false)

        return object : OfflineOwnersDetailHolder(v) {
            override fun onClick(v: View?) {
//                onItemClick?.invoke(items, adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (items != null) items!!.size else 0
    }

    override fun onBindViewHolder(holder: OfflineOwnersDetailHolder, position: Int) {

        holder.itemView.checkAnimalKindBox.setOnCheckedChangeListener { _, b ->
            onItemClick?.invoke(items,b,position)
        }
        holder.itemView.offlineOwnersAnimalKIndText.text = items!![position].nameRu ?: ""
    }
}