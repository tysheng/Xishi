package me.tysheng.xishi.ui.main

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import me.tysheng.xishi.R
import me.tysheng.xishi.adapter.MainsAdapter
import me.tysheng.xishi.data.Album
import me.tysheng.xishi.ui.*
import me.tysheng.xishi.ui.album.AlbumActivity
import me.tysheng.xishi.utils.SnackBarUtil
import me.tysheng.xishi.widget.RecycleViewDivider
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity(), MainContract.View {

    private val mainsAdapter: MainsAdapter by inject()
    override val presenter: MainContract.Presenter by inject()
    private lateinit var layoutManager: LinearLayoutManager

    override fun setDayNightMode() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        recreate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.view = this
        toolBar.apply {
            setOnClickListener { toolBar.post { scrollToTop() } }
        }
        toolBarMore.setOnClickListener {
            EmailDialog().apply {
                dialogCallback = object : DialogCallback {
                    override fun itemClick(position: Int) {
                        when (position) {
                            0 -> {
                                presenter.onItemClick(SendEmail(this@MainActivity))
                            }
                            1 -> {
                                presenter.onItemClick(ShareToStore(this@MainActivity))
                            }
                            2 -> {
                                presenter.onItemClick(JumpToAlipay(this@MainActivity))
                            }
                            3 -> {
                                presenter.onItemClick(CopyEmail(this@MainActivity))
                            }
                            4 -> {
                                presenter.onItemClick(SwitchDayNightMode(this@MainActivity))
                            }
                        }

                    }
                }
                show(supportFragmentManager, EmailDialog.TAG)
            }
        }
        layoutManager = LinearLayoutManager(this)
        recyclerView.apply {
            layoutManager = this@MainActivity.layoutManager
            addItemDecoration(RecycleViewDivider(this@MainActivity))
            adapter = mainsAdapter
        }
        mainsAdapter.apply {
            setOnLoadMoreListener({
                presenter.fetchData(false)
            }, recyclerView)
            onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
                val id = mainsAdapter.getItem(position)?.id
                if (!TextUtils.isEmpty(id)) {
                    val intent = AlbumActivity.newIntent(this@MainActivity, id!!)
                    ActivityCompat.startActivity(this@MainActivity, intent, null)
                }
            }
        }
        swipeRefreshLayout.apply {
            setColorSchemeResources(R.color.colorAccent)
            setOnRefreshListener {
                presenter.fetchData(true)
            }
            post {
                swipeRefreshLayout.isRefreshing = true
                presenter.fetchData(true)
            }
        }
    }

    override fun showAlipayFail() {
        SnackBarUtil.show(coordinatorLayout, getString(R.string.alipay_copied))
        val c = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        c.primaryClip = ClipData.newPlainText("alipay", getString(R.string.email_address))
    }

    override fun copyEmailAddress() {
        SnackBarUtil.show(coordinatorLayout, getString(R.string.email_copied))
        val c = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        c.primaryClip = ClipData.newPlainText("email", getString(R.string.email_address))
    }

    override fun onBackPressed() {
        if (mainsAdapter.data.size == 0) {
            super.onBackPressed()
            return
        }
        recyclerView.stopScroll()
        val pos = layoutManager.findFirstVisibleItemPosition()
        if (pos == 0) {
            super.onBackPressed()
        } else {
            scrollToTop()
        }
    }

    private fun scrollToTop() {
        val pos = layoutManager.findFirstCompletelyVisibleItemPosition()
        if (pos > 15) {
            layoutManager.scrollToPosition(5)
        }
        appBarLayout.setExpanded(true, true)
        recyclerView.smoothScrollToPosition(0)
    }

    override fun onPause() {
        super.onPause()
        recyclerView.stopScroll()
    }

    override fun onEnd() {
        mainsAdapter.onEnd()
    }

    override fun onError() {
        mainsAdapter.onError()
    }

    override fun setNewData(album: List<Album>) {
        mainsAdapter.setNewData(album)
    }

    override fun addData(album: List<Album>) {
        mainsAdapter.addData(album)
    }

    override fun loadMoreComplete() {
        mainsAdapter.loadMoreComplete()
    }

    override fun stopRefresh() {
        swipeRefreshLayout.post {
            if (swipeRefreshLayout.isRefreshing) {
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}
