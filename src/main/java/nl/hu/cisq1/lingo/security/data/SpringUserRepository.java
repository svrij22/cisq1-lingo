package nl.hu.cisq1.lingo.security.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * This is a magic interface, which is converted
 * to a class during compilation.
 *
 * Note that this introduces coupling between Chips and the way they are stored!
 * In more loosely coupled components, you would add an intermediary abstraction
 * like an abstract repository or a DAO!
 */
public interface SpringUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query(value = "select *, (select score from games where games.id = users.game_id) as score from users where (select score from games where games.id = users.game_id) is not null order by score desc limit 10",
            nativeQuery = true)
    Collection<User> getTop10UsersByScore();
}