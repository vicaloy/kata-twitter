import com.domain.model.user.User
import com.domain.model.user.UserRepository

class FakeUserRepository : UserRepository {
    private val users = mutableListOf<User>()
    override fun add(user: User) {
        users.add(user)
    }

    override fun findByNickname(nickname: String): User? {
        return users.firstOrNull { it.nickname == nickname }
    }

    override fun updateName(nickname: String, name: String) {
        users.firstOrNull { it.nickname == nickname }?.name = name
    }

}