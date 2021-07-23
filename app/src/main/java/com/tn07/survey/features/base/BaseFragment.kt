package com.tn07.survey.features.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Created by toannguyen
 * Jul 16, 2021 at 14:07
 */

typealias ViewBindingInflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

open class BaseFragment<VB : ViewBinding>(
    private val inflate: ViewBindingInflate<VB>
) : Fragment() {

    private var compositeDisposable = CompositeDisposable()
    private val locker = Any()

    private var _binding: VB? = null
    internal val binding get() = requireNotNull(_binding)

    fun Disposable.addToCompositeDisposable() {
        synchronized(locker) {
            compositeDisposable.add(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            handleSystemBarInsets(insets)
            WindowInsetsCompat.CONSUMED
        }
    }

    open fun handleSystemBarInsets(insets: Insets) {

    }

    override fun onResume() {
        super.onResume()
        synchronized(locker) {
            if (compositeDisposable.isDisposed) {
                compositeDisposable = CompositeDisposable()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        synchronized(locker) {
            compositeDisposable.dispose()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}