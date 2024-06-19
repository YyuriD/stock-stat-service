package telran.java51.communication.dao;

import org.springframework.data.repository.CrudRepository;

import telran.java51.communication.model.Index;

public interface IndexRepository extends CrudRepository<Index, String> {

	Index findByIndexName(String indexName);
}