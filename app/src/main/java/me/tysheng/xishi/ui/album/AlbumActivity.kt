package me.tysheng.xishi.ui.album

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.view.View
import kotlinx.android.synthetic.main.activity_album.*
import me.tysheng.xishi.R
import me.tysheng.xishi.adapter.AlbumAdapter
import me.tysheng.xishi.adapter.PhotoViewListener
import me.tysheng.xishi.data.Picture
import me.tysheng.xishi.ext.dp2Px
import me.tysheng.xishi.ext.toast
import me.tysheng.xishi.ext.toggleInVisible
import me.tysheng.xishi.ui.BaseActivity
import org.koin.android.ext.android.inject

class AlbumActivity : BaseActivity(), AlbumContract.View {

    private val albumAdapter: AlbumAdapter by inject()
    override val presenter: AlbumContract.Presenter by inject()

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setScrollViewParams(newConfig.orientation)
    }

    private fun setScrollViewParams(orientation: Int) {
        val params = scrollView.layoutParams
        params.height = (if (orientation == Configuration.ORIENTATION_LANDSCAPE) INTRO_LANDSCAPE_HEIGHT else INTRO_PORTRAIT_HEIGHT).dp2Px()
        scrollView.layoutParams = params
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            window.statusBarColor = Color.TRANSPARENT
        }

        setContentView(R.layout.activity_album)
        presenter.view = this

        presenter.setAlbumId(intent?.getIntExtra(AlbumActivity.KEY_ALBUMS, 0) ?: 0)
        /**
         * Landscape
         */
        setScrollViewParams(this.resources.configuration.orientation)
        viewPager.adapter = albumAdapter
        viewPager.addOnPageChangeListener(presenter.viewPagerScrollListener)
        albumAdapter.photoViewListener = object : PhotoViewListener {
            override fun longClick(picture: Picture) {
                AlertDialog.Builder(this@AlbumActivity, R.style.BlackDialog)
                        .setItems(arrayOf(getString(R.string.save), getString(R.string.share), getString(R.string.share_to_wechat_friends))) { dialogInterface, i ->
                            dialogInterface.dismiss()
                            presenter.saveImageToGallery(this@AlbumActivity, picture.url, i)
                        }.show()
            }

            override fun tap() {
                hideOrShow()
            }
        }

        presenter.fetchData()
    }

    override fun setData(picture: List<Picture>) {
        albumAdapter.data = picture
    }

    override fun selected(amount: Int, position: Int) {
        if (amount != 0) {
            val string = SpannableString("${1 + position}/$amount")
            val sizeSpan0 = RelativeSizeSpan(1.4f)
            val sizeSpan2 = RelativeSizeSpan(0.7f)
            if (position >= 9) {
                string.setSpan(sizeSpan0, 0, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            } else {
                string.setSpan(sizeSpan0, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            }
            indicator.text = string

            val picture = albumAdapter.data[position]
            val spannableString = SpannableString("${picture.title}    ${picture.author}")
            spannableString.setSpan(sizeSpan2, picture.title.length, spannableString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            tvTitle.text = spannableString
            content.text = picture.content
        }
    }


    fun hideOrShow() {
        bottomLayout.toggleInVisible()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun showPermissionDenied() {
        getString(R.string.permission_denied_hint).toast()
    }

    companion object {
        private const val KEY_ALBUMS = "KEY_ALBUMS"
        private const val INTRO_LANDSCAPE_HEIGHT = 50
        private const val INTRO_PORTRAIT_HEIGHT = 100
        fun newIntent(context: Context, id: String): Intent {
            val intent = Intent(context, AlbumActivity::class.java)
            intent.putExtra(KEY_ALBUMS, Integer.valueOf(id))
            return intent
        }
    }
}
