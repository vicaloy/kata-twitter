import com.domain.model.follow.FollowRepository
import com.domain.model.follow.FollowService
import com.domain.model.user.User
import com.domain.model.user.UserRepository
import com.domain.model.user.UserService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class FollowServiceTest {

    private val user = User("Victoria", "vicky")
    private val followerUser = User("Lucia", "lu")
    private val followerTwoUser = User("Jessica", "jessi")
    private lateinit var followRepository: FollowRepository
    private lateinit var userRepository: UserRepository
    private lateinit var userService: UserService
    private lateinit var followService: FollowService

    @BeforeEach
    fun setup(){
        followRepository = FakeFollowRepository()
        userRepository = FakeUserRepository()
        userService = UserService(userRepository)
        followService = FollowService(followRepository, userService)
    }

    @Test
    fun `follow user`() {
        givenUsers(user, followerUser)
        whenFollowUsers(Pair(user.nickname, followerUser.nickname))
        thenFollowersByNicknameExists(listOf(user), listOf(followerUser))
    }

    @Test
    fun `user followed by two user`() {
        givenUsers(user, followerUser, followerTwoUser)
        whenFollowUsers(
            Pair(user.nickname, followerUser.nickname),
            Pair(user.nickname, followerTwoUser.nickname)
        )
        thenFollowersByNicknameExists(listOf(user), listOf(followerUser, followerTwoUser))
    }

    @Test
    fun `get following list`() {
        givenUsers(followerUser, user)
        whenFollowUsers(Pair(user.nickname, followerUser.nickname))
        thenFollowedByNicknameExists(listOf(user), listOf(followerUser))
    }

    @Test
    fun `user can not follow other nonexistent user`() {
        givenUsers(followerUser)
        whenFollowUsers(Pair(user.nickname, followerUser.nickname))
        thenFollowersByNicknameNotExists(listOf(user), listOf(followerUser))
    }

    @Test
    fun `nonexistent user cannot follow other created user`() {
        givenUsers(user)
        whenFollowUsers(Pair(user.nickname, followerUser.nickname))
        thenFollowersByNicknameNotExists(listOf(user), listOf(followerUser))

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
            followers.addAll(followService.getFollowedUsersByNickname(it.nickname))
        }

        val result = followers.containsAll(followedUsers)
        return result
    }

    private fun thenFollowedByNicknameExists(followedUsers: List<User>, followerUsers: List<User>) {
        val result = hasUserFollowed(followedUsers, followerUsers)
        Assertions.assertTrue(result)
    }

    private fun thenFollowersByNicknameExists(followedUsers: List<User>, followerUsers: List<User>) {
        val result = hasUserFollowers(followedUsers, followerUsers)
        Assertions.assertTrue(result)
    }

    private fun thenFollowersByNicknameNotExists(followedUsers: List<User>, followerUsers: List<User>) {
        val result = hasUserFollowers(followedUsers, followerUsers)
        Assertions.assertFalse(result)
    }

    private fun hasUserFollowers(followedUsers: List<User>, followerUsers: List<User>): Boolean {
        val followers = mutableListOf<User>()
        followedUsers.forEach {
            followers.addAll(followService.getFollowerUsersByNickname(it.nickname))
        }

        val result = followers.containsAll(followerUsers)
        return result
    }
}