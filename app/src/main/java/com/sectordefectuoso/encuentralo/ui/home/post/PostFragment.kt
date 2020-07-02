package com.sectordefectuoso.encuentralo.ui.home.post

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.sectordefectuoso.encuentralo.R
import com.sectordefectuoso.encuentralo.utils.BaseFragment
import com.sectordefectuoso.encuentralo.utils.ResourceState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_post.*
import kotlinx.android.synthetic.main.item_postlist.*

@AndroidEntryPoint
class PostFragment : BaseFragment() {
    private val args: PostFragmentArgs by navArgs()
    private val postViewModel: PostViewModel by viewModels()

    override val TAG: String
        get() = "PostFragment"

    override fun getLayout(): Int {
        return R.layout.fragment_post
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(getLayout(), container, false)

        observeData()
        return root
    }

    private fun observeData(){
        postViewModel.getServiceById.observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is ResourceState.Loading -> {
                    Log.i(TAG, result.toString())
                }
                is ResourceState.Success -> {
                    Log.i(TAG, result.data.toString())
                    val data = result.data
                    tvPostTitle.text = data?.title
                    tvPostDescripcion.text =data?.description

                }
            }
        })

        postViewModel.getUserById.observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is ResourceState.Loading -> {
                    Log.i(TAG, result.toString())
                }
                is ResourceState.Success -> {
                    Log.i(TAG, result.data.toString())
                    val data = result.data
                    tvPostUsername.text = "${data?.names} ${data?.lastNames}"
                    tvPostLastOnline.text = "Online hace X horas"//DateUtils.getRelativeTimeSpanString(data.lastOnline)
                    Glide.with(requireContext()).load(data?.imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(false)
                        .centerCrop().into(ivPostImageProfile)
                    btnPostLlamar.setOnClickListener {
                        data?.phone?.let { phone -> makePhoneCall(phone) }
                    }
                }
            }
        })
    }

    fun makePhoneCall(number: String) : Boolean {
        try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
            startActivity(intent)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}