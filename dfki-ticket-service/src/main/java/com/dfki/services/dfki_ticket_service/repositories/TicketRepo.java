package com.dfki.services.dfki_ticket_service.repositories;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

public class TicketRepo {
    private Repository repository;

    public TicketRepo() {
        this.repository = new SailRepository(new MemoryStore());
        this.repository.init();
    }

    public void save(final Model model) {
        RepositoryConnection connection = repository.getConnection();
        connection.begin();
        connection.add(model);
        connection.commit();
        connection.close();
    }

    public String getValue(final String property) {
        String queryString = "prefix sm: <http://www.smartmaas.de/sm-ns#> "
                + "prefix gr: 	<http://purl.org/goodrelations/v1#> "
                + "SELECT ?p WHERE {sm:offer1 gr:" + property + " ?p }";
        TupleQuery query = repository.getConnection().prepareTupleQuery(queryString);
        TupleQueryResult result = query.evaluate();
        String string = "";
        if (result.hasNext()) {
            return result.next().getValue("p").stringValue();
        }
        return string;

    }

    public String getObject(final String subjectPrefix, final String subject,
                            final String predicatePrefix, final String predicate) {
        String string = "";
        try {
            String queryString = "prefix sm: <http://www.smartmaas.de/sm-ns#> "
                    + "prefix gr: 	<http://purl.org/goodrelations/v1#> "
                    + "prefix tio: 	<http://purl.org/tio/ns#> "
                    + "SELECT ?p WHERE {" + subjectPrefix + ":" + subject + " "
                    + predicatePrefix + ":" + predicate + " ?p }";
            TupleQuery query = repository.getConnection().prepareTupleQuery(queryString);
            TupleQueryResult result = query.evaluate();
            if (result.hasNext()) {
                return result.next().getValue("p").stringValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;

    }

    public String getType(final String subjectPrefix, final String subject, final String typePrefix) {
        String string = "";
        try {
            String queryString = "prefix sm: <http://www.smartmaas.de/sm-ns#> "
                    + "prefix gr: 	<http://purl.org/goodrelations/v1#> "
                    + "prefix tio: 	<http://purl.org/tio/ns#> "
                    + "prefix rdf: 	<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                    + "SELECT ?type WHERE {" + subjectPrefix + ":" + subject.trim() + " a " + "?type .}";
            TupleQuery query = repository.getConnection().prepareTupleQuery(queryString);
            TupleQueryResult result = query.evaluate();
            if (result.hasNext()) {
                return result.next().getValue("type").stringValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;

    }


}
