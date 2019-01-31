package de.dfki.asr.smartticket.service;

import de.dfki.asr.smartticket.data.InMemoryRepo;
import org.eclipse.rdf4j.model.Model;

public class BookingProcess {

    private final InMemoryRepo repo;

    public BookingProcess() {
	repo = new InMemoryRepo();
    }

    public void writeRequestToRepo(final Model input) {
	repo.write(input);
    }
}
