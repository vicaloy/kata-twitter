import com.domain.model.tweet.Tweet
import com.domain.model.tweet.TweetRepository

class FakeTweetRepository : TweetRepository {
    private val tweets = mutableListOf<Tweet>()
    override fun add(tweet: Tweet) {
        tweets.add(tweet)
    }

    override fun getTweetsByNickname(nickname: String): List<String> {
        val list = mutableListOf<String>()
        tweets.forEach{
            if(it.user.nickname==nickname)
                list.add(it.text)
        }
        return list
    }


}