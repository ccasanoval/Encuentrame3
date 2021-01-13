package com.cesoft.feature_geocommon

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

object Cloud {
    private var fbdb: FirebaseDatabase? = null
    @Synchronized private fun getDBInstance(): FirebaseDatabase? {
        if (fbdb == null) {
            fbdb = FirebaseDatabase.getInstance()
            try {
                fbdb!!.setPersistenceEnabled(true) /// true = Init firebase disk persistence
                fbdb!!.reference.keepSynced(false)
            }
            catch(e: Exception) {
                android.util.Log.e(tag, "getDBInstance:e:", e)
            }
        }
        return fbdb
    }

    private fun getCurrentUserID(): String {
        val a = FirebaseAuth.getInstance()
        return a.currentUser?.uid ?: ""
    }

    fun getDatabase(): DatabaseReference {
        return getDBInstance()!!.reference.child(getCurrentUserID())
    }

    fun getStorage(): StorageReference {
        return FirebaseStorage.getInstance().reference.child(getCurrentUserID())
    }

    private const val tag = "Cloud"
}