package kz.putinbyte.iszhfermer.ui.issuedInj.list.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.v12ten.mvp_rentcycle.ui.trips.rv.RegOperatorListHolder
import iszhfermer.R
import kotlinx.android.synthetic.main.item_issued_inj.view.*
import kz.putinbyte.iszhfermer.entities.animals.replaceinj.ReplaceInjList

open class IssuedInjListListAdapter(
    private val onItemClick: ((items: List<ReplaceInjList>?, position: Int) -> Unit)? = null
) : RecyclerView.Adapter<RegOperatorListHolder>() {

    var items: List<ReplaceInjList>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegOperatorListHolder {
        val v = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_issued_inj, parent, false)

        return object : RegOperatorListHolder(v) {
            override fun onClick(v: View?) {
                onItemClick?.invoke(items, adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (items != null) items!!.size else 0
    }

    override fun onBindViewHolder(holder: RegOperatorListHolder, position: Int) {

        (holder).itemView.itemIssuedInjDropDownText.setTitleText(
            "Устаревший ИНЖ - " + items!![position].oldInj)
        (holder).itemView.itemIssuedInjDropDownText.setContentText(
            "Владелец:\n"
                    + items!![position].user.plus(
                "\n\nДата и время замены:\n"
                        + items!![position].dateIssue?.plus(
                    "\n\nПримечание:\n" + items!![position].note
                )
            )
        )
    }
}