package com.example.realmtest

import io.realm.Realm
import io.realm.RealmModel

fun <T : RealmModel> Realm.findById(clazz: Class<T>, id: String) : T? {
	return this.where(clazz).equalTo("id", id).findFirst()
}

