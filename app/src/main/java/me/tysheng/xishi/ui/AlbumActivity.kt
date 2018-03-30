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
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.Toast
import com.tbruyelle.rxpermissions2.RxPermissions
import com.trello.rxlifecycle2.android.ActivityEvent
import me.tysheng.xishi.R
import me.tysheng.xishi.adapter.AlbumAdapter
import me.tysheng.xishi.base.BaseSwipeActivity
import me.tysheng.xishi.bean.DayAlbums
import me.tysheng.xishi.databinding.ActivityAlbumBinding
import me.tysheng.xishi.net.XishiService
import me.tysheng.xishi.utils.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class AlbumActivity : BaseSwipeActivity() {
    @Inject
    lateinit var mXishiService: XishiService
    @Inject
    lateinit var mAdapter: AlbumAdapter
    private lateinit var binding: ActivityAlbumBinding

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setScrollViewParams(newConfig.orientation)
    }

    private fun setScrollViewParams(orientation: Int) {
        val params = binding.scrollView.layoutParams
        params.height = ScreenUtil.dip2px((if (orientation == Configuration.ORIENTATION_LANDSCAPE) 60 else 120).toFloat())
        binding.scrollView.layoutParams = params
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            window.statusBarColor = Color.TRANSPARENT //也可以设置成灰色透明的，比较符合Material Design的风格
        }

        injectAppComponent().inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_album)
        binding.visible = true
        parseIntent()

        /**
         * 横屏
         */
        setScrollViewParams(this.resources.configuration.orientation)
        binding.viewPager.adapter = mAdapter
        binding.viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (position == binding.amount - 1 && positionOffsetPixels == 0) {
                    binding!!.countForFinish = binding!!.countForFinish + 1
                    if (binding!!.countForFinish > 8) {
                        finish()
                    }
                } else {
                    binding!!.countForFinish = 0
                }
            }

            override fun onPageSelected(position: Int) {
                selected(position)
            }
        })

        mXishiService.getDayAlbums(binding.id)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxHelper.ioToMain())
                .subscribe(object : StySubscriber<DayAlbums>() {
                    override fun next(dayAlbums: DayAlbums) {
                        Timber.d(dayAlbums.toString())
                        binding.amount = dayAlbums.picture.size
                        mAdapter.data = dayAlbums.picture
                        selected(0)
                    }
                })
    }

    private fun selected(position: Int) {
        if (binding!!.amount != 0) {
            val string = SpannableString(String.format(Locale.getDefault(), "%d/%d", 1 + position, binding!!.amount))
            val sizeSpan0 = RelativeSizeSpan(1.4f)
            val sizeSpan2 = RelativeSizeSpan(0.7f)
            if (position >= 9) {
                string.setSpan(sizeSpan0, 0, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            } else {
                string.setSpan(sizeSpan0, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

            }
            binding!!.indicator.text = string
            val spannableString = SpannableString(mAdapter!!.data[position].title + "    " + mAdapter!!.data[position].author)
            spannableString.setSpan(sizeSpan2, mAdapter!!.data[position].title.length, spannableString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            binding!!.title.text = spannableString
            binding!!.content.text = mAdapter!!.data[position].content
        }
    }

    fun hideOrShow() {
        binding!!.visible = !binding!!.visible
    }

    private fun parseIntent() {
        binding.id = intent.getIntExtra("albums", 1322)
    }

    fun saveImageToGallery(position: Int, i: Int) {
        RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(object : StySubscriber<Boolean>() {
                    override fun next(aBoolean: Boolean) {
                        if (aBoolean) {
                            ImageUtil.saveImageToGallery(this@AlbumActivity, mAdapter!!.data[position].url)
                                    .compose(RxHelper.ioToMain())
                                    .subscribe(object : StySubscriber<Uri>() {
                                        override fun next(uri: Uri) {
                                            val appDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                                            val msg = String.format(getString(R.string.picture_has_save_to),
                                                    appDir.absolutePath)
                                            when (i) {
                                                0 -> Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                                                1 -> SystemUtil.share(this@AlbumActivity, "来自西施App的图片分享", "分享到", uri)
                                                2 -> ImageUtil.shareImage(this@AlbumActivity, uri)
                                                else -> {
                                                }
                                            }
                                        }
                                    })
                        } else {
                            Toast.makeText(applicationContext, "Oops permission denied", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
    }

    companion object {

        fun newIntent(context: Context, id: String): Intent {
            val intent = Intent(context, AlbumActivity::class.java)
            intent.putExtra("albums", Integer.valueOf(id))
            return intent
        }
    }
}
