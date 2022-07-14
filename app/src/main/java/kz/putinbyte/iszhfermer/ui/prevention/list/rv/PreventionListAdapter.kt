package kz.putinbyte.iszhfermer.ui.prevention.list.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iszhfermer.R
import kotlinx.android.synthetic.main.item_prevention.view.*
import kz.putinbyte.iszhfermer.entities.animals.AnimalPreventiveActionModelItem
import kz.putinbyte.iszhfermer.utils.MyUtils

open class PreventionListAdapter(
    private val onItemClick: ((items: List<AnimalPreventiveActionModelItem>?, position: Int) -> Unit)? = null
) : RecyclerView.Adapter<SourceListHolder>() {

    var items: List<AnimalPreventiveActionModelItem>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceListHolder {
        val v = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_prevention, parent, false)

        return object : SourceListHolder(v) {
            override fun onClick(v: View?) {
                onItemClick?.invoke(items, adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (items != null) items!!.size else 0
    }

    override fun onBindViewHolder(holder: SourceListHolder, position: Int) {

        (holder).itemView.itemPreventionDropDownText.setTitleText(items!![position].immunKind ?: "_")
        (holder).itemView.itemPreventionDropDownText.setContentText("Заболевание:\n"
                + (items!![position].sickness ?: "_")
            .plus("\n\nДата иммунизации:\n" + (MyUtils.toMyDate(items!![position].immunizationDate ?: "_")))
            .plus("\n\nВрач:\n" + (items!![position].doctor ?: "_"))
        )
    }
}