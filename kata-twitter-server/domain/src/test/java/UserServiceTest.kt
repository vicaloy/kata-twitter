import com.domain.model.user.User
import com.domain.model.user.UserRepository
import com.domain.model.user.UserService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class UserServiceTest {

    private val newName = "Victoria Aloy"
    private val user = User("Victoria", "vicky")
    lateinit var userRepository: UserRepository
    private lateinit var userService: UserService


    @BeforeEach
    fun setup() {
        userRepository = FakeUserRepository()
        userService = UserService(userRepository)
    }

    @Test
    fun `given name and nickname then register user`() {
        givenUsers(user)
        val actual = userService.findUserByNickname(user.nickname)
        assertEquals(user, actual)
    }

    @Test
    fun `given registered nickname then try register and error`() {
        givenUsers(user)
        assertThrows<UserService.NicknameInUseException> { userService.registerUser(user) }
    }

    @Test
    fun `given existing user nickname and a new name then update user`() {
        givenUsers(user)
        userService.updateUser(user.nickname, newName)
        val actual = userService.findUserByNickname(user.nickname)
        assertEquals(newName, actual?.name)
    }

    @Test
    fun `given nonexistent user nickname and a new name then update nothing`() {
        userService.updateUser(user.nickname, newName)
        val actual = userService.findUserByNickname(user.nickname)
        assertEquals(null, actual?.name)
    }


    private fun givenUsers(vararg users: User) {
        users.forEach {
            userService.registerUser(it)
        }
    }


}