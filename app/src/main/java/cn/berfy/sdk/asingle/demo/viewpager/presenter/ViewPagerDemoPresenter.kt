package cn.berfy.sdk.asingle.demo.viewpager.presenter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import cn.berfy.sdk.asingle.demo.viewpager.view.IViewPagerDemoView
import cn.berfy.sdk.asingle.demo.viewpager.view.VPDemoFragment
import cn.berfy.sdk.mvpbase.prensenter.BasePresenter

class ViewPagerDemoPresenter : BasePresenter<IViewPagerDemoView>() {

    fun init(supportFragmentManager: FragmentManager) {
        var fragmemts: MutableList<Fragment> = ArrayList()
        fragmemts.add(VPDemoFragment())
        fragmemts.add(VPDemoFragment())
        fragmemts.add(VPDemoFragment())
        var adapter = PageAdapter(supportFragmentManager, fragmemts);
        view.getViewPager().adapter = adapter
        // just set viewPager
        view.getSpringIndicator().setViewPager(view.getViewPager())
    }

    private inner class PageAdapter internal constructor(fm: FragmentManager, fragments: List<Fragment>) : FragmentPagerAdapter(fm) {

        var mFragments: List<Fragment>;

        init {
            mFragments = fragments;
        }

        override fun getItem(position: Int): Fragment {
            return mFragments.get(position)
        }

        override fun getCount(): Int {
            return mFragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return position.toString();
        }
    }
}