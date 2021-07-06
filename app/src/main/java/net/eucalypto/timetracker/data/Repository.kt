package net.eucalypto.timetracker.data

import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.*
import net.eucalypto.timetracker.data.database.TimeTrackerDatabase
import net.eucalypto.timetracker.data.database.entities.DatabaseActivity
import net.eucalypto.timetracker.data.database.entities.asDatabaseModel
import net.eucalypto.timetracker.data.database.entities.asDomainModel
import net.eucalypto.timetracker.data.database.getDatabase
import net.eucalypto.timetracker.domain.model.Activity
import net.eucalypto.timetracker.domain.model.Category
import java.util.*

class Repository(
    database: TimeTrackerDatabase,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    private val categoryDao = database.categoryDao
    private val activityDao = database.activityDao

    fun getCategoriesAsLiveData(): LiveData<List<Category>> =
        Transformations.map(categoryDao.categoriesAsLiveData()) {
            it.asDomainModel()
        }

    suspend fun getCategories(): List<Category> {
        return withContext(defaultDispatcher) {
            categoryDao.categoryList().asDomainModel()
        }
    }

    suspend fun insert(category: Category) {
        categoryDao.insert(category.asDatabaseModel())
    }

    suspend fun delete(category: Category) {
        categoryDao.delete(category.asDatabaseModel())
    }

    fun getActivitiesAsLiveData(): LiveData<List<Activity>> =
        activityDao.getActivitiesAsLiveData().switchMap { databaseActivities ->
            liveData {
                emit(databaseActivities.asDomainModel(this@Repository))
            }
        }

    suspend fun getCategoryById(categoryId: UUID): Category? {
        return categoryDao.byId(categoryId)?.asDomainModel()
    }

    suspend fun getLastActivity(): Activity? {
        val databaseActivity = activityDao.getLastActivity() ?: return null
        return domainModelFrom(databaseActivity)
    }


    private suspend fun domainModelFrom(databaseActivity: DatabaseActivity): Activity {
        val uuid = UUID.fromString(databaseActivity.categoryId)
        val category = getCategoryById(uuid)!!
        return Activity(
            category,
            databaseActivity.startTime,
            databaseActivity.endTime,
            databaseActivity.id
        )
    }

    suspend fun update(activity: Activity) {
        activityDao.update(activity.asDataBaseModel())
    }

    suspend fun insert(activity: Activity) {
        activityDao.insert(activity.asDataBaseModel())
    }
}

private lateinit var INSTANCE: Repository

fun getRepository(context: Context): Repository {
    if (!::INSTANCE.isInitialized) {
        INSTANCE = Repository(getDatabase(context.applicationContext))
    }

    return INSTANCE
}


fun Activity.asDataBaseModel(): DatabaseActivity {
    return DatabaseActivity(
        category.id.toString(),
        startTime,
        endTime,
        id
    )
}

suspend fun List<DatabaseActivity>.asDomainModel(repository: Repository): List<Activity> {
    return withContext(Dispatchers.Default) {
        val categoryList = repository.getCategories()
        val categories = categoryList.associateBy { it.id.toString() }

        this@asDomainModel.map {
            Activity(
                categories[it.categoryId]!!,
                it.startTime,
                it.endTime,
                it.id
            )
        }
    }
}