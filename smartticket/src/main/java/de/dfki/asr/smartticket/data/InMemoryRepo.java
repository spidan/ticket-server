package de.dfki.asr.smartticket.data;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.SimpleStatement;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryRepo {
	private static final Logger LOG = LoggerFactory.getLogger(InMemoryRepo.class);
	private final Repository repo;

	public InMemoryRepo() {
		repo = new SailRepository(new MemoryStore());
		repo.initialize();
	}

	public void write(final Iterable<SimpleStatement> statements) {
		try (RepositoryConnection con = repo.getConnection()) {
			con.add(statements);
		} catch (RepositoryException ex) {
			LOG.error("Failed to access triplestore: " + ex.getMessage());
		}
	}

	public void write(final Model model) {
		try (RepositoryConnection con = repo.getConnection()) {
		    con.begin();
		    con.add(model);
		    con.commit();
		}

		catch (RepositoryException ex) {
			LOG.error("Failed to access triplestore: " + ex.getMessage());
		}
	}
}
