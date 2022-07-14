package kz.putinbyte.iszhfermer.ui.search.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iszhfermer.R
import kotlinx.android.synthetic.main.item_search.view.*
import kz.putinbyte.iszhfermer.entities.animals.search.SearchResponse

open class SearchAdapter(
    private val onItemClick: ((items:ArrayList<SearchResponse.Lists>?, position: Int) -> Unit)? = null
) : RecyclerView.Adapter<FiltersHolder>() {

    var items: ArrayList<SearchResponse.Lists>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FiltersHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search, parent, false)

        return object : FiltersHolder(view) {
            override fun onClick(v: View?) {
                onItemClick?.invoke(items, adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun onBindViewHolder(holder: FiltersHolder, position: Int) {
        (holder).itemView.searchInjText.text =
            items!![position].animalKindString
                .plus(" - " + "ИНЖ " + items!![position].inj)
        (holder).itemView.searchBreedText.text =
            items!![position].mastString
        (holder).itemView.searchGenderText.text = "Пол - ".plus(items!![position].genderString)
        val splitDate = items!![position].birthDate.split("T")
        val f = splitDate[0].split("-")
        (holder).itemView.searchBirthdayText.text =
            "Др - ".plus(f[2] + "-" + f[1] + "-" + f[0]).plus("/" + items!![position].age)
        holder.itemView.searchOwnerText.text = items!![position].ownerFullName

    }
}