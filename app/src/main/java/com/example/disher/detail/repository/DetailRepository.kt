package com.example.disher.detail.repository

import com.example.disher.detail.model.DetailResponse
import com.example.disher.detail.service.IDetailService

interface IDetailRepository {
    suspend fun getDetailsOfDish(id: String): DetailResponse
}

class DetailRepository constructor(
    val service: IDetailService
): IDetailRepository{
    override suspend fun getDetailsOfDish(id: String): DetailResponse {
        //TODO use different Dispatcher!
        return service.getDetailsForDish(id)
    }
}