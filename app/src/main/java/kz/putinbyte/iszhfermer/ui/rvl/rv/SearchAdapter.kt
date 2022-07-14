package kz.putinbyte.iszhfermer.ui.rvl.rv

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import iszhfermer.R
import kotlinx.android.synthetic.main.item_rvl.view.*
import kotlinx.android.synthetic.main.item_search_recycler.view.*
import kz.putinbyte.iszhfermer.entities.animals.ListModel
import kz.putinbyte.iszhfermer.ui.base.adapter.GenericRecyclerAdapter
import kz.putinbyte.iszhfermer.ui.base.adapter.ViewHolder
import kz.putinbyte.iszhfermer.utils.MyUtils
import kz.putinbyte.iszhfermer.utils.MyUtils.toServerDateTime

class SearchAdapter(var listener: Listener, item: ArrayList<ListModel> = arrayListOf()): GenericRecyclerAdapter<ListModel>(item) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_search_recycler)
    }

    override fun bind(item: ListModel, holder: ViewHolder) = with(holder.itemView) {
        searchTextView.setTitleText("ИНЖ - "+items[holder.adapterPosition].inj)

        searchCheckBox.isVisible = MyUtils.currentDate.toInt() - MyUtils.toMyDateTime(item.birthDate.toString())
            .toInt() > 360

        val name = if (items[holder.adapterPosition].genderId == 1){
            "Мужской"
        }else{
            "Женский"
        }
        searchTextView.setContentText(
            "Владелец:\n" + items[holder.adapterPosition].fullName!!
                .plus("\n\nПол:\n$name")
                .plus("\n\nВид:\n"+items[holder.adapterPosition].animalKind!!.nameRu)
                .plus("\n\nДата рождения\n" + toServerDateTime(items[holder.adapterPosition].birthDate.toString())))

        searchCheckBox.setOnCheckedChangeListener { _, b ->
            listener.setOnClickListener(b, item)
        }
    }

    interface Listener{
        fun setOnClickListener(boolean: Boolean, item: ListModel)
    }
}