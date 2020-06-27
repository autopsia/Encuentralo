package com.sectordefectuoso.encuentralo.ui.home.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostFragment : BaseFragment() {
    private val postViewModel: PostViewModel by viewModels()

    override val TAG: String
        get() = TODO("Not yet implemented")

    override fun getLayout(): Int {
        return R.layout.fragment_post
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(getLayout(), container, false)

        return root
    }
}