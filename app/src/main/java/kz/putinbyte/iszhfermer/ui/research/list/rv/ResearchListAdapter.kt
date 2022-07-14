package kz.putinbyte.iszhfermer.ui.research.list.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iszhfermer.R
import kotlinx.android.synthetic.main.item_research.view.*
import kz.putinbyte.iszhfermer.entities.research.AnimalResearchModelItem
import kz.putinbyte.iszhfermer.utils.MyUtils

open class ResearchListAdapter(
    private val onItemClick: ((items: List<AnimalResearchModelItem>?, position: Int) -> Unit)? = null
) : RecyclerView.Adapter<ResearchListHolder>() {

    var items: List<AnimalResearchModelItem>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResearchListHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_research, parent, false)

        return object : ResearchListHolder(v) {
            override fun onClick(v: View?) {
                onItemClick?.invoke(items, adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (items != null) items!!.size else 0
    }

    override fun onBindViewHolder(holder: ResearchListHolder, position: Int) {

        (holder).itemView.itemResearchDropDownText.setTitleText(items!![position].researchKind ?: "_")
        (holder).itemView.itemResearchDropDownText.setContentText("Заболевание:\n"
                + (items!![position].sickness ?: "_")
            .plus("\n\nРезультат:\n" + (items!![position].causeResult ?: "_"))
            .plus("\n\nДата:\n" + (MyUtils.toMyDate(items!![position].researchDate ?: "_")))
            .plus("\n\nВетеринарный врач:\n" + (items!![position].doctor ?: "_"))
            .plus("\n\nПримечание:\n" + (items!![position].note ?: "_"))
        )

    }
}