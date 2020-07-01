package com.sectordefectuoso.encuentralo.ui.home.postlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.ui.home.HomeFragmentDirections
import com.sectordefectuoso.encuentralo.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_post.view.*
import kotlinx.android.synthetic.main.fragment_postlist.*
import kotlinx.android.synthetic.main.fragment_postlist.view.*

@AndroidEntryPoint
class PostListFragment : BaseFragment() {
    override val TAG: String
        get() = "PostListFragment"

    override fun getLayout(): Int {
        return R.layout.fragment_postlist
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(getLayout(), container, false)

        root.btnPostListTest.setOnClickListener {

            val  action = PostListFragmentDirections.actionPostListFragmentToPostFragment()
            root.findNavController().navigate(action)
        }

        return root
    }


}