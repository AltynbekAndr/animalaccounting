package kz.putinbyte.iszhfermer.ui.nameRegions.rv

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iszhfermer.R
import kotlinx.android.synthetic.main.item_regions.view.*
import kz.putinbyte.iszhfermer.entities.network.region.Region
import kz.putinbyte.iszhfermer.extensions.visible
import kz.putinbyte.iszhfermer.model.data.enums.ListRegion

open class NameRegionsListAdapter(
    private val onItemClick: ((items: List<Region.AnimalAmountByKato>?, position: Int, buttonType: ListRegion) -> Unit)? = null
) : RecyclerView.Adapter<NameRegionsListHolder>() {

    var disconnect = false
    var items: List<Region.AnimalAmountByKato>? = null
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameRegionsListHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_regions, parent, false)

        return object : NameRegionsListHolder(v) {
            init {
                itemView.region_add_animal_button.setOnClickListener {
                    onItemClick?.invoke(items, adapterPosition, ListRegion.ADD_ANIMAL)
                }
                itemView.region_owners_button.setOnClickListener {
                    onItemClick?.invoke(items, adapterPosition, ListRegion.OWNER_BUTTON)
                }
                itemView.region_next_level.setOnClickListener {
                    onItemClick?.invoke(items, adapterPosition, ListRegion.NEXT_LEVEL)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (items != null) items!!.size else 0
    }

    override fun onBindViewHolder(holder: NameRegionsListHolder, position: Int) {
        val item = items!![position]
        (holder).itemView.region_name.text = item.name.trim()
        (holder).itemView.region_krs_value.text = item.cattleCount.toString()
        (holder).itemView.region_camels_value.text = item.camelsCount.toString()
        (holder).itemView.region_pork_value.text = item.pigsCount.toString()
        (holder).itemView.region_one_hoofed_value.text = item.hoofedsCount.toString()
        (holder).itemView.region_horses_value.text = item.horsesCount.toString()
        (holder).itemView.region_mrs_value.text = item.smallCattlesCount.toString()

        holder.itemView.region_add_animal_button.visible(item.last)
        holder.itemView.region_owners_button.visible(item.last)
        holder.itemView.region_next_level.visible(!item.last)

        holder.itemView.region_owners_button.isEnabled = !disconnect
        holder.itemView.region_next_level.isEnabled = !disconnect
    }
    fun chekConnect(show: Boolean) {
        disconnect = show
        notifyDataSetChanged()
    }
}