package eu.profinit.hackaton2019.CSOBforehand.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.profinit.hackaton2019.CSOBforehand.model.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessagingService implements CommandLineRunner {
    @Autowired
    private InitService initService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private ObjectMapper objectMapper;

    public void prepareFirstGeneration() throws JsonProcessingException {
        List<Cell> firstGen = boardService.calculateNeighbours(initService.generateFirstGen()).stream()
                                          .flatMap(List::stream)
                                          .collect(Collectors.toList());

        System.out.println(objectMapper.writeValueAsString(firstGen));
    }

    @Override
    public void run(final String... args) throws Exception {
        prepareFirstGeneration();
    }
}
