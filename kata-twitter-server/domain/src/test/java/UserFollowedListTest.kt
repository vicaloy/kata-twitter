import com.domain.action.follow.UserFollowedList
import com.domain.model.follow.FollowRepository
import com.domain.model.follow.FollowService
import com.domain.model.user.User
import com.domain.model.user.UserRepository
import com.domain.model.user.UserService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserFollowedListTest {

    private val followerUser = User("Victoria", "vicky")
    private val followedUser = User("Jessica", "jessi")
    private val followedTwoUser = User("Lucia", "lu")
    private lateinit var followRepository: FollowRepository
    private lateinit var userRepository: UserRepository
    private lateinit var userService: UserService
    private lateinit var followService: FollowService
    private lateinit var userFollowedList: UserFollowedList

    @BeforeEach
    fun setup(){
        followRepository = FakeFollowRepository()
        userRepository = FakeUserRepository()
        userService = UserService(userRepository)
        followService = FollowService(followRepository, userService)
        userFollowedList = UserFollowedList(followService)
    }

    @Test
    fun `user found in followed list of the another`() {
        givenUsers(followerUser, followedUser)
        whenFollowUsers(Pair(followedUser.nickname, followerUser.nickname))
        thenFollowedByNicknameExists(listOf(followedUser), listOf(followerUser))
    }

    @Test
    fun `two user found in followed list of the another`() {
        givenUsers(followedUser, followerUser, followedTwoUser)
        whenFollowUsers(
            Pair(followedUser.nickname, followerUser.nickname),
            Pair(followedTwoUser.nickname, followerUser.nickname)
        )
        thenFollowedByNicknameExists(listOf(followedUser, followedTwoUser), listOf(followerUser))

    }

    @Test
    fun `no user found in followed list of another nonexistent`() {
        givenUsers(followerUser)
        whenFollowUsers(Pair(followedUser.nickname, followerUser.nickname))
        thenFollowedByNicknameNotExists(listOf(followedUser), listOf(followerUser))
    }

    @Test
    fun `no nonexistent user found in followed list of another existent`() {
        givenUsers(followedUser)
        whenFollowUsers(Pair(followedUser.nickname, followerUser.nickname))
        thenFollowedByNicknameNotExists(listOf(followedUser), listOf(followerUser))
    }

    private fun givenUsers(vararg users: User) {
        users.forEach {
            userService.registerUser(it)
        }
    }

    private fun whenFollowUsers(vararg followUsers: Pair<String, String>) {
        followUsers.forEach {
            followService.followUser(it.first, it.second)
        }
    }

    private fun hasUserFollowed(followedUsers: List<User>, followerUsers: List<User>): Boolean {
        val followers = mutableListOf<User>()
        followerUsers.forEach {
            followers.addAll(userFollowedList.execute(it.nickname))
        }

        val result = followers.containsAll(followedUsers)
        return result
    }

    private fun thenFollowedByNicknameExists(followedUsers: List<User>, followerUsers: List<User>) {
        val result = hasUserFollowed(followedUsers, followerUsers)
        Assertions.assertTrue(result)
    }

    private fun thenFollowedByNicknameNotExists(followedUsers: List<User>, followerUsers: List<User>) {
        val result = hasUserFollowed(followedUsers, followerUsers)
        Assertions.assertFalse(result)
    }


}