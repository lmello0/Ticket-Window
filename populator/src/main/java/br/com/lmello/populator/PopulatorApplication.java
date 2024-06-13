package br.com.lmello.populator;

import br.com.lmello.populator.service.CommandLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
@RequiredArgsConstructor
public class PopulatorApplication implements CommandLineRunner {

    private final CommandLineService commandLineService;

    public static void main(String[] args) {
        SpringApplication.run(PopulatorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Map<String, String> parsedArgs = commandLineService.parseArguments(args);

        commandLineService.validateArguments(parsedArgs);

        String url = parsedArgs.get("--url");
        int iterations = Integer.parseInt(parsedArgs.get("--iterations"));
        int delay = Integer.parseInt(parsedArgs.get("--delay"));

        Map<String, Integer> results = commandLineService.run(url, iterations, delay);

        System.out.println("--- RESULTS ---\n");
        for (Map.Entry<String, Integer> entry : results.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("\n---------------");
    }
}
