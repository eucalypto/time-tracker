package net.eucalypto.timetracker.data

import net.eucalypto.timetracker.data.database.DatabaseCategoryDao
import net.eucalypto.timetracker.data.database.entities.asDatabaseModel
import net.eucalypto.timetracker.domain.model.Category

class Repository(
    private val categoryDao: DatabaseCategoryDao,
) {

    fun getCategoryList(): List<Category> {

        return listOf()
    }

    suspend fun insertCategory(category: Category) {
        categoryDao.insert(category.asDatabaseModel())
    }

}
