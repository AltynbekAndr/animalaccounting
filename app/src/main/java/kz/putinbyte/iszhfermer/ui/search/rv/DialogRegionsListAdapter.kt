package kz.putinbyte.iszhfermer.ui.search.rv

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import iszhfermer.R
import kotlinx.android.synthetic.main.item_custom_regions.view.*
import kotlinx.android.synthetic.main.item_custom_regions.view.regionsMainTitle
import kz.putinbyte.iszhfermer.entities.network.region.Region
import kz.putinbyte.iszhfermer.model.data.enums.SearchListRegion

open class DialogRegionsListAdapter(
    private val onItemClick: ((items: List<Region.AnimalAmountByKato>, position: Int, buttonType: SearchListRegion) -> Unit)? = null,
) : RecyclerView.Adapter<DialogRegionsListHolder>() {

    var items: List<Region.AnimalAmountByKato>? = null
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogRegionsListHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.item_custom_regions, parent, false)

        return object : DialogRegionsListHolder(v) {
            init {
                itemView.regionsSelectAndNextButton.setOnClickListener {
                    if (itemView.regionsSelectAndNextButton.text != itemView.context.getText(R.string.icon_scanned)) {
                        onItemClick?.invoke(items!!, adapterPosition, SearchListRegion.NEXT)
                    } else {
                        onItemClick?.invoke(items!!, adapterPosition, SearchListRegion.CHOOSE)
                    }
                }
                itemView.regionsSelectButton.setOnClickListener {
                    onItemClick?.invoke(items!!, adapterPosition, SearchListRegion.CHOOSE)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (items != null) items!!.size else 0
    }

    override fun onBindViewHolder(holder: DialogRegionsListHolder, position: Int) {
        (holder).itemView.regionsMainTitle.text = items!![position].name.trim()

        if (items!![position].last) {
            holder.itemView.regionsSelectAndNextButton.background =
                ContextCompat.getDrawable(holder.itemView.context, R.drawable.btn_green)
            holder.itemView.regionsSelectAndNextButton.text =
                holder.itemView.context.getText(R.string.icon_scanned)
            holder.itemView.regionsSelectButton.visibility = View.GONE
        } else {
            holder.itemView.regionsSelectAndNextButton.background =
                ContextCompat.getDrawable(holder.itemView.context, R.drawable.btn_primary)
            holder.itemView.regionsSelectAndNextButton.text =
                holder.itemView.context.getText(R.string.icon_angle_right)
            holder.itemView.regionsSelectButton.visibility = View.VISIBLE
        }
    }
}