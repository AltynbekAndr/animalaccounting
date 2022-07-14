package kz.putinbyte.iszhfermer.ui.individuals

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kz.putinbyte.iszhfermer.ui.individuals.juridicall.JuridicallListFragment
import kz.putinbyte.iszhfermer.ui.individuals.physical.PhysicalListFragment

class IndividualPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {

        return when (position) {
            0 -> {
                PhysicalListFragment()
            }
            1 -> {
                JuridicallListFragment()
            }
            else -> {
                PhysicalListFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> {
                return "Физические лица"
            }
            1 -> {
                return "Юридические лица"
            }
        }
        return super.getPageTitle(position)
    }

}