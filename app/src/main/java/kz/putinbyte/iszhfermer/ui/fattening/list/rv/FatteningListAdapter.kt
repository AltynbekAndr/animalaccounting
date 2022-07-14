package kz.putinbyte.iszhfermer.ui.fattening.list.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iszhfermer.R
import kotlinx.android.synthetic.main.item_fattening.view.*
import kz.putinbyte.iszhfermer.entities.animals.fattening.FatteningList
import kz.putinbyte.iszhfermer.utils.MyUtils

open class FatteningListAdapter(
    private val onItemClick: ((items: List<FatteningList>?, position: Int, isCopy: Boolean) -> Unit)? = null
) : RecyclerView.Adapter<FatteningListHolder>() {

    var items: List<FatteningList>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FatteningListHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_fattening, parent, false)

        return object : FatteningListHolder(v) {
            override fun onClick(v: View?) {
                onItemClick?.invoke(items, adapterPosition, false)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (items != null) items!!.size else 0
    }

    override fun onBindViewHolder(holder: FatteningListHolder, position: Int) {

        (holder).itemView.itemFatteningDropDownText.setTitleText(items!![position].nameRu ?:"_")
        (holder).itemView.itemFatteningDropDownText.setContentText("Дата постановки:\n"
                + (if (items!![position].dateCreate != null) MyUtils.toMyDate(items!![position].dateCreate) else "_")
            .plus("\n\nДата регистрации:\n" + (MyUtils.toMyDate(items!![position].startDate ?:"_"))
            .plus("\n\nМасса животного (кг):\n" + (items!![position].animalWeight ?: "_"))
            .plus("\n\nМасса животного при снятии:\n" + (items!![position].animalWeightWhenRemove ?: "_"))
            .plus("\n\nУчетный номер площадки:\n" + (items!![position].tubeNumber ?: "_"))
            .plus("\n\nДата снятия откорма:\n" + (if (items!![position].endDate != null) MyUtils.toMyDate(items!![position].endDate) else "_"))
            .plus("\n\nПричина снятия:\n" + ( if (items!![position].isFattening) "В откорме" else items!![position].inFattening ?: "_"))
        ))
    }
}