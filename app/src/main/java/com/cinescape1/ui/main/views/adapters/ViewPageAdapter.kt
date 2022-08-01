//package com.cinescape1.ui.main.views.adapters
//
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentManager
//import androidx.fragment.app.FragmentStatePagerAdapter
//import com.cinescape1.data.models.responseModel.MoviesResponse
//import com.cinescape1.ui.main.views.fragments.ComingSoonFragment
//import com.cinescape1.ui.main.views.fragments.NowShowingFragment
//
//
//class ViewPageAdapter(
//    fm: FragmentManager,
//    behavior: Int,
//    private val output: MoviesResponse.Output
//) : FragmentStatePagerAdapter(fm, behavior) {
//    private var tabCount = behavior
//
//
//    override fun getItem(position: Int): Fragment {
//
//        var fragment: Fragment? = null
//        when (position) {
//            0 -> {
//                fragment = NowShowingFragment(output.nowshowing)
//                return fragment
//            }
//            1 -> {
//                fragment = ComingSoonFragment(output.nowshowing)
//                return fragment
//            }
//        }
//
//        return fragment!!
//    }
//
//
//    override fun getCount(): Int {
//        return tabCount
//    }
//
//    override fun getPageTitle(position: Int): CharSequence? {
//        var title: String? = null
//        when (position) {
//
//            0 -> {
//                title = "NOW SHOWING"
//                return title
//            }
//            1 -> {
//                title = "COMING SOON"
//                return title
//            }
//
//        }
//        return title!!
//
//
//    }
//
//
//}