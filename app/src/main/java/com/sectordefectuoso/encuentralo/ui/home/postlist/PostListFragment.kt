package com.sectordefectuoso.encuentralo.ui.home.postlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.ui.home.HomeFragmentDirections
import com.sectordefectuoso.encuentralo.ui.home.adapter.PostListAdapter
import com.sectordefectuoso.encuentralo.ui.home.subcategories.SubCategoryFragmentArgs
import com.sectordefectuoso.encuentralo.utils.BaseFragment
import com.sectordefectuoso.encuentralo.utils.ResourceState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_post.view.*
import kotlinx.android.synthetic.main.fragment_postlist.*
import kotlinx.android.synthetic.main.fragment_postlist.view.*

@AndroidEntryPoint
class PostListFragment : BaseFragment() {
    val args: PostListFragmentArgs by navArgs()
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

        val rvPostList : RecyclerView = root.findViewById(R.id.rvPostList)
        rvPostList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

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
                    Log.i(TAG, postListViewModel.getSubCategoryId())
                    Log.i(TAG, result.toString())
                    var adapter = PostListAdapter(result.data)
                    rvPostList.adapter = adapter
                }
                is ResourceState.Failed -> {
                    Log.i(TAG, result.toString())
                }
            }

        })
    }

}