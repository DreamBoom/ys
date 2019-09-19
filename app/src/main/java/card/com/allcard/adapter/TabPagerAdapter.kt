package card.com.allcard.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

@Suppress("DUPLICATE_LABEL_IN_WHEN")
class TabPagerAdapter(fm: FragmentManager,var list: List<Fragment>) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }
}
