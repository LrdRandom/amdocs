package com.saidinit.random.amdocs.excersice4.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saidinit.random.amdocs.excersice4.domain.Entity;
import com.saidinit.random.amdocs.excersice4.repositories.EntityRepository;

public class Excersice4Service {

	private static final ObjectMapper objectMapper = new ObjectMapper();
	// I assume I have all of this.
	private final EntityRepository repo = null;
	String pathToFile = "";

	/**
	 * This method has some advantages: 
	 * - First, you are streaming the read which
	 * will mean that you do not brake your JVM upon loading 2GB of a file 
	 * - Second, you will insert through a JPA repo (that hopefully is reactive) with a buffer.
	 */
	public void doAnEtlProcess() throws IOException, JsonMappingException, JsonProcessingException {
		try (Stream<String> stream = Files.lines(Paths.get(pathToFile))) {
			stream.forEach(l -> {
				Entity entity = null;
				try {
					entity = objectMapper.readValue(l, Entity.class);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					// meh, tempted to put a continue here, save whatever line could not be inserted
					// and move on
				}

				// the amounts of saves that you are doing are going to impact the database
				// negatively, that is why you have hibernate properties for batching and
				// ordering. There are other options that we could consider if this is not
				// efficient enough, such as controlling the bean that creations the connection
				// to whatever database we are using. Also, depending on the database (the
				// exercise expects an SQL database) there should be batch inserts as a command
				// on them, which would relieve the pressure on the code here. If the database
				// is postgres, h2 or SQL server there is the R2DBC project that will make all
				// insertions in reactive manner, probably increasing a lot of efficiency
				repo.save(entity);
			});
		}
	}

	/**
	 * On other notes, I do know of spring cloud data flow (added to the
	 * dependencies in this very pom). At it's core, this is an ETL problem and I do
	 * believe should be solved with an ETL program, such as Informatica or
	 * (specifically for Oracle, Golden Gate), anyway, there are a bunch of ETLs in
	 * the market, some of them are java or integrated with spring, some aren't.
	 * Finding a suitable ETL is not a new problem but it depends on the specifics
	 * of each organisation, you can check people asking for the same thing on SO as
	 * far as 10 years ago:
	 * https://stackoverflow.com/questions/4251336/java-etl-hard-to-find-a-suitable-one
	 * 
	 * Part of the problem is the overhead that exists in whatever JDBC you are
	 * going to use, that is unavoidable, you can increase JVM memory with -xms and
	 * -xmx, but there is so much that that can fix.
	 */
}
