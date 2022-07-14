package kz.putinbyte.iszhfermer.ui.rvl.bottomFragment

import android.os.Bundle
import android.os.Handler
import android.view.View
import iszhfermer.R
import kz.putinbyte.iszhfermer.ui.add.baseBottomSheet.BaseBottomSheetFragment

class RvlMessageBottomSheetFragment : BaseBottomSheetFragment(){

    override val layoutRes = R.layout.fragment_rvl_message_dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClick()
    }

    private fun initClick() {
        Handler().postDelayed({ // Do something after 5s = 500ms
            dismiss()
        }, 2000)
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme;
    }
}