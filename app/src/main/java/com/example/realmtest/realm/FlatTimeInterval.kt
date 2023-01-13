package com.example.realmtest.realm

import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.LinkingObjects
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*

@RealmClass
open class TimeInterval : RealmObject() {

    @PrimaryKey
    var id = UUID.randomUUID().toString()

    @LinkingObjects("timeIntervals")
    val sessions: RealmResults<Session>? = null

}