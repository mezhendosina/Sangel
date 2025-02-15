package ru.sangel.zaya.data

import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import ru.sangel.zaya.data.SETTINGS_PREFERENCES
import ru.sangel.zaya.data.createDataStoreWithDefaults
import java.io.File

actual fun dataStorePreferences(
    applicationContext: Context,
    corruptionHandler: ReplaceFileCorruptionHandler<Preferences>?,
    coroutineScope: CoroutineScope,
    migrations: List<DataMigration<Preferences>>,
): DataStore<Preferences> =
    createDataStoreWithDefaults(
        corruptionHandler = corruptionHandler,
        migrations = migrations,
        coroutineScope = coroutineScope,
        path = {
            File(applicationContext.filesDir, "datastore/$SETTINGS_PREFERENCES").path
        },
    )
