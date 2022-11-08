import com.domain.action.user.RegisterUser
import com.domain.model.user.User
import com.domain.model.user.UserRepository
import com.domain.model.user.UserService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RegisterUserTest {

    private val name = "Victoria"
    private val nickname = "vicky"
    private val user = User(name, nickname)
    private lateinit var userRepository: UserRepository
    private lateinit var userService: UserService
    private lateinit var registerUser: RegisterUser

    @BeforeEach
    fun setup(){
        userRepository = FakeUserRepository()
        userService = UserService(userRepository)
        registerUser = RegisterUser(userService)
    }

    @Test
    fun `given name and nickname then register user`(){
        registerUser.execute(user)
        val actual = userRepository.findByNickname(user.nickname)
        assertEquals(user, actual)
    }

    @Test
    fun `given registered nickname then try register and error`(){
        val registerUser = RegisterUser(userService)
        registerUser.execute(user)
        assertThrows<UserService.NicknameInUseException> { registerUser.execute(user) }
    }


}