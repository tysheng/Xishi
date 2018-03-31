package me.tysheng.xishi.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.view.View
import com.tbruyelle.rxpermissions2.RxPermissions
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.tysheng.xishi.R
import me.tysheng.xishi.adapter.AlbumAdapter
import me.tysheng.xishi.adapter.PhotoViewListener
import me.tysheng.xishi.base.BaseSwipeActivity
import me.tysheng.xishi.bean.DayAlbums
import me.tysheng.xishi.bean.Picture
import me.tysheng.xishi.databinding.ActivityAlbumBinding
import me.tysheng.xishi.ext.dp2Px
import me.tysheng.xishi.ext.toast
import me.tysheng.xishi.net.XishiService
import me.tysheng.xishi.utils.ImageUtil
import me.tysheng.xishi.utils.RxHelper
import me.tysheng.xishi.utils.SystemUtil
import me.tysheng.xishi.utils.TySubscriber
import java.util.*
import javax.inject.Inject

class AlbumActivity : BaseSwipeActivity() {
    @Inject
    lateinit var service: XishiService
    @Inject
    lateinit var albumAdapter: AlbumAdapter
    private lateinit var binding: ActivityAlbumBinding

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setScrollViewParams(newConfig.orientation)
    }

    private fun setScrollViewParams(orientation: Int) {
        val params = binding.scrollView.layoutParams
        params.height = (if (orientation == Configuration.ORIENTATION_LANDSCAPE) 50 else 100).dp2Px()
        binding.scrollView.layoutParams = params
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            window.statusBarColor = Color.TRANSPARENT
        }

        injectAppComponent().inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_album)
        binding.visible = true
        parseIntent()

        /**
         * Landscape
         */
        setScrollViewParams(this.resources.configuration.orientation)
        binding.viewPager.adapter = albumAdapter
        binding.viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (position == binding.amount - 1 && positionOffsetPixels == 0) {
                    binding.countForFinish = binding.countForFinish + 1
                    if (binding.countForFinish > 8) {
                        finish()
                    }
                } else {
                    binding.countForFinish = 0
                }
            }

            override fun onPageSelected(position: Int) {
                selected(position)
            }
        })
        albumAdapter.photoViewListener = object : PhotoViewListener {
            override fun longClick(picture: Picture) {
                AlertDialog.Builder(this@AlbumActivity, R.style.BlackDialog)
                        .setItems(arrayOf(getString(R.string.save), getString(R.string.share), getString(R.string.share_to_wechat_friends))) { dialogInterface, i ->
                            dialogInterface.dismiss()
                            saveImageToGallery(picture.url, i)
                        }.show()
            }

            override fun tap() {
                hideOrShow()
            }

        }

        service.getDayAlbums(binding.id)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxHelper.ioToMain())
                .subscribe(object : TySubscriber<DayAlbums>() {
                    override fun next(t: DayAlbums) {
                        binding.amount = t.picture.size
                        albumAdapter.data = t.picture
                        selected(0)
                    }
                })
    }

    private fun selected(position: Int) {
        if (binding.amount != 0) {
            val string = SpannableString(String.format(Locale.getDefault(), "%d/%d", 1 + position, binding.amount))
            val sizeSpan0 = RelativeSizeSpan(1.4f)
            val sizeSpan2 = RelativeSizeSpan(0.7f)
            if (position >= 9) {
                string.setSpan(sizeSpan0, 0, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            } else {
                string.setSpan(sizeSpan0, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            }
            binding.indicator.text = string
            val spannableString = SpannableString(albumAdapter.data[position].title + "    " + albumAdapter.data[position].author)
            spannableString.setSpan(sizeSpan2, albumAdapter.data[position].title.length, spannableString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            binding.title.text = spannableString
            binding.content.text = albumAdapter.data[position].content
        }
    }

    fun hideOrShow() {
        binding.visible = !binding.visible
    }

    private fun parseIntent() {
        binding.id = intent.getIntExtra(KEY_ALBUMS, 1322)
    }

    fun saveImageToGallery(imgUrl: String, positionInDialog: Int) {
        RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    if (!it) {
                        getString(R.string.permission_denied_hint).toast()
                    }
                }
                .observeOn(Schedulers.io())
                .filter {
                    it
                }
                .flatMap {
                    ImageUtil.saveImageToGallery(this@AlbumActivity, imgUrl)
                }
                .compose(RxHelper.ioToMain())
                .subscribe(object : TySubscriber<Uri>() {
                    override fun next(uri: Uri) {
                        val appDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        val msg = String.format(getString(R.string.picture_has_save_to),
                                appDir.absolutePath)
                        when (positionInDialog) {
                            0 -> msg.toast()
                            1 -> SystemUtil.shareVia(this@AlbumActivity, getString(R.string.share_text), getString(R.string.share_to), uri)
                            2 -> ImageUtil.shareImage2Wechat(this@AlbumActivity, uri)
                        }
                    }
                })
    }

    companion object {
        private const val KEY_ALBUMS = "KEY_ALBUMS"
        fun newIntent(context: Context, id: String): Intent {
            val intent = Intent(context, AlbumActivity::class.java)
            intent.putExtra(KEY_ALBUMS, Integer.valueOf(id))
            return intent
        }
    }
}
