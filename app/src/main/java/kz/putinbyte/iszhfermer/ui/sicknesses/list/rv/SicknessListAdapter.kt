package kz.putinbyte.iszhfermer.ui.sicknesses.list.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iszhfermer.R
import kotlinx.android.synthetic.main.item_sickness.view.*
import kz.putinbyte.iszhfermer.entities.animals.AnimalSicknessModelItem
import kz.putinbyte.iszhfermer.utils.MyUtils

open class SicknessListAdapter(
    private val onItemClick: ((items: List<AnimalSicknessModelItem>?, position: Int) -> Unit)? = null
) : RecyclerView.Adapter<OperatorListHolder>() {

    var items: List<AnimalSicknessModelItem>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperatorListHolder {
        val v = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_sickness, parent, false)

        return object : OperatorListHolder(v) {
            override fun onClick(v: View?) {
                onItemClick?.invoke(items, adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (items != null) items!!.size else 0
    }

    override fun onBindViewHolder(holder: OperatorListHolder, position: Int) {
        (holder).itemView.itemSicknessDropDownText.setTitleText(items!![position].initialDiagnosis ?: "_")
        (holder).itemView.itemSicknessDropDownText.setContentText("Заключительный диагноз:\n"
                + (items!![position].finalDiagnosis ?: "_")
            .plus("\n\nДата:\n" + (MyUtils.toMyDate(items!![position].sicknessRegDate ?: "_")))
            .plus( "\n\nВетеринарный врач:\n" + (items!![position].doctor ?: "_"))
            .plus( "\n\nИсход:\n" + (items!![position].cause ?: "_"))
            .plus( "\n\nПримечание:\n" + (items!![position].note ?: "_"))
        )
    }
}