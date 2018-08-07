package cn.berfy.sdk.asingle.main.presenter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import cn.berfy.sdk.asingle.main.view.IMainView
import cn.berfy.sdk.asingle.main.view.MainHTTPDemoFragment
import cn.berfy.sdk.asingle.main.view.MainMVPDemoFragment
import cn.berfy.sdk.mvpbase.prensenter.BasePresenter

class MainPresenter : BasePresenter<IMainView>() {

    fun init(supportFragmentManager: FragmentManager) {
        var fragmemts: MutableList<Fragment> = ArrayList()
        fragmemts.add(MainMVPDemoFragment())
        fragmemts.add(MainHTTPDemoFragment())
        var adapter = PageAdapter(supportFragmentManager, fragmemts);
        view.getViewPager().adapter = adapter
        // just set viewPager
        view.getSpringIndicator().tabs
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
            return if (position == 0) { "MVP示例" } else { "Http相关" };
        }
    }
}