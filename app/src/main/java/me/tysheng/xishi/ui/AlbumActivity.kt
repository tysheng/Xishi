package me.tysheng.xishi.ui

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
import me.tysheng.xishi.di.component.DaggerAlbumComponent
import me.tysheng.xishi.di.module.AlbumModule
import me.tysheng.xishi.ext.dp2Px
import me.tysheng.xishi.ext.toast
import me.tysheng.xishi.ext.toggleInVisible
import java.util.*
import javax.inject.Inject

class AlbumActivity : BaseActivity(), AlbumContract.View {

    @Inject
    lateinit var albumAdapter: AlbumAdapter
    @Inject
    lateinit var presenter: AlbumContract.Presenter

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setScrollViewParams(newConfig.orientation)
    }

    private fun setScrollViewParams(orientation: Int) {
        val params = scrollView.layoutParams
        params.height = (if (orientation == Configuration.ORIENTATION_LANDSCAPE) 50 else 100).dp2Px()
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
        DaggerAlbumComponent.builder()
                .applicationComponent(applicationComponent)
                .albumModule(AlbumModule(this))
                .build()
                .inject(this)
        setContentView(R.layout.activity_album)
        presenter.parseIntent(intent)
        /**
         * Landscape
         */
        setScrollViewParams(this.resources.configuration.orientation)
        viewPager.adapter = albumAdapter
        viewPager.addOnPageChangeListener(presenter)
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
            val string = SpannableString(String.format(Locale.getDefault(), "%d/%d", 1 + position, amount))
            val sizeSpan0 = RelativeSizeSpan(1.4f)
            val sizeSpan2 = RelativeSizeSpan(0.7f)
            if (position >= 9) {
                string.setSpan(sizeSpan0, 0, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            } else {
                string.setSpan(sizeSpan0, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            }
            indicator.text = string
            val spannableString = SpannableString(albumAdapter.data[position].title + "    " + albumAdapter.data[position].author)
            spannableString.setSpan(sizeSpan2, albumAdapter.data[position].title.length, spannableString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            tvTitle.text = spannableString
            content.text = albumAdapter.data[position].content
        }
    }


    fun hideOrShow() {
        bottomLayout.toggleInVisible()
    }

    override fun showPermissionDenied() {
        getString(R.string.permission_denied_hint).toast()

    }

    companion object {
        const val KEY_ALBUMS = "KEY_ALBUMS"
        fun newIntent(context: Context, id: String): Intent {
            val intent = Intent(context, AlbumActivity::class.java)
            intent.putExtra(KEY_ALBUMS, Integer.valueOf(id))
            return intent
        }
    }
}
