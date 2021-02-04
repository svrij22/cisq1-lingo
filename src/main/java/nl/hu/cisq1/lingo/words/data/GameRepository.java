package nl.hu.cisq1.lingo.words.data;

import nl.hu.cisq1.lingo.words.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * We depend on an interface,
 * Spring generates an implementation based on our configured adapters
 * (see: application.properties and pom.xml)
 */
public interface GameRepository extends JpaRepository<Game, String> {
    Optional<Game> findById(Integer id);
}
