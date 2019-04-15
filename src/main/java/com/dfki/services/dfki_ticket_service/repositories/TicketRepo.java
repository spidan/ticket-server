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
        try {
            this.repository = new SailRepository(new MemoryStore());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.repository.init();
    }

    public void save(Model model) {
        try (RepositoryConnection connection = repository.getConnection()) {

            connection.begin();
            connection.add(model);
            connection.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getValue(String property) {
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = "prefix sm: <http://www.smartmaas.de/sm-ns#> "
                    + "prefix gr: 	<http://purl.org/goodrelations/v1#> "
                    + "SELECT ?p WHERE {sm:offer1 gr:" + property + " ?p }";
            TupleQuery query = connection.prepareTupleQuery(queryString);
            TupleQueryResult result = query.evaluate();
            StringBuilder stringBuilder = new StringBuilder();
            if (result.hasNext()) {
                return result.next().getValue("p").stringValue();
            }
            return stringBuilder.toString();
        }
    }

    public String getObject(String subjectPrefix, String subject, String predicatePrefix, String predicate) {
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = "prefix sm: <http://www.smartmaas.de/sm-ns#> "
                    + "prefix gr: 	<http://purl.org/goodrelations/v1#> "
                    + "prefix tio: 	<http://purl.org/tio/ns#> "
                    + "SELECT ?p WHERE {" + subjectPrefix + ":" + subject + " " + predicatePrefix + ":" + predicate + " ?p }";
            TupleQuery query = connection.prepareTupleQuery(queryString);
            TupleQueryResult result = query.evaluate();
            StringBuilder stringBuilder = new StringBuilder();
            if (result.hasNext()) {
                return result.next().getValue("p").stringValue();
            }
            return stringBuilder.toString();
        }
    }

    public String getType(String subjectPrefix, String subject, String typePrefix) {
        try (RepositoryConnection connection = repository.getConnection()) {
            String queryString = "prefix sm: <http://www.smartmaas.de/sm-ns#> "
                    + "prefix gr: 	<http://purl.org/goodrelations/v1#> "
                    + "prefix tio: 	<http://purl.org/tio/ns#> "
                    + "prefix rdf: 	<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                    + "SELECT ?type WHERE {" + subjectPrefix + ":" + subject.trim() + " a " + "?type .}";
            TupleQuery query = connection.prepareTupleQuery(queryString);
            TupleQueryResult result = query.evaluate();
            StringBuilder stringBuilder = new StringBuilder();
            if (result.hasNext()) {
                return result.next().getValue("type").stringValue();
            }
            return stringBuilder.toString();
        }
    }


}
