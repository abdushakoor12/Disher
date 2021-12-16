package com.example.disher.dishes.usecase

import com.example.disher.dishes.model.DishesResponse
import com.example.disher.dishes.repository.IDishesRepository

interface IGetDishesUseCase {
    suspend operator fun invoke(categoryName: String): DishesResponse
}

class GetDishesUseCase constructor(
    val repo: IDishesRepository
) : IGetDishesUseCase {

    override suspend fun invoke(categoryName: String): DishesResponse {
        return repo.getDishesForCategory(categoryName)
    }
}