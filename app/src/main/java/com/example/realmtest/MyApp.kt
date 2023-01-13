package com.example.realmtest

import android.app.Application
import io.realm.Realm
import timber.log.Timber

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        Realm.getDefaultInstance().executeTransactionAsync {
            it.deleteAll()
        }
        
        TimeManager.configure()

        if (BuildConfig.DEBUG) {
            // Logs
            Timber.plant(PokerAnalyticsLogs())
        }

    }

}

class PokerAnalyticsLogs : Timber.DebugTree() {
    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
        super.log(priority, "PokerAnalytics:$tag", message, throwable)
    }
}