import com.domain.action.follow.FollowUser
import com.domain.model.follow.FollowRepository
import com.domain.model.follow.FollowService
import com.domain.model.user.User
import com.domain.model.user.UserRepository
import com.domain.model.user.UserService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FollowUserTest {

    private val followerUser = User("Victoria", "vicky")
    private val followerTwoUser = User("Lucia", "lu")
    private val followedUser = User("Jessica", "jessi")
    private lateinit var followRepository: FollowRepository
    private lateinit var userRepository: UserRepository
    private lateinit var userService: UserService
    private lateinit var followService: FollowService
    private lateinit var followUser: FollowUser

    @BeforeEach
    fun setup(){
        followRepository = FakeFollowRepository()
        userRepository = FakeUserRepository()
        userService = UserService(userRepository)
        followService = FollowService(followRepository, userService)
        followUser = FollowUser(followService)
    }

    @Test
    fun `a user can follow another`(){
        givenUsers(followerUser, followedUser)
        whenFollowUsers(Pair(followedUser.nickname, followerUser.nickname))
        thenFollowersByNicknameExists(listOf(followedUser), listOf(followerUser))
    }

    @Test
    fun `two users follows another`(){
        givenUsers(followedUser, followerUser, followerTwoUser)
        whenFollowUsers(Pair(followedUser.nickname, followerUser.nickname),
            Pair(followedUser.nickname, followerTwoUser.nickname))
        thenFollowersByNicknameExists(listOf(followedUser), listOf(followerUser, followerTwoUser))
    }

    @Test
    fun `a user can not follow other nonexistent`(){
        givenUsers(followerUser)
        whenFollowUsers(Pair(followedUser.nickname, followerUser.nickname))
        thenFollowersByNicknameNotExists(listOf(followedUser), listOf(followerUser))

    }

    @Test
    fun `nonexistent user can not follow a created one`(){
        givenUsers(followerUser)
        whenFollowUsers(Pair(followedUser.nickname, followerUser.nickname))
        thenFollowersByNicknameNotExists(listOf(followedUser), listOf(followerUser))

    }

    private fun givenUsers(vararg users: User){
        users.forEach {
            userService.registerUser(it)
        }
    }

    private fun whenFollowUsers(vararg followUsers: Pair<String, String>){
        followUsers.forEach {
            followUser.execute(it.first, it.second)
        }
    }

    private fun hasUserFollowers(followedUsers: List<User>, followerUsers: List<User>): Boolean{
        val followers = mutableListOf<User>()
        followedUsers.forEach {
            followers.addAll(followService.getFollowerUsersByNickname(it.nickname))
        }

        val result = followers.containsAll(followerUsers)
        return result
    }

    private fun thenFollowersByNicknameExists(followedUsers: List<User>, followerUsers: List<User>){
        val result = hasUserFollowers(followedUsers, followerUsers)
        Assertions.assertTrue(result)
    }

    private fun thenFollowersByNicknameNotExists(followedUsers: List<User>, followerUsers: List<User>){
        val result = hasUserFollowers(followedUsers, followerUsers)
        Assertions.assertFalse(result)
    }

}