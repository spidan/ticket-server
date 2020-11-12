package de.dfki.asr.smartticket.data;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;
import lombok.Getter;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
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

    @Getter
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

    public URI getTemplateUriForService(final String serviceName) throws URISyntaxException {
	RepositoryConnection con = repo.getConnection();
	String queryString = "PREFIX sm: <http://www.smart-maas.eu/#>"
		+ "SELECT ?uri WHERE {sm:"
		+ serviceName + " sm:hasTemplate ?uri . }";
	TupleQuery tupleQuery = con.prepareTupleQuery(queryString);
	TupleQueryResult result = tupleQuery.evaluate();
	if (result.hasNext()) {
	    return new URI(result.next().getValue("uri").stringValue());
	}
	throw new NoSuchElementException("No template found for given service");
    }

    public URI getServiceEndpoint(final String serviceName) throws URISyntaxException {
	RepositoryConnection con = repo.getConnection();
	String queryString = "PREFIX sm: <http://www.smart-maas.eu/#> "
		+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
		+ "PREFIX wsdl: <https://www.w3.org/ns/wsdl20-rdf#>"
		+ "SELECT ?endpoint WHERE {sm:"
		+ serviceName + " wsdl:endpoint ?endpoint . }";
	TupleQuery tupleQuery = con.prepareTupleQuery(queryString);
	TupleQueryResult result = tupleQuery.evaluate();
	if (result.hasNext()) {
	    return new URI(result.next().getValue("endpoint").stringValue());
	}
	throw new NoSuchElementException("No template found for given service");
    }
}
