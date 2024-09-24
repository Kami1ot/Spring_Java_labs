package com.example.Barber_lab6.Controllers;


import com.example.Barber_lab6.Models.Clients;
import com.example.Barber_lab6.Repository.ClientsRepository;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {
    private final ClientsRepository ClientsRepository;

    public MainController(ClientsRepository ClientsRepository) {
        this.ClientsRepository = ClientsRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Главная Страница");
        return "greeting";
    }
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Страница входа");
        return "login";
    }
    @GetMapping("/support")
    public String support(Model model) {
        model.addAttribute("title", "Поддержка");
        return "support";
    }
    @GetMapping("/Clients")
    public String Plays(Model model) {
        Iterable<Clients> Client = ClientsRepository.findAll();
        model.addAttribute("title","Страница сеансов");
        model.addAttribute("Clients", Client);
        return "Clients";
    }
    @GetMapping("/addClient")
    public String addPlay(Model model) {
        model.addAttribute("title","Страница добавления клиента");
        return "addClient";
    }

    @GetMapping("/Clients/{id}")
    public String updatePlay(@PathVariable long id, Model model) {
        if (!ClientsRepository.existsById(id)){
            return "redirect:/Clients";
        }
        Optional<Clients> Client = ClientsRepository.findById(id);
        ArrayList<Clients> res = new ArrayList<>();
        Client.ifPresent(res::add);
        model.addAttribute("Clients", res);
        model.addAttribute("title", "Страница редактирования");
        return "ClientDetails";

    }

    @GetMapping("/Clients/filter")
    public String searchPlays(
            @RequestParam(required = false) String initials,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start_date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end_date,
            @RequestParam(required = false) String service_name,
            @RequestParam(required = false, defaultValue = "asc") String sort,
            Model model) {

        Sort.Direction sortDirection = sort.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sortBy = Sort.by(sortDirection, "sessionDateTime");

        LocalDateTime startDateTime = start_date != null ? start_date.atStartOfDay() : null;
        LocalDateTime endDateTime = end_date != null ? end_date.atTime(23, 59, 59) : null;

        List<Clients> Clients;

        if (initials != null || start_date != null || end_date != null|| service_name != null) {
            Clients = ClientsRepository.findByParams(initials, startDateTime, endDateTime, service_name, sortBy);
        } else {
            Clients = ClientsRepository.findAll(sortBy);
        }

        model.addAttribute("Clients", Clients);
        return "Clients";
    }

    @GetMapping("/Clients/stats")
    public String stats(Model model) {
        List<Object[]> stats = ClientsRepository.findClientIssueStats();

        List<String> dates = new ArrayList<>();
        List<Long> counts = new ArrayList<>();

        for (Object[] row : stats) {
            dates.add(row[0].toString());
            counts.add((Long) row[1]);
        }

        model.addAttribute("dates", dates);
        model.addAttribute("counts", counts);
        return "Client_stats";
    }



    @PreAuthorize("isAuthenticated()")
    @PostMapping("/addClient")
    public String addPlay(@RequestParam String initials,
                          @RequestParam LocalDateTime sessionDateTime,
                          @RequestParam String service_name,
                          @RequestParam String master_name) {
        Clients Client = new Clients(initials, sessionDateTime, service_name, master_name);
        ClientsRepository.save(Client);
        return "redirect:/Clients";

    }

    @PostMapping("/Clients/save")
    public String saveFiles(
            @RequestParam("id") long id,
            @RequestParam String initials,
            @RequestParam LocalDateTime sessionDateTime,
            @RequestParam String service_name,
            @RequestParam String master_name) {
        Clients Client = ClientsRepository.findById(id).orElseThrow();
        Client.setInitials(initials);
        Client.setSessionDateTime(sessionDateTime);
        Client.setService_name(service_name);
        Client.setMaster_name(master_name);
        ClientsRepository.save(Client);
        return "redirect:/Clients";

    }

    @PostMapping("/Clients/{id}/remove")
    public String removeClient(@PathVariable long id) {
        Clients Client = ClientsRepository.findById(id).orElseThrow();
        ClientsRepository.delete(Client);
        return "redirect:/Clients";
    }






}
