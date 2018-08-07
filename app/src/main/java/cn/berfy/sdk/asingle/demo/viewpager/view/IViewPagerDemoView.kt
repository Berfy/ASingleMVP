package cn.berfy.sdk.asingle.demo.viewpager.view

import cn.berfy.sdk.mvpbase.iview.IBaseView
import cn.berfy.sdk.mvpbase.view.viewpager.SpringIndicator
import cn.berfy.sdk.mvpbase.view.viewpager.viewpager.ScrollerViewPager

interface IViewPagerDemoView : IBaseView {

    fun getViewPager(): ScrollerViewPager;

    fun getSpringIndicator(): SpringIndicator;
}