package kz.putinbyte.iszhfermer.ui.history.list.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iszhfermer.R
import kotlinx.android.synthetic.main.item_history.view.*
import kz.putinbyte.iszhfermer.entities.animals.history.HistoryList
import kz.putinbyte.iszhfermer.utils.MyUtils

open class HistoryListAdapter(
    private val onItemClick: ((items: List<HistoryList>?, position: Int) -> Unit)? = null
) : RecyclerView.Adapter<ScheduleDatesHolder>() {

    var items: List<HistoryList>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleDatesHolder {
        val v = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_history, parent, false)

        return object : ScheduleDatesHolder(v) {}
    }

    override fun getItemCount(): Int {
        return if (items != null) items!!.size else 0
    }

    override fun onBindViewHolder(holder: ScheduleDatesHolder, position: Int) {

        holder.itemView.itemHistoryDropDownText.setTitleText(items!![position].owner ?: "_")
        holder.itemView.itemHistoryDropDownText.setContentText(
            "Дата:\n"
                    + (if (items!![position].dateCreate != null) MyUtils.toMyDate(items!![position].dateCreate) else "_")
                .plus("\n\nКато:\n" + (items!![position].kato ?: "_"))
                .plus("\n\nПричина:\n" + (items!![position].causeId ?: "_"))
                .plus("\n\nБолезнь:\n" + (items!![position].sickness ?: "_"))
                .plus("\n\nВетеринар:\n" + (items!![position].veterinarian ?: "_"))
                .plus("\n\nДата постановки:\n" + MyUtils.toMyDate(items!![position].dateCreate ?: "_"))
        )

    }
}