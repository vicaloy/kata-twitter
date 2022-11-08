package com.domain.model.user

class UserService(private val userRepository: UserRepository) {

    fun registerUser(user: User) {
        validateUser(user)
        userRepository.add(user)
    }

    fun updateUser(nickname: String, name: String) {
        userRepository.updateName(nickname, name)
    }

    fun findUserByNickname(nickname: String): User?{
        return userRepository.findByNickname(nickname)
    }

    fun isUserExisting(nickname: String): Boolean {
        return userRepository.findByNickname(nickname) != null
    }

    private fun validateUser(user: User) {
        if (isUserExisting(user.nickname))
            throw NicknameInUseException()
    }

    class NicknameInUseException : RuntimeException()
}