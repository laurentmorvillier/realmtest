package com.example.realmtest

import com.example.realmtest.realm.Session
import com.example.realmtest.realm.TimeInterval
import io.realm.Realm
import io.realm.RealmResults
import timber.log.Timber

/**
 * The TimeManager pre-computes time related data:
 * - SessionSet: All overlapping sessions are grouped into a SessionSet,
 *   used to calculate the number of sessions and break durations
 * - FlatTimeInterval: Sessions time intervals are breaked down into smaller intervals
 *   when overlapping occurs to get faster duration calculations
 */
object TimeManager {

    var sessions: RealmResults<Session>? = null

    private val sessionIdsToProcess = mutableSetOf<String>()

    fun configure() {} // launch init

    fun sessionChanged(session: Session) {
        this.sessionIdsToProcess.add(session.id)
    }

    init {

        val realm = Realm.getDefaultInstance()

        sessions = realm.where(Session::class.java).findAllAsync()
        sessions?.addChangeListener { _, _ ->

            if (sessionIdsToProcess.isNotEmpty()) {

                realm.executeTransactionAsync({ asyncRealm ->

                    val sessions = sessionIdsToProcess.mapNotNull { asyncRealm.findById(Session::class.java, it) }
                    sessionIdsToProcess.clear()

                    for (session in sessions) {
                        Timber.d("Session id = ${session.id}")
                        Timber.d("Session time intervals count = ${session.timeIntervals.size}")
                        session.timeIntervals.deleteAllFromRealm()
                        val fti = TimeInterval()
                        session.timeIntervals.add(fti)
                        asyncRealm.insertOrUpdate(session)
                    }

                }, {
                    Timber.d("executeTransactionAsync onSuccess listener...")
                    val timeIntervals = realm.where(TimeInterval::class.java).findAll()
                    Timber.d("Total timeIntervals count = ${timeIntervals.size}")

                    timeIntervals.forEach {
                        Timber.d(">>> Time interval session count = ${it.sessions?.size}, session id = ${it.sessions?.firstOrNull()?.id}")
                    }

                }, {})

            }

        }

        realm.close()
    }

}