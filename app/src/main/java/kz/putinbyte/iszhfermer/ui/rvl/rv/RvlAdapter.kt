package kz.putinbyte.iszhfermer.ui.rvl.rv

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import iszhfermer.R
import kotlinx.android.synthetic.main.item_rvl.view.*
import kz.putinbyte.iszhfermer.entities.animals.ListModel
import kz.putinbyte.iszhfermer.ui.base.adapter.GenericRecyclerAdapter
import kz.putinbyte.iszhfermer.ui.base.adapter.ViewHolder
import kz.putinbyte.iszhfermer.utils.MyUtils.currentDate
import kz.putinbyte.iszhfermer.utils.MyUtils.toMyDateTime
import kz.putinbyte.iszhfermer.utils.MyUtils.toServerDateTime

open class RvlAdapter(
    private val onItemClick: ((items: ListModel?, position: Boolean) -> Unit)? = null,
    item: ArrayList<ListModel> = arrayListOf()
): GenericRecyclerAdapter<ListModel>(item) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_rvl)
    }

    override fun bind(item: ListModel, holder: ViewHolder): Unit = with(holder.itemView) {
        first_dropdown_text_view.setTitleText("ИНЖ - "+items[holder.adapterPosition].inj)

        checkRvlBox.isVisible = currentDate.toInt() - toMyDateTime(item.birthDate.toString()).toInt() > 360

        val name = if (items[holder.adapterPosition].genderId == 1){
            "Мужской"
        }else{
            "Женский"
        }

        if (items[holder.adapterPosition].fullName != null){
            first_dropdown_text_view.setContentText(
                "Владелец:\n" + items[holder.adapterPosition].fullName!!
                    .plus("\n\nПол:\n$name")
                    .plus("\n\nВид:\n"+items[holder.adapterPosition].animalKind!!.nameRu)
                    .plus("\n\nДата рождения\n" + toServerDateTime(items[holder.adapterPosition].birthDate.toString())))
        }else{
            first_dropdown_text_view.setContentText(
                "Владелец:\n" + ""
                    .plus("\n\nПол:\n$name")
                    .plus("\n\nВид:\n"+items[holder.adapterPosition].animalKind!!.nameRu)
                    .plus("\n\nДата рождения\n" + toServerDateTime(items[holder.adapterPosition].birthDate.toString())))
        }


        checkRvlBox.setOnCheckedChangeListener { _, b ->
            onItemClick?.invoke(items[holder.adapterPosition], b)
        }
    }

    fun addItem(items: ListModel){
        this.items.add(items)
        notifyItemInserted(itemCount)
    }
}