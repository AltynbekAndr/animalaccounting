package kz.putinbyte.iszhfermer.ui.diagnostic.success.rv

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iszhfermer.R
import kotlinx.android.synthetic.main.item_success.view.*
import kz.putinbyte.iszhfermer.entities.animals.success.SuccessList
import kz.putinbyte.iszhfermer.ui.deposit.list.rv.DepositTest
import kz.putinbyte.iszhfermer.utils.MyUtils

open class SuccessListAdapter(
    val context: Context,
    private val onItemClick: ((items: List<SuccessList>?, position: Int) -> Unit)? = null
) : RecyclerView.Adapter<SuccessListHolder>() {

    var items: List<SuccessList>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuccessListHolder {
        val v = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_success, parent, false)

        return object : SuccessListHolder(v) {
            override fun onClick(v: View?) {
                onItemClick?.invoke(items, adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (items != null) items!!.size else 0
    }

    override fun onBindViewHolder(holder: SuccessListHolder, position: Int) {

        (holder).itemView.itemSuccessDropDownText.setTitleText(items!![position].organizationNameRu ?: "_")
        (holder).itemView.itemSuccessDropDownText.setContentText("Дата и время акта экспертизы:\n"
                + MyUtils.toMyDate(items!![position].dateCreate ?: "_")
            .plus("\n\nФИО лаборанта РВЛ:\n" + (items!![position].laborantName  ?: "_"))
            .plus("\n\nФИО приёмщика РВЛ:\n" + (items!![position].receiverName ?: "_"))
            .plus("\n\nНомер акта экспертизы:\n" + (items!![position].actNumber ?: "_"))
            .plus("\n\nНомер пробы:\n" + (items!![position].tubeNumber ?: "_"))
            .plus("\n\nОбщая диагностическая оценка:\n" + (items!![position].resultNameRu ?: "_"))
            .plus("\n\nВид болезни:\n" + (items!![position].sickness?.nameRu ?: "_"))
            .plus("\n\nВид исследования:\n" + (items!![position].researchKind?.nameRu ?: "_"))
        )
    }
}