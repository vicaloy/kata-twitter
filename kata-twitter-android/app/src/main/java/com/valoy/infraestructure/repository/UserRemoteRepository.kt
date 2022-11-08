package com.valoy.infraestructure.repository

import com.valoy.core.domain.user.User
import com.valoy.core.domain.user.UserRepository
import com.valoy.infraestructure.client.UserClient
import com.valoy.infraestructure.request.RegisterUserRequest
import retrofit2.HttpException
import java.net.SocketTimeoutException

class UserRemoteRepository(private val userClient: UserClient) : UserRepository {

    companion object{
        private const val NICKNAME_IN_USE_ERROR_CODE = 406
    }

    override suspend fun add(user: User) {
        try {
            userClient.add(RegisterUserRequest.fromUser(user))
        } catch (ex: HttpException) {
            if (ex.code() != NICKNAME_IN_USE_ERROR_CODE) {
                throw UserRepository.AddUserException()
            }
        }catch (ex: SocketTimeoutException){
            throw UserRepository.AddUserException()
        }
    }
}