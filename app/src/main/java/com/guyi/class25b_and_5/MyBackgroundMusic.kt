package com.guyi.class25b_and_5

import android.content.Context
import android.media.MediaPlayer


class MyBackgroundMusic private constructor(private val appContext: Context) {

    companion object {
        var instance: MyBackgroundMusic? = null
            private set

        fun initHelper(context: Context): MyBackgroundMusic? {
            if (instance == null) {
                instance = MyBackgroundMusic(context.applicationContext)
            }
            return instance
        }
    }
    
    private var mediaPlayer: MediaPlayer? = null
    private var RAW_RESOURCE_ID = 0
    private var backSoundOn = true

    fun setResourceId(rawResourceId: Int) {
        RAW_RESOURCE_ID = rawResourceId
        initMediaPlayer()
    }

    private fun initMediaPlayer() {
        if (mediaPlayer != null) {
            release()
        }
        mediaPlayer = MediaPlayer.create(appContext, RAW_RESOURCE_ID)
        mediaPlayer?.setLooping(true)
        mediaPlayer?.setVolume(0.15f, 0.15f)
    }

    fun setBackSound(backSoundOn: Boolean) {
        this.backSoundOn = backSoundOn
    }

    fun playMusic() {
        if (!backSoundOn) {
            return
        }

        if (mediaPlayer != null && mediaPlayer?.isPlaying == false) {
            try {
                mediaPlayer?.start()
            } catch (ex: IllegalStateException) {
                ex.printStackTrace()
            }
        } else {
            initMediaPlayer()
            try {
                mediaPlayer?.start()
            } catch (ex: IllegalStateException) {
                ex.printStackTrace()
            }
        }
    }

    fun pauseMusic() {
        if (mediaPlayer != null && mediaPlayer?.isPlaying == true) {
            try {
                mediaPlayer?.pause()
            } catch (ex: IllegalStateException) {
                ex.printStackTrace()
            }
        }
    }

    fun stopMusic() {
        if (mediaPlayer != null && mediaPlayer?.isPlaying == true) {
            try {
                mediaPlayer?.stop()
                release()
            } catch (ex: IllegalStateException) {
                ex.printStackTrace()
            }
        }
    }

    private fun release() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer?.release()
                mediaPlayer = null
            } catch (ex: IllegalStateException) {
                ex.printStackTrace()
            }
        }
    }

    val isMusicPlaying: Boolean
        get() {
            if (mediaPlayer != null) {
                try {
                    return mediaPlayer?.isPlaying ?: false
                } catch (ex: Exception) {
                }
            }
            return false
        }
}
