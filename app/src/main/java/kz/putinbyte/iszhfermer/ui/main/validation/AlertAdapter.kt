package kz.putinbyte.iszhfermer.ui.main.validation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iszhfermer.R
import kotlinx.android.synthetic.main.item_alert_recycler.view.*
import kz.putinbyte.iszhfermer.entities.animals.AnimalList
import kz.putinbyte.iszhfermer.ui.owners.rv.OwnersHolder

open class AlertAdapter(
    private val onItemClick: ((items: List<AnimalList.Animals>?, position: Int) -> Unit)? = null
) : RecyclerView.Adapter<OwnersHolder>() {

    var items: List<AnimalList.Animals>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OwnersHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_alert_recycler, parent, false)

        return object : OwnersHolder(v) {
            override fun onClick(v: View?) {
            }
        }
    }

    override fun getItemCount(): Int {
        return if (items != null) items!!.size else 0
    }

    override fun onBindViewHolder(holder: OwnersHolder, position: Int) {

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(items,position)
        }

        holder.itemView.alertItemInj.text = items!![position].inj ?: "_"
        holder.itemView.alertItemMessage.text = items!![position].comment ?: "_"
    }
}