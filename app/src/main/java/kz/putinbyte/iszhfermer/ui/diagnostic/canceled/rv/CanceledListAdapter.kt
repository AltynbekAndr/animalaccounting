package kz.putinbyte.iszhfermer.ui.diagnostic.canceled.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iszhfermer.R
import kotlinx.android.synthetic.main.item_canceled.view.*
import kz.putinbyte.iszhfermer.entities.animals.success.SuccessList

open class CanceledListAdapter(
    private val onItemClick: ((items: List<SuccessList>?, position: Int) -> Unit)? = null
) : RecyclerView.Adapter<CanceledListHolder>() {

    var items: List<SuccessList>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CanceledListHolder {
        val v = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_canceled, parent, false)

        return object : CanceledListHolder(v) {
            override fun onClick(v: View?) {
                onItemClick?.invoke(items, adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (items != null) items!!.size else 0
    }

    override fun onBindViewHolder(holder: CanceledListHolder, position: Int) {

        (holder).itemView.itemCanceledDropDownText.setTitleText(items!![position].organizationNameRu ?: "_")
        (holder).itemView.itemCanceledDropDownText.setContentText("Несоответствие:\n"
                + (items!![position].actNumber ?: "_")
            .plus("\n\nФИО приёмщика РВЛ:\n" + (items!![position].receiverName ?: "_"))
            .plus("\n\nНомер акта экспертизы:\n" + (items!![position].actNumber ?: "_"))
            .plus("\n\nНомер пробы:\n" + (items!![position].tubeNumber ?: "_"))
            .plus("\n\nОбщая диагностическая оценка:\n" + (items!![position].resultNameRu ?: "_"))
            .plus("\n\nВид болезни:\n" + (items!![position].sickness?.nameRu ?: "_"))
            .plus("\n\nВид исследования:\n" + (items!![position].researchKind?.nameRu ?: "_"))
        )
    }
}