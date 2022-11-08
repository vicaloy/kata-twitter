import com.domain.action.follow.UserFollowerList
import com.domain.model.follow.FollowRepository
import com.domain.model.follow.FollowService
import com.domain.model.user.User
import com.domain.model.user.UserRepository
import com.domain.model.user.UserService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserFollowerListTest {

    private val followerUser = User("Victoria", "vicky")
    private val followedUser = User("Jessica", "jessi")
    private lateinit var followRepository: FollowRepository
    private lateinit var userRepository: UserRepository
    private lateinit var userService: UserService
    private lateinit var followService: FollowService
    private lateinit var userFollowerList: UserFollowerList

    @BeforeEach
    fun setup(){
        followRepository = FakeFollowRepository()
        userRepository = FakeUserRepository()
        userService = UserService(userRepository)
        followService = FollowService(followRepository, userService)
        userFollowerList = UserFollowerList(followService)
    }

    @Test
    fun `user found follower list`() {
        givenUsers(followedUser, followerUser)
        whenFollowUsers(Pair(followedUser.nickname, followerUser.nickname))
        thenFollowersByNicknameExists(listOf(followedUser), listOf(followerUser))
    }


    @Test
    fun `existent user not found in follower list`() {
        givenUsers(followerUser)
        whenFollowUsers(Pair(followedUser.nickname, followerUser.nickname))
        thenFollowersByNicknameNotExists(listOf(followedUser), listOf(followerUser))
    }

    @Test
    fun `nonexistent user not found in follower list`() {
        givenUsers(followedUser)
        whenFollowUsers(Pair(followedUser.nickname, followerUser.nickname))
        thenFollowersByNicknameNotExists(listOf(followedUser), listOf(followerUser))
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
            followers.addAll(userFollowerList.execute(it.nickname))
        }

        val result = followers.containsAll(followerUsers)
        return result
    }


}