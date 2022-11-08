import com.domain.action.tweet.GetTweetList
import com.domain.model.tweet.TweetRepository
import com.domain.model.tweet.TweetService
import com.domain.model.user.User
import com.domain.model.user.UserRepository
import com.domain.model.user.UserService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetTweetListTest {

    private val tweetText = "Tweet"
    private val name = "Victoria"
    private val nickname = "vicky"
    private val user = User(name, nickname)
    private lateinit var userRepository: UserRepository
    private lateinit var userService: UserService
    private lateinit var tweetService: TweetService
    private lateinit var tweetRepository: TweetRepository
    private lateinit var getTweetList: GetTweetList

    @BeforeEach
    fun setup() {
        userRepository = FakeUserRepository()
        userService = UserService(userRepository)
        tweetRepository = FakeTweetRepository()
        tweetService = TweetService(tweetRepository, userService)
        getTweetList = GetTweetList(tweetService)
    }

    @Test
    fun `list of tweets`() {
        userService.registerUser(user)
        tweetService.createTweet(user.nickname, tweetText)
        tweetService.createTweet(user.nickname, tweetText)
        val actual = getTweetList.execute(user.nickname)
        assertEquals(listOf(tweetText, tweetText), actual)
    }

    @Test
    fun `list of tweets empty`() {
        tweetService.createTweet(user.nickname, tweetText)
        val actual = getTweetList.execute(user.nickname)
        assertEquals(emptyList<String>(), actual)
    }

}