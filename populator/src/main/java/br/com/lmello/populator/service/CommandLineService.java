package br.com.lmello.populator.service;

import br.com.lmello.common.customer.CustomerDTO;
import br.com.lmello.common.customer.Operation;
import br.com.lmello.populator.dto.ReturnMessageDTO;
import br.com.lmello.populator.exceptions.InvalidArgumentException;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CommandLineService {

    private final RestClient restClient = RestClient.create();

    private final Faker faker = new Faker();

    private final Logger logger = LoggerFactory.getLogger(CommandLineService.class.getName());

    private final Gson gson = new Gson();

    private final Random rand = new Random();

    public Map<String, String> parseArguments(String... args) {
        Map<String, String> parsedArgs = new HashMap<>();

        for (int i = 0; i < args.length; i += 2) {
            if (args[i].startsWith("--")) {
                parsedArgs.put(args[i], args[i + 1]);
            }
        }

        return parsedArgs;
    }

    public void validateArguments(Map<String, String> parsedArgs) {
        for (Map.Entry<String, String> entry : parsedArgs.entrySet()) {
            if (entry.getKey().equals("--url")) {
                validateUrl(entry.getValue());
                continue;
            }

            if (entry.getKey().equals("--iterations") || entry.getKey().equals("--delay")) {
                validateNumber(entry.getValue());
                continue;
            }

            throw new InvalidArgumentException("Unrecognized argument: " + entry.getKey());
        }
    }

    public Map<String, Integer> run(String url, int iterations, int delay) throws InterruptedException {
        Map<String, Integer> results = new HashMap<>();

        logger.info("Starting {} iterations", iterations);

        for (int i = 0; i < iterations; i++) {
            try {
                if (rand.nextInt(0, 29) == 0) {
                    ReturnMessageDTO returnMessage = enqueue(url);
                    updateResults(results, returnMessage.message());
                    logger.info("Customer generated {} - {} iteration", returnMessage.message(), i + 1);
                    continue;
                }

                updateResults(results, "Skipped iterations");
                logger.info("Customer not generated, skipping {} iteration", i + 1);
            } catch (HttpClientErrorException.BadRequest e) {
                ReturnMessageDTO returnMessage = gson.fromJson(e.getResponseBodyAsString(), ReturnMessageDTO.class);
                updateResults(results, returnMessage.message());
                logger.info("{} - {} iteration", returnMessage.message(), i + 1);
            }

            double progress = (double) (i + 1) / iterations * 100;
            System.out.printf("[%d/%d] - %.2f%%\r", i + 1, iterations, progress);

            Thread.sleep(delay);
        }

        System.out.println();
        logger.info("Done!");

        return results;
    }

    private void updateResults(Map<String, Integer> results, String message) {
        results.merge(message, 1, Integer::sum);
    }

    public void validateNumber(String arg) {
        try {
            Integer.parseInt(arg);
        } catch (NumberFormatException ignored) {
            throw new InvalidArgumentException(arg + " is not a valid number");
        }
    }

    public void validateUrl(String arg) {
        try {
            new URL(arg).toURI();
        } catch (MalformedURLException | URISyntaxException ignored) {
            throw new InvalidArgumentException("This is not a valid URL");
        }
    }

    private ReturnMessageDTO enqueue(String url) {
        CustomerDTO customer = new CustomerDTO(
                faker.name().fullName(),
                Operation.randomOperation()
        );

        return restClient
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(customer)
                .retrieve()
                .body(ReturnMessageDTO.class);
    }
}
