package kz.putinbyte.iszhfermer.ui.diagnostic

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kz.putinbyte.iszhfermer.ui.diagnostic.canceled.CanceledListFragment
import kz.putinbyte.iszhfermer.ui.diagnostic.success.SuccessListFragment

class PagerAdapter(fm: FragmentManager, var id: Int) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {

        return when (position) {
            0 -> {
                SuccessListFragment(id)
            }
            1 -> {
                CanceledListFragment(id)
            }
            else -> {
                SuccessListFragment(id)
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> {
                return "Успешные"
            }
            1 -> {
                return "Отмененные"
            }
        }
        return super.getPageTitle(position)
    }

}