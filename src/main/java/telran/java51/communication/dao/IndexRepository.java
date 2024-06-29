package telran.java51.communication.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;
import telran.java51.communication.model.Index;

public interface IndexRepository extends CrudRepository<Index, String> {

	Optional<Index> findBySource(String source);
	
	@Transactional
	void deleteBySource(String source);
	
	List<Index> findAllBySourceIn(Set<String> sources);
	
}
