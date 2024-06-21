package telran.java51.communication.dao;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import telran.java51.communication.model.Index;

public interface IndexRepository extends CrudRepository<Index, String> {

	Index findBySource(String source);
	Set<Index> findAllBySourceIn(Set<String> sources);
}
