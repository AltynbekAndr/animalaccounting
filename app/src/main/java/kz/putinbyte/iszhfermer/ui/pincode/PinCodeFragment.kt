package kz.putinbyte.iszhfermer.ui.pincode

import android.os.Bundle
import android.view.View
import android.widget.Toast
import iszhfermer.R
import kotlinx.android.synthetic.main.pincode_dialog.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.putinbyte.iszhfermer.di.Scopes
import kz.putinbyte.iszhfermer.ui.add.baseBottomSheet.BaseBottomSheetFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import toothpick.Toothpick
import kz.putinbyte.iszhfermer.presentation.pincode.PinCodePresenter
import kz.putinbyte.iszhfermer.presentation.pincode.PinCodeView

class PinCodeFragment : BaseBottomSheetFragment(),
    PinCodeView {

    override val layoutRes = R.layout.pincode_dialog

    @InjectPresenter
    lateinit var presenter: PinCodePresenter

    @ProvidePresenter
    fun providePresenter(): PinCodePresenter {
        return Toothpick.openScope(Scopes.DATA_SCOPE)
            .getInstance(PinCodePresenter::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme;
    }

    override fun showCheckDialog() {
        pin_code_title.text = getString(R.string.pin_code_fragment_title_check)
        pin_code_input.visibility = View.VISIBLE
        repeat_pin_code_input.visibility = View.GONE
        pin_code_button.text = getString(R.string.pin_code_fragment_button_check)
    }

    override fun showCreateDialog() {
        pin_code_title.text = getString(R.string.pin_code_fragment_title_save)
        pin_code_input.visibility = View.VISIBLE
        repeat_pin_code_input.visibility = View.VISIBLE
        pin_code_button.text = getString(R.string.pin_code_fragment_button_save)
    }

    override fun checkPinCode(pinCode: String) {

        var tryCount = 0
        pin_code_button.setOnClickListener {
            val iPinCode = pin_code_input.text.toString()
            kz.putinbyte.iszhfermer.utils.LogUtils.error(
                javaClass.simpleName,
                "$pinCode ______ $iPinCode"
            )
            when {
                iPinCode.length != 4 -> showMessage(getString(R.string.pin_code_fragment_not_full))
                iPinCode != pinCode -> {
                    tryCount++
                    if (tryCount < 5) {
                        pin_validation_message.visibility = View.VISIBLE
                        pin_code_input.text = null
                        pin_validation_message.text =
                            getString(R.string.pin_code_fragment_count_try).replace(
                                "%s",
                                (5 - tryCount).toString()
                            )
                        showMessage(getString(R.string.pin_code_fragment_not_correct))
                        pin_code_input.setText("")
                    } else {
                        showMessage(getString(R.string.pin_code_fragment_try_count_exhausted))
                        presenter.cleanAuthData()
                        dismiss()
                    }
                }
                else -> {
                    presenter.screenMain()
                    dismiss()
                    pin_code_button.isClickable = false
                }
            }
        }
    }

    override fun showMessage(msg: String) {
        GlobalScope.launch {
            try {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        msg,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }catch (e: Exception){
                kz.putinbyte.iszhfermer.utils.LogUtils.error(javaClass.simpleName, e.message)
            }
        }
    }

    override fun savePinCode() {
        pin_code_button.setOnClickListener {
            val pinCode = pin_code_input.text.toString()
            val repeatPinCode = repeat_pin_code_input.text.toString()
            when {
                pinCode.length + repeatPinCode.length != 8 -> showMessage(getString(R.string.pin_code_fragment_not_full))
                pinCode != repeatPinCode -> showMessage(getString(R.string.pin_code_fragment_not_equals))
                else -> {
                    presenter.savePinCode(pinCode)
                    dismiss()
                    pin_code_button.isClickable = false
                }
            }
        }
    }

}