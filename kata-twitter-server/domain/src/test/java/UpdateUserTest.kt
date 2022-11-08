import com.domain.action.user.UpdateUser
import com.domain.model.user.User
import com.domain.model.user.UserRepository
import com.domain.model.user.UserService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UpdateUserTest {

    private val name = "Victoria"
    private val nickname = "vicky"
    private val newName = "Victoria Aloy"
    private val user = User(name, nickname)
    private lateinit var userRepository: UserRepository
    private lateinit var userService: UserService
    private lateinit var updateUser: UpdateUser

    @BeforeEach
    fun setup(){
        userRepository = FakeUserRepository()
        userService = UserService(userRepository)
        updateUser = UpdateUser(userService)
    }

    @Test
    fun `given existing user nickname and a new name then update user`(){
        userService.registerUser(user)
        updateUser.execute(user.nickname, newName)
        val actual = userRepository.findByNickname(user.nickname)
        Assertions.assertEquals(newName, actual?.name)
    }

    @Test
    fun `given nonexistent user nickname and a new name then update nothing`(){
        updateUser.execute(user.nickname, newName)
        val actual = userRepository.findByNickname(user.nickname)
        Assertions.assertEquals(null, actual)
    }

}