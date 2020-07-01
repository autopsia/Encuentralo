package com.sectordefectuoso.encuentralo.ui.home.postlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.ui.home.HomeFragmentDirections
import com.sectordefectuoso.encuentralo.ui.home.subcategories.SubCategoryFragmentArgs
import com.sectordefectuoso.encuentralo.utils.BaseFragment
import com.sectordefectuoso.encuentralo.utils.ResourceState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_post.view.*
import kotlinx.android.synthetic.main.fragment_postlist.*
import kotlinx.android.synthetic.main.fragment_postlist.view.*

@AndroidEntryPoint
class PostListFragment : BaseFragment() {
    //val args: SubCategoryFragmentArgs by navArgs()
    val postListViewModel : PostListViewModel by viewModels()

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

        observeData()
        
        return root
    }

    private fun observeData() {
        postListViewModel.getPostListBySubCategoryId.observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is ResourceState.Loading -> {
                    Log.i(TAG, result.toString())
                }
                is ResourceState.Success -> {
                    Log.i(TAG, result.toString())
                }
                is ResourceState.Failed -> {
                    Log.i(TAG, result.toString())
                }
            }

        })
    }

}