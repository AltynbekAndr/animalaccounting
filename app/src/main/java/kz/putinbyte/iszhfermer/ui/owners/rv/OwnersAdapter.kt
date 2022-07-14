package kz.putinbyte.iszhfermer.ui.owners.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iszhfermer.R
import kotlinx.android.synthetic.main.item_owners.view.*
import kz.putinbyte.iszhfermer.entities.animals.OwnersByKato

open class OwnersAdapter(
    private val onItemClick: ((items: List<OwnersByKato>?, position: Int) -> Unit)? = null
) : RecyclerView.Adapter<OwnersHolder>() {

    var items: List<OwnersByKato>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OwnersHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_owners, parent, false)

        return object : OwnersHolder(v) {
            override fun onClick(v: View?) {
            }
        }
    }

    override fun getItemCount(): Int {
        return if (items != null) items!!.size else 0
    }

    override fun onBindViewHolder(holder: OwnersHolder, position: Int) {

        holder.itemView.owners_animalListButton.setOnClickListener {
            onItemClick?.invoke(items,position)
        }
        holder.itemView.owners_name.text = items!![position].fullNameRu
        holder.itemView.owners_krsText.text = if (items!![position].cattle!! < 0) "0" else items!![position].cattle.toString()
        holder.itemView.owners_camelsText.text = if (items!![position].camels!! < 0) "0" else items!![position].camels.toString()
        holder.itemView.owners_porkText.text = if (items!![position].pigs!! < 0) "0" else items!![position].pigs.toString()
        holder.itemView.owners_oneHoofedText.text = if (items!![position].hoofeds!! < 0) "0" else items!![position].hoofeds.toString()
        holder.itemView.owners_horsesText.text = if (items!![position].horses!! < 0) "0" else items!![position].horses.toString()
        holder.itemView.owners_mrsText.text = if (items!![position].smallCattles!! < 0) "0" else  items!![position].smallCattles.toString()
    }
}