package me.tysheng.xishi.module.main

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.activity_main.*
import me.tysheng.xishi.R
import me.tysheng.xishi.adapter.MainsAdapter
import me.tysheng.xishi.data.Album
import me.tysheng.xishi.ext.mainThreadPost
import me.tysheng.xishi.ext.toast
import me.tysheng.xishi.module.BaseActivity
import me.tysheng.xishi.module.DialogCallback
import me.tysheng.xishi.module.MainDialogAction
import me.tysheng.xishi.module.MenuMoreDialog
import me.tysheng.xishi.net.data.CommonResponse
import me.tysheng.xishi.utils.Navigator
import me.tysheng.xishi.utils.SnackBarUtil
import me.tysheng.xishi.widget.RecycleViewDivider
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity(), MainContract.View, DialogCallback {


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
        toolBar.setOnClickListener { mainThreadPost { scrollToTop() } }
        toolBarMore.setOnClickListener { MenuMoreDialog().show(supportFragmentManager, MenuMoreDialog.TAG) }
        toolBar.setNavigationOnClickListener { Navigator.openRegisterLogin(this) }
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
                mainsAdapter.getItem(position)?.id?.also {
                    Navigator.openAlbum(this@MainActivity, it.toInt())
                }
            }
            setOnItemLongClickListener { _, _, position ->
                val album = mainsAdapter.getItem(position)
                album?.also {
                    AlertDialog.Builder(this@MainActivity)
                            .setMessage(getString(R.string.content_bookmark_album))
                            .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                                presenter.bookmarkAlbum(it, position)
                            }
                            .show()
                }
                true
            }
        }
        swipeRefreshLayout.apply {
            setColorSchemeResources(R.color.colorAccent)
            setOnRefreshListener {
                presenter.fetchData(true)
            }
        }
        mainThreadPost {
            swipeRefreshLayout.isRefreshing = true
            presenter.fetchData(true)
        }
    }

    override fun itemClick(action: MainDialogAction) {
        presenter.onItemClick(action = action)
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
        // smooth scroll
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

    override fun bookmarkSuccess(it: CommonResponse<Any>) {
        getString(R.string.bookmark_successfully).toast()
    }

    override fun stopRefresh() {
        mainThreadPost {
            swipeRefreshLayout.isRefreshing = false
        }
    }
}
