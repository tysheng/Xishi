package me.tysheng.xishi.module.register

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_reigster.*
import me.tysheng.xishi.R
import me.tysheng.xishi.ext.md5
import me.tysheng.xishi.ext.toast
import me.tysheng.xishi.net.XishiService
import me.tysheng.xishi.net.data.RegisterParam
import org.koin.android.ext.android.inject

/**
 * Created by tysheng
 * Date: 26/9/18 7:34 PM.
 * Email: tyshengsx@gmail.com
 */
class RegisterFragment : Fragment() {
    private val xishiService: XishiService by inject()
    private var disposable: Disposable? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reigster, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolBar.setNavigationOnClickListener { activity?.finish() }

//        nameInput.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                nameInputLayout.error = if (s?.length !in 2..10) {
//                    getString(R.string.username_input_error)
//                } else {
//                    null
//                }
//            }
//        })
        val helperColor = ColorStateList.valueOf(ContextCompat.getColor(context!!, R.color.material_blue_grey_800))
        nameInputLayout.helperText = getString(R.string.username_input_error)
        nameInputLayout.setHelperTextColor(helperColor)
        passwordInputLayout.helperText = getString(R.string.password_input_error)
        passwordInputLayout.setHelperTextColor(helperColor)

//        passwordInput.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                passwordInputLayout.error = if (s?.length !in 4..16) {
//                    getString(R.string.password_input_error)
//                } else {
//                    null
//                }
//            }
//        })

        confirm.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val name = nameInput.text?.toString()
        val password = passwordInput.text?.toString()
        if (name?.length !in 2..10) {
            getString(R.string.username_input_error).toast()
            return
        }
        if (password?.length !in 4..16) {
            getString(R.string.password_input_error).toast()
            return
        }

        val param = RegisterParam(name!!, password!!.md5())
        disposable = xishiService.register(param)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {

                }
    }

    override fun onDestroyView() {
        disposable?.dispose()
        super.onDestroyView()
    }
}