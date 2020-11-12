package de.dfki.asr.smartticket.data;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.SimpleStatement;
import org.eclipse.rdf4j.query.MalformedQueryException;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
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
	try {
	    repo.getConnection().add(statements);
	} catch (RepositoryException ex) {
	    LOG.error("Failed to access triplestore: " + ex.getMessage());
	}
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

    public String getValue(final String property) {
	try {
	    String queryString = "prefix sm: <http://www.smartmaas.de/sm-ns#> "
		    + "prefix gr: 	<http://purl.org/goodrelations/v1#> "
		    + "SELECT ?p WHERE {?o " + property + " ?p. "
		    + "			?o a gr:offering . }";
	    TupleQuery query = repo.getConnection().prepareTupleQuery(queryString);
	    TupleQueryResult result = query.evaluate();
	    StringBuilder buf = new StringBuilder();
	    if (result.hasNext()) {
		return result.next().getValue("p").stringValue();
	    }
	    return buf.toString();
	} catch (RepositoryException ex) {
	    LOG.error("Error querying the repo for property " + property + ": " + ex.getMessage());
	} catch (MalformedQueryException ex) {
	    LOG.error("Malformed query while getting data: " + ex.getMessage());
	}
	return "No results found";
    }
}
