package cn.berfy.sdk.asingle.demo.fragment.presenter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.berfy.sdk.asingle.R
import cn.berfy.sdk.asingle.demo.fragment.view.DemoFragment
import cn.berfy.sdk.asingle.demo.fragment.view.IDemoFragmentView
import cn.berfy.sdk.mvpbase.listener.CommonFragmentListener
import cn.berfy.sdk.mvpbase.prensenter.BasePresenter
import kotlinx.android.synthetic.main.activity_fragment_demo.view.*

class DemoFragmentPresenter : BasePresenter<IDemoFragmentView>(), View.OnClickListener {

    private lateinit var mFragDemo1: DemoFragment
    private lateinit var mFragDemo2: DemoFragment

    fun init() {
        mFragDemo1 = DemoFragment()
        mFragDemo2 = DemoFragment()
        view.getBtnAdd1().setOnClickListener(this)
        view.getBtnAdd2().setOnClickListener(this)
        mFragDemo1.setCommonListener(object : CommonFragmentListener {
            override fun onCreate(savedInstanceState: Bundle?) {
            }

            override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) {
            }

            override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
            }

            override fun onResume() {
            }

            override fun onPause() {
            }

            override fun onStop() {
            }

            override fun onDestroyView() {
            }

            override fun onDestroy() {
            }

            override fun onFragmentRemoved() {
                view.getBtnAdd1().visibility = View.VISIBLE
            }
        })
        mFragDemo2.setCommonListener(object : CommonFragmentListener {
            override fun onCreate(savedInstanceState: Bundle?) {
            }

            override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) {
            }

            override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
            }

            override fun onResume() {
            }

            override fun onPause() {
            }

            override fun onStop() {
            }

            override fun onDestroyView() {
            }

            override fun onDestroy() {
            }

            override fun onFragmentRemoved() {
                view.getBtnAdd2().visibility = View.VISIBLE
            }
        })
    }

    override fun onClick(v: View?) {
        when (v) {
            view.getBtnAdd1() -> {
                view.getBtnAdd1().visibility = View.GONE
                view.addNewFragment(R.id.framlayout1, mFragDemo1)
            }
            view.getBtnAdd2() -> {
                view.getBtnAdd2().visibility = View.GONE
                view.addNewFragment(R.id.framlayout2, mFragDemo2)
            }
        }
    }
}