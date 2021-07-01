package net.eucalypto.timetracker.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import net.eucalypto.timetracker.data.database.DatabaseCategoryDao
import net.eucalypto.timetracker.data.database.entities.asDatabaseModel
import net.eucalypto.timetracker.data.database.entities.asDomainModel
import net.eucalypto.timetracker.domain.model.Category

class Repository(
    private val categoryDao: DatabaseCategoryDao,
) {

    fun getCategoryList(): LiveData<List<Category>> =
        Transformations.map(categoryDao.categoryList()) {
            it.asDomainModel()
        }

    suspend fun insert(category: Category) {
        categoryDao.insert(category.asDatabaseModel())
    }

    suspend fun delete(category: Category) {
        categoryDao.delete(category.asDatabaseModel())
    }

}
