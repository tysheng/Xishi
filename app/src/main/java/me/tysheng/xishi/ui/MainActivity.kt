package me.tysheng.xishi.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.trello.rxlifecycle2.android.ActivityEvent
import me.tysheng.xishi.BuildConfig
import me.tysheng.xishi.R
import me.tysheng.xishi.adapter.MainsAdapter
import me.tysheng.xishi.base.BaseMainActivity
import me.tysheng.xishi.bean.Mains
import me.tysheng.xishi.databinding.ActivityMainBinding
import me.tysheng.xishi.net.XishiService
import me.tysheng.xishi.utils.*
import me.tysheng.xishi.view.RecycleViewDivider
import javax.inject.Inject

class MainActivity : BaseMainActivity() {
    @Inject
    lateinit var mAdapter: MainsAdapter
    @Inject
    lateinit var mXishiService: XishiService
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var binding: ActivityMainBinding

    fun setDayNightMode() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        recreate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectAppComponent().inject(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.page = 1
        binding.toolBar.setOnClickListener { binding.toolBar.post { scrollToTop() } }
        binding.toolBar.inflateMenu(R.menu.menu_toolbar)
        binding.toolBar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_send) {
                val dialog = EmailDialog()
                dialog.show(supportFragmentManager, "")
            }
            false
        }
        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = mLayoutManager
        mAdapter.bindToRecyclerView(binding.recyclerView)
        mAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            val id = mAdapter.getItem(position)?.id
            if (!TextUtils.isEmpty(id)) {
                val intent = AlbumActivity.newIntent(this@MainActivity, id!!)
                ActivityCompat.startActivity(this@MainActivity, intent, null)
            }
        }
        mAdapter.setOnLoadMoreListener {
            binding.page = binding.page + 1
            getMains(binding.page, 1)
        }
        binding.recyclerView.addItemDecoration(RecycleViewDivider(this))
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.page = 1
            getMains(binding.page, 0)
        }
        binding.swipeRefreshLayout.post {
            binding.swipeRefreshLayout.isRefreshing = true
            binding.page = 1
            getMains(binding.page, 0)
        }
        //        RxPermissions.getInstance(this)
        //                .request(Manifest.permission.READ_PHONE_STATE,
        //                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
        //                .subscribe(new Action1<Boolean>() {
        //                    @Override
        //                    public void call(Boolean aBoolean) {
        //                        if (!aBoolean) {
        //                            SnackBarUtil.show(binding.coordinatorLayout, "没有这些权限可能会出现问题:(");
        //                        } else {
        //
        //                        }
        //                    }
        //                });
    }

    fun showAlipayFail() {
        SnackBarUtil.show(binding.coordinatorLayout, "支付宝账号已复制到剪贴板")
        val c = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        c.primaryClip = ClipData.newPlainText("alipay", "353491983@qq.com")//设置Clipboard 的内容
    }

    fun copyEmailAddress() {
        SnackBarUtil.show(binding.coordinatorLayout, "邮箱地址已复制到剪贴板")
        val c = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        c.primaryClip = ClipData.newPlainText("email", "353491983@qq.com")//设置Clipboard 的内容
    }

    override fun onBackPressed() {
        if (mAdapter.data == null || mAdapter.data.size == 0) {
            super.onBackPressed()
            return
        }
        binding.recyclerView.stopScroll()
        val pos = mLayoutManager.findFirstCompletelyVisibleItemPosition()
        if (pos == 0) {
            super.onBackPressed()
        } else {
            scrollToTop()
        }
    }

    private fun scrollToTop() {
        val pos = mLayoutManager.findFirstCompletelyVisibleItemPosition()
        if (pos > 15) {
            mLayoutManager.scrollToPosition(5)
        }
        binding.appBarLayout.setExpanded(true, true)
        binding.recyclerView.smoothScrollToPosition(0)
    }

    override fun onPause() {
        super.onPause()
        binding.recyclerView.stopScroll()
    }

    private fun getMains(page: Int, type: Int) {
        mXishiService.getMains(page)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxHelper.ioToMain())
                .doOnTerminate {
                    binding.swipeRefreshLayout.post {
                        if (binding.swipeRefreshLayout.isRefreshing) {
                            binding.swipeRefreshLayout.isRefreshing = false
                        }
                    }
                }
                .subscribe(object : StySubscriber<Mains>() {
                    override fun onError(e: Throwable) {
                        super.onError(e)
                        if (TextUtils.equals("HTTP 404 Not Found", e.message)) {
                            mAdapter.onEnd()
                        } else {
                            mAdapter.onError()
                        }
                    }

                    override fun next(mains: Mains) {
                        if (type == 0) {
                            mAdapter.setNewData(mains.album)
                        } else {
                            mAdapter.addData(mains.album)
                        }
                        mAdapter.loadMoreComplete()
                    }
                })
    }

    fun onItemClick(position: Int) {
        when (position) {
            0 -> SystemUtil.sendEmail(this)
            1 -> SystemUtil.shareAppShop(this, BuildConfig.APPLICATION_ID)
            2 -> if (AlipayZeroSdk.hasInstalledAlipayClient(this)) {
                if (!AlipayZeroSdk.startAlipayClient(this, "aex07650apwol9ijoslnm39")) {
                    showAlipayFail()
                }
            } else
                showAlipayFail()
            3 -> copyEmailAddress()
            4 -> setDayNightMode()
            else -> {
            }
        }
    }
}
