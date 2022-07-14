package kz.putinbyte.iszhfermer.ui.base

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import iszhfermer.R
import kotlinx.android.synthetic.main.item_date.view.*
import kotlinx.android.synthetic.main.item_spinner.view.*
import kz.putinbyte.iszhfermer.entities.FieldData
import kz.putinbyte.iszhfermer.model.data.enums.FieldType
import moxy.MvpAppCompatFragment
import kz.putinbyte.iszhfermer.presentation.base.BaseView
import kz.putinbyte.iszhfermer.utils.LoadingAlert

abstract class BaseFragment : MvpAppCompatFragment(), BaseView {

    lateinit var alert: LoadingAlert

    abstract val layoutResId: Int

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(layoutResId, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alert = LoadingAlert(requireActivity())
    }

    private var permissionsForRequest: Pair<String, Pair<() -> Unit, () -> Unit>>? = null

    fun withPermission(permission: String, granted: () -> Unit, denied: () -> Unit) {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            granted()
        } else {
            permissionsForRequest = Pair(permission, Pair(granted, denied))
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(permission),
                BaseActivity.REQUEST_CODE_PERMISSIONS
            )
        }
    }

    fun baseValidateError(fields: List<FieldData>, fieldKey: String) {
        fields.firstOrNull { it.fieldName == fieldKey }?.let {
            when(it.fieldType){
                FieldType.INPUT -> it.fieldId.background =  ContextCompat.getDrawable(requireContext(), R.drawable.bg_edit_validate)
                FieldType.DATE -> it.fieldId.itemDateButton?.let { dateField -> dateField.background = ContextCompat.getDrawable(requireContext(), R.drawable.btn_danger) }
                FieldType.FILE -> it.fieldId.background = ContextCompat.getDrawable(requireContext(), R.drawable.btn_danger)
                FieldType.SPINNER -> it.fieldId.itemSpinnerSpinner?.let { spinField -> spinField.background = ContextCompat.getDrawable(requireContext(), R.drawable.spinner_bg_error) }
            }
        }
    }

    fun baseResetError(fields: List<FieldData>){
        fields.map {
            when(it.fieldType){
                FieldType.INPUT -> it.fieldId.background =  ContextCompat.getDrawable(requireContext(), R.drawable.edt_bg)
                FieldType.DATE -> it.fieldId.itemDateButton?.let { dateField -> dateField.background = ContextCompat.getDrawable(requireContext(), R.drawable.btn_primary) }
                FieldType.FILE -> it.fieldId.background = ContextCompat.getDrawable(requireContext(), R.drawable.btn_primary)
                FieldType.SPINNER -> it.fieldId.itemSpinnerSpinner?.let { spinField -> spinField.background = ContextCompat.getDrawable(requireContext(), R.drawable.spinner_bg) }
            }
        }
    }
    
    override fun showMessage(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
    }
}