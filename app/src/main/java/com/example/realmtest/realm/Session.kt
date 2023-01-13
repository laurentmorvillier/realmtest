package com.example.realmtest.realm

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Session : RealmObject() {

    @PrimaryKey
    var id = UUID.randomUUID().toString()

    var size: Int? = null

    var timeIntervals: RealmList<TimeInterval> = RealmList()

}