package kz.putinbyte.iszhfermer.ui.offline.list.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iszhfermer.R
import kotlinx.android.synthetic.main.item_offline_owners.view.*
import kz.putinbyte.iszhfermer.entities.db.rvl.Owners

open class OfflineOwnersListAdapter(
    private val onItemClick: ((items: List<Owners>?, position: Int) -> Unit)? = null,
    private val onItemRemoveClick: ((items: List<Owners>?, position: Int) -> Unit)? = null,
) : RecyclerView.Adapter<OfflineOwnersListHolder>() {

    var items: List<Owners>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfflineOwnersListHolder {
        val v = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_offline_owners, parent, false)

        return object : OfflineOwnersListHolder(v) {
            override fun onClick(v: View?) {
                onItemClick?.invoke(items, adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (items != null) items!!.size else 0
    }

    override fun onBindViewHolder(holder: OfflineOwnersListHolder, position: Int) {
        (holder).itemView.offlineOwnersFullNameText.text = (items!![position].fullName ?: "_")
        holder.itemView.offlineOwnersRemoveButton.setOnClickListener {
            onItemRemoveClick?.invoke(items,position)
        }
    }
}