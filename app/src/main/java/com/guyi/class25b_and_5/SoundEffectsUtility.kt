package com.guyi.class25b_and_5

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

/**
 * Simple singleton utility to play short sound effects.
 */
object SoundEffectsUtility {
    private const val MAX_STREAMS = 3

    // Lazy initialization of SoundPool
    private val soundPool: SoundPool by lazy {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        SoundPool.Builder()
            .setMaxStreams(MAX_STREAMS)
            .setAudioAttributes(audioAttributes)
            .build()
    }

    // Map resource IDs (e.g., R.raw.sound) to loaded sound IDs
    private val soundMap = mutableMapOf<Int, Int>()
    private var initialized = false

    /**
     * Initialize the utility and load the given sound resources.
     * Call once, for example in Application.onCreate()
     */
    fun init(context: Context, vararg sounds: Int) {
        if (initialized) return
        sounds.forEach { resId ->
            val soundId = soundPool.load(context, resId, 1)
            soundMap[resId] = soundId
        }
        initialized = true
    }

    /**
     * Play a loaded sound effect at full volume.
     * Throws if utility not initialized or sound not loaded.
     */
    fun play(resId: Int) {
        check(initialized) { "SoundEffectsUtility not initialized. Call init() first." }
        val soundId = soundMap[resId]
            ?: throw IllegalArgumentException("Sound resource \$resId not loaded. Did you call init()?")

        soundPool.play(soundId, /* leftVolume */ 1f, /* rightVolume */ 1f, /* priority */ 1, /* loop */ 0, /* rate */ 1f)
    }

    /**
     * Release SoundPool resources. Call when no longer needed.
     */
    fun release() {
        if (initialized) {
            soundPool.release()
            soundMap.clear()
            initialized = false
        }
    }
}
