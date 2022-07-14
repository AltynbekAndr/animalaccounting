package kz.putinbyte.iszhfermer.ui.add.owners.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iszhfermer.R
import kotlinx.android.synthetic.main.item_owners_recycler.view.*
import kz.putinbyte.iszhfermer.entities.db.rvl.Owners

open class OwnersAdapter(
    private val onItemClick: ((items: List<Owners>, position: Int) -> Unit)? = null
) : RecyclerView.Adapter<OwnersListHolder>() {

    var items: List<Owners>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OwnersListHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_owners_recycler, parent, false)

        return object : OwnersListHolder(view) {
            override fun onClick(v: View?) {
                onItemClick?.invoke(items!!, adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (items != null) items!!.size else 0
    }

    override fun onBindViewHolder(holder: OwnersListHolder, position: Int) {

        holder.itemView.ownersItemFio.text = if (items!![position].fullName == null) items!![position].lastName
            .plus(" " + items!![position].firstName + " " + items!![position].middleName) else items!![position].fullName

        holder.itemView.ownersItemIin.text = items!![position].iin

    }

    fun filter(sequence: CharSequence? = null, list: ArrayList<Owners>? = null): ArrayList<Owners> {
        val temp = ArrayList<Owners>()
        if (list != null) {
            for (s in list) {
                if (s.firstName.lowercase().contains(sequence!!)
                    || s.middleName.lowercase().contains(sequence)
                    || s.lastName.lowercase().contains(sequence)
                ) {
                    temp.add(s)
                }
                if (s.iin?.lowercase()?.contains(sequence)!!) {
                    temp.add(s)
                }
            }
        }
        items = temp
        return temp
    }
}