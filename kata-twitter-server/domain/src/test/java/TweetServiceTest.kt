import com.domain.model.tweet.TweetRepository
import com.domain.model.tweet.TweetService
import com.domain.model.user.User
import com.domain.model.user.UserRepository
import com.domain.model.user.UserService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TweetServiceTest {

    private val tweetText = "Tweet"
    private val name = "Victoria"
    private val nickname = "vicky"
    private val user = User(name, nickname)
    private lateinit var userRepository: UserRepository
    private lateinit var userService: UserService
    private lateinit var tweetService: TweetService
    private lateinit var tweetRepository: TweetRepository

    @BeforeEach
    fun setup() {
        userRepository = FakeUserRepository()
        userService = UserService(userRepository)
        tweetRepository = FakeTweetRepository()
        tweetService = TweetService(tweetRepository, userService)
    }

    @Test
    fun `list of tweets`() {
        userService.registerUser(user)
        tweetService.createTweet(user.nickname, tweetText)
        tweetService.createTweet(user.nickname, tweetText)
        val actual = tweetService.getTweetsByNickname(user.nickname)
        assertEquals(listOf(tweetText, tweetText), actual)
    }

    @Test
    fun `list of tweets empty`() {
        tweetService.createTweet(user.nickname, tweetText)
        val actual = tweetService.getTweetsByNickname(user.nickname)
        assertEquals(emptyList<String>(), actual)
    }

    @Test
    fun `user create a tweet`() {
        userService.registerUser(user)
        tweetService.createTweet(user.nickname, tweetText)
        val actual = tweetService.getTweetsByNickname(user.nickname)
        assertEquals(listOf(tweetText), actual)
    }

    @Test
    fun `nonexistent user cannot create a tweet`() {
        tweetService.createTweet(user.nickname, tweetText)
        val actual = tweetRepository.getTweetsByNickname(user.nickname)
        assertEquals(emptyList<String>(), actual)
    }

}