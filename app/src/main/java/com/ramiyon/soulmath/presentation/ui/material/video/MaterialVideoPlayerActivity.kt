package com.ramiyon.soulmath.presentation.ui.material.video

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.Toast
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.ramiyon.soulmath.R
import com.ramiyon.soulmath.base.BaseActivity
import com.ramiyon.soulmath.databinding.ActivityMaterialVideoPlayerBinding
import com.ramiyon.soulmath.domain.model.material.MaterialDetail
import com.ramiyon.soulmath.presentation.ui.material.reward.MaterialRewardActivity
import com.ramiyon.soulmath.util.Constanta.ARG_MATERIAL_ID
import com.ramiyon.soulmath.util.Constanta.ARG_MODULE_TITLE
import com.ramiyon.soulmath.util.Constanta.ARG_XP
import com.ramiyon.soulmath.util.Resource
import com.ramiyon.soulmath.util.ResourceStateCallback
import com.ramiyon.soulmath.util.ScreenOrientation
import org.koin.androidx.viewmodel.ext.android.viewModel

class MaterialVideoPlayerActivity : BaseActivity<ActivityMaterialVideoPlayerBinding>() {

    private val viewModel by viewModel<MaterialVideoPlayerViewModel>()
    private var exoPlayer: ExoPlayer? = null
    private var playerView: StyledPlayerView? = null
    private var isFavorite = false
    private lateinit var material: MaterialDetail
    private lateinit var materialId: String
    private lateinit var mediaDataSourceFactory: DataSource.Factory
    private lateinit var decorView: View

    override fun inflateViewBinding(): ActivityMaterialVideoPlayerBinding {
        return ActivityMaterialVideoPlayerBinding.inflate(layoutInflater)
    }

    override fun determineScreenOrientation(): ScreenOrientation {
        return ScreenOrientation.LANDSCAPE
    }

    override fun ActivityMaterialVideoPlayerBinding.binder() {
        materialId = intent.getStringExtra(ARG_MATERIAL_ID) ?: ""
        val moduleTitle = intent.getStringExtra(ARG_MODULE_TITLE) ?: ""
        
        setSupportActionBar(materialVideoPlayerToolbar)
        tvToolbarTitle?.text = moduleTitle
        supportActionBar?.hide()
    
        decorView = window.decorView
        decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if (visibility == 0) {
                decorView.systemUiVisibility = hideSystemBars()
            }
        }
        
        viewModel.fetchMaterialDetail(materialId).observe(this@MaterialVideoPlayerActivity) {
            when(it) {
                is Resource.Loading -> videoPlayerResourceCallback.onResourceLoading()
                is Resource.Success -> videoPlayerResourceCallback.onResourceSuccess(it.data!!)
                is Resource.Error -> videoPlayerResourceCallback.onResourceError(it.message, null)
                is Resource.Empty -> videoPlayerResourceCallback.onResourceEmpty()
            }
        }
        playerView?.setOnClickListener {
            if (exoPlayer?.playWhenReady == true)
                onPause()
            else
                onStart()
        }
    }

    private val videoPlayerResourceCallback = object : ResourceStateCallback<MaterialDetail>() {
        override fun onResourceLoading() {
            binding.ivFavorite?.visibility = View.GONE
            binding.pbVideoPlayer?.visibility = View.GONE
        }

        override fun onResourceSuccess(data: MaterialDetail) {
            binding.ivFavorite?.visibility = View.VISIBLE
            material = data
            isFavorite = data.isFavorite
            
            if(isFavorite)
                binding.ivFavorite?.setImageResource(R.drawable.ic_favorite)
            else
                binding.ivFavorite?.setImageResource(R.drawable.ic_unfavorite)
            
            binding.ivFavorite?.setOnClickListener {
                if(isFavorite) {
                    viewModel.deleteFavorite(materialId).observe(this@MaterialVideoPlayerActivity) {
                        when(it) {
                            is Resource.Success -> binding.ivFavorite?.setImageResource(R.drawable.ic_unfavorite).also { isFavorite = false }
                            is Resource.Error -> {Toast.makeText(this@MaterialVideoPlayerActivity, "Gagal menghapus favorit", Toast.LENGTH_SHORT).show()}
                            else -> {}
                        }
                    }
                } else {
                    viewModel.postFavorite(materialId).observe(this@MaterialVideoPlayerActivity) {
                        when(it) {
                            is Resource.Success -> binding.ivFavorite?.setImageResource(R.drawable.ic_favorite).also { isFavorite = true }
                            is Resource.Error -> {Toast.makeText(this@MaterialVideoPlayerActivity, "Gagal menambahkan favorit", Toast.LENGTH_SHORT).show()}
                            else -> {}
                        }
                    }
                }
            }
            
            binding.ivBack?.setOnClickListener { finish() }
            
            initializePlayer(data.videoUrl)
        }

        override fun onResourceError(message: String?, data: MaterialDetail?) {

        }
    }

    private fun initializePlayer(streamUrl: String?) {
        mediaDataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"))

        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(
            MediaItem.fromUri(streamUrl.toString()))

        val mediaSourceFactory: MediaSource.Factory = DefaultMediaSourceFactory(mediaDataSourceFactory)
    
        val playbackListener = object: Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when(playbackState) {
                    ExoPlayer.STATE_READY -> {
                        exoPlayer?.playWhenReady = true
                        binding.pbVideoPlayer?.visibility = View.GONE
                    }
                    ExoPlayer.STATE_ENDED -> {
                        val intent = Intent(this@MaterialVideoPlayerActivity, MaterialRewardActivity::class.java)
                        intent.putExtra(ARG_XP, material.xpEarned)
                        intent.putExtra(ARG_MATERIAL_ID, materialId)
                        startActivity(intent)
                        finish()
                    }
                    ExoPlayer.STATE_BUFFERING -> binding.pbVideoPlayer?.visibility = View.VISIBLE
                    else -> super.onPlaybackStateChanged(playbackState)
                }
            }
        }
        
        exoPlayer = ExoPlayer.Builder(this)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()

        exoPlayer!!.addMediaSource(mediaSource)
        exoPlayer!!.addListener(playbackListener)
        exoPlayer!!.prepare()

        binding.materialVideoPlayer?.setShutterBackgroundColor(Color.TRANSPARENT)
        binding.materialVideoPlayer?.player = exoPlayer
        binding.materialVideoPlayer?.requestFocus()
    }

    private fun releasePlayer() {
        exoPlayer?.release()
    }

    override fun onResume() {
        super.onResume()

        if (Util.SDK_INT <= 23) initializePlayer(material.videoUrl)
    }

    override fun onPause() {
        super.onPause()

        if (Util.SDK_INT <= 23) releasePlayer()
    }

    override fun onStop() {
        super.onStop()

        if (Util.SDK_INT > 23) releasePlayer()
    }
    
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if(hasFocus)
            decorView.systemUiVisibility = hideSystemBars()
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemBars() =
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN
    
}