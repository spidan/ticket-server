package de.dfki.asr.smartticket.data;

import java.io.File;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RDFServiceRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(RDFServiceRegistry.class);
    private final Repository repo;

    public RDFServiceRegistry() {
	String tripleStorePath = System.getProperty("user.dir").concat("/serviceRegistry");
	File memoryStoreFile = new File(tripleStorePath);
	repo = new SailRepository(new MemoryStore(memoryStoreFile));
	repo.initialize();
    }

    public void write(final Model model) {
	try {
	    RepositoryConnection con = repo.getConnection();
	    con.begin();
	    con.add(model);
	    con.commit();
	} catch (RepositoryException ex) {
	    LOG.error("Failed to access triplestore: " + ex.getMessage());
	}
    }
}
