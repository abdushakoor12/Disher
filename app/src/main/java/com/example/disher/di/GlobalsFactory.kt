package com.example.disher.di

//class GlobalsFactory(private val appContext: Context) : GlobalServices.Factory {
//
//    override fun create(backstack: Backstack): GlobalServices {
//
//        val database = getDatabase()
//        val dao = database.provideDao()
//        val retrofit = getRetrofit()
//
//        val builder = GlobalServices.builder()
//
//        // database
//        builder.add(database).add(dao)
//
//        // retrofit and services
//        val categoryService = retrofit.create(ICategoryService::class.java)
//        val dishesService = retrofit.create(IDishesService::class.java)
//        val detailsService = retrofit.create(IDetailService::class.java)
//
//        // use cases and repos
//        val dispatcher = Dispatchers.IO
//        val categoryRepository = provideCategoryRepository(categoryService, dao, dispatcher)
//        val dishesRepository = provideDishesRepository(dishesService, dao, dispatcher)
//        val detailsRepository = provideDetailRepo(detailsService)
//        val dishesUseCase = provideGetDishesUseCase(dishesRepository)
//        val detailsUseCase = provideGetDetailUseCase(detailsRepository)
//        val categoryUseCase = provideGetCategoryUseCase(categoryRepository)
//
//        builder.add(dishesUseCase).add(detailsUseCase).add(categoryUseCase)
//
//        return builder.build()
//    }
//
//}