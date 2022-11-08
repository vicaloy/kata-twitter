import com.domain.model.follow.Follow
import com.domain.model.follow.FollowRepository
import com.domain.model.user.User

class FakeFollowRepository : FollowRepository {
    private val following = mutableSetOf<Follow>()

    override fun getFollowersByNickname(nickname: String): List<User> {
        val followers = following.filter { it.followed.nickname == nickname }
        val users = followers.map { it.follower }
        return users
    }

    override fun add(follow: Follow) {
        following.add(follow)
    }

    override fun getFollowedByNickname(nickname: String): List<User> {
        val followed = following.filter { it.follower.nickname == nickname }
        val users = followed.map { it.followed }
        return users
    }
}