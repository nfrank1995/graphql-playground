package de.nfrank.graphqlplayground

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service


data class Post(
    val id: String,
    val title: String,
    val category: String,
    val authorId: String
)

data class Author(
    val id: String,
    val name: String,
    val posts: MutableList<String>
)

@Service
class PostDao {
    private val postRepo = mutableListOf(
        Post(
            id = "p0",
            title = "Hello Blog",
            category = "Lifestyle",
            authorId = "a0"
        ),
        Post(
            id = "p1",
            title = "Why you should switch from your macbook to a ENIAC",
            category = "Technic",
            authorId = "a0"
        ),
        Post(
            id = "p2",
            title = "What happens when you eat one thousand watermelons a day for a month",
            category = "Health",
            authorId = "a1"
        ),
        Post(
            id = "p3",
            title = "How I lost all my money and you can too",
            category = "Finance",
            authorId = "a1"
        ),
        Post(
            id = "p4",
            title = "How I turned my microwave into portal to other dimensions",
            category = "Technic",
            authorId = "a0"
        ),
        Post(
            id = "p5",
            title = "Why I do one million pushups a day and you should too",
            category = "Sport",
            authorId = "a1"
        ),
    )

    fun getRecentPosts(count: Int, offset: Int): List<Post> {
        return postRepo.subList(offset, offset + count)
    }

    fun createPost(title: String, category: String, authorId: String): Post {
        val post = Post(
            id = "p${postRepo.size}",
            title = title,
            category = category,
            authorId = authorId
        )

        postRepo.add(post)

        return post
    }
}

@Service
class AuthorDao {
    private val authorRepo = mutableListOf(
        Author(
            id = "a0",
            name = "Ethan Wiener",
            posts = mutableListOf(
                "p0",
                "p1",
                "p4",
            )
        ),
        Author(
            id = "a1",
            name = "Anita Bath",
            posts = mutableListOf(
                "p2",
                "p3",
                "p5",
            )
        )
    )

    fun getAuthor(id: String): Author? {
        return authorRepo.find { it.id == id }
    }

    fun createAuthor(firstname: String, name: String): Author {
        val author = Author(
            id = "a${authorRepo.size}",
            name = name,
            posts = mutableListOf()
        )

        authorRepo.add(author)

        return author
    }

}

@Controller
class PostController(
    val postDao: PostDao,
    val authorDao: AuthorDao,
) {
    @QueryMapping
    fun recentPosts(@Argument count: Int, @Argument offset: Int): List<Post> {
        return postDao.getRecentPosts(count, offset)
    }

    @SchemaMapping
    fun author(post: Post) : Author {
        return authorDao.getAuthor(post.authorId)!!
    }

    @MutationMapping
    fun createPost(
        @Argument title: String,
        @Argument category: String,
        @Argument authorId: String
    ): Post {
        return postDao.createPost(title,category,authorId)
    }
}