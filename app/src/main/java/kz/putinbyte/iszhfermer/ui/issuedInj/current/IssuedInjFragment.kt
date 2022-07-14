package kz.putinbyte.iszhfermer.ui.issuedInj.current

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import iszhfermer.R
import kotlinx.android.synthetic.main.fragment_issued_inj.*
import kotlinx.android.synthetic.main.item_spinner.view.*
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.FieldData
import kz.putinbyte.iszhfermer.entities.animals.replaceinj.ReplaceInj
import kz.putinbyte.iszhfermer.model.data.enums.FieldType
import kz.putinbyte.iszhfermer.presentation.issuedInj.current.IssuedInjPresenter
import kz.putinbyte.iszhfermer.presentation.issuedInj.current.IssuedInjView
import kz.putinbyte.iszhfermer.presentation.main.MainView
import kz.putinbyte.iszhfermer.ui.base.BaseFragment
import kz.putinbyte.iszhfermer.utils.LogUtils
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import toothpick.Toothpick
import java.text.SimpleDateFormat
import java.util.*

class IssuedInjFragment : BaseFragment(), IssuedInjView {

    override val layoutResId = R.layout.fragment_issued_inj

    companion object {

        fun newInstance() = IssuedInjFragment()

        private const val CURRENT_INJ_KEY = "animalId"

        fun newInstance(animalId: Int) = IssuedInjFragment().apply {
            arguments = Bundle().apply {
                putInt(CURRENT_INJ_KEY, animalId)
            }
        }
    }

    @ProvidePresenter
    fun providePresenter(): IssuedInjPresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(IssuedInjPresenter::class.java)
            .apply {
                animalId = arguments?.getInt(CURRENT_INJ_KEY)
            }
    }

    private lateinit var fields : List<FieldData>

    @InjectPresenter
    lateinit var presenter: IssuedInjPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainView).setTitle(getString(R.string.title_issuedInj))
        setListeners()

        fields = listOf(
            FieldData("Identification", issuedTypeSpinner, FieldType.SPINNER),
            FieldData("NewInj", issuedNewInjEdit, FieldType.INPUT)
        )
        getCurrentTime()
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentTime(){
        val calendar = Calendar.getInstance()
        val frontFormat = SimpleDateFormat(getString(R.string.app_front_date))
        presenter.replaceInj.dateIssue = frontFormat.format(calendar.time)
    }
    private fun setListeners() {

        val builder = setAsteriskMark()
        asteriskNewInj.text = builder
        asteriskIdenTypeSpinner.text = builder

        issuedScanButton.setOnClickListener {
            presenter.onScanClicked()
        }

        issuedSaveButton.setOnClickListener {
            presenter.onSaveClicked()
        }

        issuedTypeSpinner.itemSpinnerSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.replaceInj.identId = issuedTypeSpinner.items[position].id!!

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        issuedNewInjEdit.filters = arrayOf<InputFilter>(InputFilter.AllCaps())

        issuedNewInjEdit.afterTextChanged{
           presenter.setInj(it.uppercase().filter {  s -> !s.isWhitespace() })
        }
        issuedCommentEdit.afterTextChanged { presenter.replaceInj.note = it }

        issuedImportSwitch.setOnCheckedChangeListener{_,isChecked->
            presenter.replaceInj.import = isChecked
        }

    }

    override fun setBaseInjData(inj: String?) {
        issuedNewInjEdit.setText(inj)
    }

    private fun setAsteriskMark(): SpannableStringBuilder {
        val colored = "*"
        val builder = SpannableStringBuilder()
        builder.append()
        val start = builder.length
        builder.append(colored)
        val end = builder.length
        builder.setSpan(
            ForegroundColorSpan(Color.RED), start, end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return builder
    }

    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }

    override fun showLoader(show: Boolean) {
        if (show)
            alert.show()
        else
            alert.hide()
    }

    override fun showMessage(msg: String) {
        try {
            Snackbar.make(
                injFragment,
                msg,
                Snackbar.LENGTH_LONG
            )
                .show()
        }catch (e: Exception){
            LogUtils.error(javaClass.simpleName,e.message)
        }


    }

    override fun showTypeIdentification(items: List<BaseFormat>) {
        val newList = arrayListOf<BaseFormat>()
        newList.add(BaseFormat(0, null, "Выберите тип идентификации", null))
        newList.addAll(items)
        issuedTypeSpinner.items = newList
    }

    override fun showValidateError(error: Boolean, fieldKey: String) {
        if (error) {
            presenter.validateError = true
            baseValidateError(fields,fieldKey)
        }
    }

    override fun resetError(){
        presenter.validateError = false
        baseResetError(fields)
    }

    override fun showData(replaceInj: ReplaceInj) {
        issuedNewInjEdit.setText(replaceInj.inj)
    }
}