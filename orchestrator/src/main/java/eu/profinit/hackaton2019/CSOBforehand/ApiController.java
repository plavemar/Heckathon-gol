package eu.profinit.hackaton2019.CSOBforehand;

import eu.profinit.hackaton2019.CSOBforehand.model.InitBoard;
import eu.profinit.hackaton2019.CSOBforehand.services.CollectService;
import eu.profinit.hackaton2019.CSOBforehand.services.InitService;
import eu.profinit.hackaton2019.CSOBforehand.services.MessagingService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class ApiController {

    private final MessagingService messagingService;
    private final InitService initService;
    private final CollectService collectService;

    @GetMapping
    @SneakyThrows
    public String get(@RequestParam(name = "board", defaultValue = "RANDOM") String board) {
        messagingService.sendFirstGeneration(collectService, initService, InitBoard.valueOf(board));

        return "index";
    }
}
