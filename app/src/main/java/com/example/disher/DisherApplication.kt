package com.example.disher

import android.app.Application
import androidx.room.Room
import com.example.disher.category.repository.CategoryRepository
import com.example.disher.category.repository.ICategoryRepository
import com.example.disher.category.service.ICategoryService
import com.example.disher.category.usecase.GetCategoriesUseCase
import com.example.disher.category.usecase.IGetCategoriesUseCase
import com.example.disher.db.DisherDao
import com.example.disher.db.DisherDatabase
import com.example.disher.detail.repository.DetailRepository
import com.example.disher.detail.repository.IDetailRepository
import com.example.disher.detail.service.IDetailService
import com.example.disher.detail.usecase.GetDetailsUseCase
import com.example.disher.detail.usecase.IGetDetailsUseCase
import com.example.disher.dishes.repository.DishesRepository
import com.example.disher.dishes.repository.IDishesRepository
import com.example.disher.dishes.service.IDishesService
import com.example.disher.dishes.usecase.GetDishesUseCase
import com.example.disher.dishes.usecase.IGetDishesUseCase
import com.zhuinden.simplestack.GlobalServices
import com.zhuinden.simplestackextensions.servicesktx.add
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DisherApplication : Application(){

    lateinit var globalServices: GlobalServices
    private set

    override fun onCreate() {
        super.onCreate()

        val database = getDatabase()
        val dao = database.provideDao()
        val retrofit = getRetrofit()

        // retrofit and services
        val categoryService = retrofit.create(ICategoryService::class.java)
        val dishesService = retrofit.create(IDishesService::class.java)
        val detailsService = retrofit.create(IDetailService::class.java)

        // use cases and repos
        val dispatcher = Dispatchers.IO
        val categoryRepository = provideCategoryRepository(categoryService, dao, dispatcher)
        val dishesRepository = provideDishesRepository(dishesService, dao, dispatcher)
        val detailsRepository = provideDetailRepo(detailsService)
        val dishesUseCase = provideGetDishesUseCase(dishesRepository)
        val detailsUseCase = provideGetDetailUseCase(detailsRepository)
        val categoryUseCase: IGetCategoriesUseCase = provideGetCategoryUseCase(categoryRepository)

        globalServices = GlobalServices.builder()
            .add(database).add(dao)
            .add(dishesUseCase).add(detailsUseCase).add(categoryUseCase)
            .build()

    }

    private fun getDatabase(): DisherDatabase {
        return Room.databaseBuilder(
            this.applicationContext,
            DisherDatabase::class.java,
            "disher_database"
        ).build()
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun provideDishesRepository(
        dishesService: IDishesService,
        dao: DisherDao,
        dispatcher: CoroutineDispatcher,
    ): IDishesRepository = DishesRepository(dishesService, dao, dispatcher)

    fun provideGetDishesUseCase(repo: IDishesRepository): IGetDishesUseCase = GetDishesUseCase(repo)

    fun provideDetailRepo(service: IDetailService): IDetailRepository = DetailRepository(service)

    fun provideGetDetailUseCase(repository: IDetailRepository): IGetDetailsUseCase =
        GetDetailsUseCase(repository)

    fun provideCategoryRepository(
        service: ICategoryService,
        dao: DisherDao,
        dispatcher: CoroutineDispatcher,
    ): ICategoryRepository = CategoryRepository(service, dao, dispatcher)

    fun provideGetCategoryUseCase(repository: ICategoryRepository): IGetCategoriesUseCase = GetCategoriesUseCase(repository)
}