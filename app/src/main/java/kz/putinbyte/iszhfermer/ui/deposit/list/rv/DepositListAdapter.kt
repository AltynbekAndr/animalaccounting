package kz.putinbyte.iszhfermer.ui.deposit.list.rv

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iszhfermer.R
import kotlinx.android.synthetic.main.item_research.view.*
import kz.putinbyte.iszhfermer.entities.animals.deposit.DepositList
import kz.putinbyte.iszhfermer.utils.MyUtils

open class DepositListAdapter(
    private val onItemClick: ((items: List<DepositList>?, position: Int, isCopy: Boolean) -> Unit)? = null
) : RecyclerView.Adapter<DepositListHolder>() {

    var items: List<DepositList>? = null
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepositListHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_research, parent, false)

        return object : DepositListHolder(v) {
            override fun onClick(v: View?) {
                onItemClick?.invoke(items, adapterPosition, false)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (items != null) items!!.size else 0
    }

    override fun onBindViewHolder(holder: DepositListHolder, position: Int) {

        (holder).itemView.itemResearchDropDownText.setTitleText(MyUtils.toMyDate(items!![position].contractStartDate ?: "_"))
        (holder).itemView.itemResearchDropDownText.setContentText("Залогодатель:\n"
                + (items!![position].pledgeeId ?: 0)+ ("\n\nЗалогодержатель:\n" + (items!![position].pledgerId ?: 0))
            .plus("\n\nРегистрационный номер:\n" + (items!![position].registerNumber ?: 0))
            .plus("\n\nНомер договора:\n" + (items!![position].contractNumber ?: 0))
            .plus("\n\nДата заключения договора:\n" + MyUtils.toMyDate(items!![position].contractStartDate ?: "_"))
            .plus("\n\nДата прекращения договора:\n" + MyUtils.toMyDate(items!![position].contractEndDate ?: "_"))
            .plus("\n\nСумма займа (тг):\n" + (items!![position].pledgeSum ?: 0))
            .plus("\n\nПримечание:\n" + (items!![position].note ?: "_"))
        )
    }
}