package com.guyi.class25b_and_5

import android.app.Application

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        MyBackgroundMusic.initHelper(this)
        MyBackgroundMusic.instance?.setResourceId(R.raw.msc_game_loop)

        SoundEffectsUtility.init(
            this,
            R.raw.snd_coin,
            R.raw.snd_game_over,
        )
    }
}