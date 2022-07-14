package kz.putinbyte.iszhfermer.ui.rvl.rv

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iszhfermer.R
import kotlinx.android.synthetic.main.item_search_save_recycler.view.*
import kz.putinbyte.iszhfermer.entities.animals.ListModel
import kz.putinbyte.iszhfermer.ui.base.adapter.GenericRecyclerAdapter
import kz.putinbyte.iszhfermer.ui.base.adapter.ViewHolder

class SaveSearchAdapter(var listener: Listener, var item: ArrayList<ListModel> = arrayListOf()): GenericRecyclerAdapter<ListModel>(item) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_search_save_recycler)
    }

    override fun bind(item: ListModel, holder: ViewHolder) = with(holder.itemView) {
        searchSaveTextView.setTitleText("ИНЖ - "+items[holder.adapterPosition].inj)
        searchSaveCheckBox.isChecked = true
        val name = if (items[holder.adapterPosition].genderId == 1){
            "Мужской"
        }else{
            "Женский"
        }
        searchSaveTextView.setContentText(
            "Владелец:\n" + (items[holder.adapterPosition].fullName ?: "")
                .plus("\n\nПол:\n$name")
                .plus("\n\nВид:\n"+items[holder.adapterPosition].animalKind!!.nameRu)
                .plus("\n\nДата рождения\n" + items[holder.adapterPosition].birthDate))

        searchSaveCheckBox.setOnCheckedChangeListener { _, b ->
            listener.setOnClickListener(b, item)
        }
    }

    interface Listener{
        fun setOnClickListener(boolean: Boolean, item: ListModel)
    }
}