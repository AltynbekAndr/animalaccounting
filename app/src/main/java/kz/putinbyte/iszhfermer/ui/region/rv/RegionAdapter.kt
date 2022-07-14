package kz.putinbyte.iszhfermer.ui.region.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iszhfermer.R
import kotlinx.android.synthetic.main.item_owners.view.*
import kotlinx.android.synthetic.main.item_regions.view.*
import kz.putinbyte.iszhfermer.entities.animals.KatoResponse

open class RegionAdapter(
    private val onItemClick: ((items: List<KatoResponse.AnimalAmountByKato>?, position: Int) -> Unit)? = null,
    private val onAddItemClick: ((items: List<KatoResponse.AnimalAmountByKato>?, position: Int) -> Unit)? = null
) : RecyclerView.Adapter<RegionHolder>() {

    var items: List<KatoResponse.AnimalAmountByKato>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_regions, parent, false)

        return object : RegionHolder(v) {
            override fun onClick(v: View?) {}
        }
    }

    override fun getItemCount(): Int {
        return if (items != null) items!!.size else 0
    }

    override fun onBindViewHolder(holder: RegionHolder, position: Int) {

        holder.itemView.region_add_animal_button.visibility = View.VISIBLE
        holder.itemView.region_owners_button.visibility = View.VISIBLE
        holder.itemView.region_next_level.visibility = View.GONE

        (holder).itemView.owners_name.text = items!![position].name
        (holder).itemView.region_krs_value.text = items!![position].cattleCount.toString()
        (holder).itemView.region_camels_value.text = items!![position].camelsCount.toString()
        (holder).itemView.region_one_hoofed_value.text = items!![position].hoofedsCount.toString()
        (holder).itemView.region_horses_value.text = items!![position].horsesCount.toString()
        (holder).itemView.region_mrs_value.text = items!![position].smallCattlesCount.toString()
        (holder).itemView.region_pork_value.text = items!![position].pigsCount.toString()

        (holder).itemView.region_add_animal_button.setOnClickListener {
            onAddItemClick?.invoke(items, position)
        }

        (holder).itemView.region_owners_button.setOnClickListener {
            onItemClick?.invoke(items, position)
        }
    }
}