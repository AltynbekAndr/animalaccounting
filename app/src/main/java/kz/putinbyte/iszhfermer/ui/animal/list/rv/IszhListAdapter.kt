package kz.putinbyte.iszhfermer.ui.animal.list.rv

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getDrawable
import androidx.recyclerview.widget.RecyclerView
import iszhfermer.R
import kotlinx.android.synthetic.main.item_iszh.view.*
import kz.putinbyte.iszhfermer.entities.animals.AnimalList
import kz.putinbyte.iszhfermer.utils.MyUtils

open class IszhListAdapter(
    private val onItemClick: ((items: List<AnimalList.Animals>, position: Int) -> Unit)? = null,
    private val onItemLongClick: ((items: List<AnimalList.Animals>, position: Int, d: Boolean) -> Unit)? = null,
    private val isAlwaysSelectable: Boolean,
    private val context: Context
) : RecyclerView.Adapter<IszhListHolder>() {

    private var isSelectableMode = isAlwaysSelectable
    private val selectedItemPositions = mutableSetOf<Int>()

    var items: List<AnimalList.Animals>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IszhListHolder {
        val v = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_iszh, parent, false)

        return object : IszhListHolder(v) {
            override fun onClick(v: View?) {

            }
        }
    }

    override fun getItemCount(): Int {
        return if (items != null) items!!.size else 0
    }

    override fun onBindViewHolder(holder: IszhListHolder, position: Int) {
        val item = items!![position]

        (holder).itemView.iszhInjText.text =
            item.animalKind.plus(" - " + "ИНЖ " + item.inj ?: "")
        (holder).itemView.iszhBreedText.text = item.mast ?: item.comment
        (holder).itemView.iszhGenderText.text = "Пол - ".plus(item.gender)
        val splitDate = MyUtils.toMyBirthDate(item.birthDate!!)
        (holder).itemView.iszhBirthdayText.text =
            "Др - ".plus(splitDate).plus(" / " + item.age)

        // open detail fragment
        holder.itemView.iszhDetailButton.setOnClickListener {
            onItemClick?.invoke(items!!, position)
        }

        //If this item is selected, check it (change background)
        holder.itemView.iszhBackgroundContainer.background = if (isSelectedItem(position))
            getDrawable(context, R.drawable.box_grey_selected) else getDrawable(
            context,
            R.drawable.box_grey_selector
        )
        // hide and show detail button
        holder.itemView.iszhDetailButton.visibility =
            if (isSelectableMode) View.GONE else View.VISIBLE

        // set padding
        val scale: Float = holder.itemView.resources.displayMetrics.density
        val dpAsPixels = (18 * scale + 0.5f).toInt()
        holder.itemView.iszhBackgroundContainer.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels)

        // simple click
        holder.itemView.setOnClickListener {

            //Treat as a normal click when not in selection mode
            if (!isSelectableMode && !isAlwaysSelectable)
//                Toast.makeText(context, "Normal click", Toast.LENGTH_SHORT).show()
            else {
                if (isSelectedItem(position))
                    removeSelectedItem(position)
                else addSelectedItem(position)
                onBindViewHolder(holder, position)
                onItemLongClick?.invoke(items!!, position, isSelectableMode)
            }
        }

        // Long click
        holder.itemView.setOnLongClickListener {
            //Long click to enter selection mode
            if (isSelectedItem(position)) removeSelectedItem(position)
            else addSelectedItem(position)
            onBindViewHolder(holder, position)
            onItemLongClick?.invoke(items!!, position, isSelectableMode)
            return@setOnLongClickListener true
        }
    }

    //Pass the Set in which the Position of the selected item is recorded to the outside
    fun getSelectedItemPositions() = selectedItemPositions.toSet()

    //Check if the item of the specified Position is selected
    private fun isSelectedItem(position: Int): Boolean = (selectedItemPositions.contains(position))

    //Enter selection mode when not in selection mode
    private fun addSelectedItem(position: Int) {
        if (selectedItemPositions.isEmpty() && !isAlwaysSelectable) {
            isSelectableMode = true
            notifyDataSetChanged()
        }
        selectedItemPositions.add(position)
    }

    //If the last one is deselected in selection mode, turn off selection mode
    private fun removeSelectedItem(position: Int) {
        selectedItemPositions.remove(position)
        if (selectedItemPositions.isEmpty() && !isAlwaysSelectable) {
            isSelectableMode = false
            notifyDataSetChanged()
        }
    }
}
