package com.deskflow.api.desk;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/desks")
public class DeskController {

    private final DeskRepository deskRepository;

    public DeskController(DeskRepository deskRepository) {
        this.deskRepository = deskRepository;
    }

    @GetMapping
    public List<DeskResponse> listDesks(
            @RequestParam(required = false) Integer floor,
            @RequestParam(required = false) Boolean hasMonitor) {
        // Route to the narrowest repository query based on the filters provided.
        List<Desk> desks = findDesks(floor, hasMonitor);

        // Map entities to response DTOs so the API shape stays stable.
        return desks.stream()
                .map(DeskResponse::from)
                .toList();
    }

    private List<Desk> findDesks(Integer floor, Boolean hasMonitor) {
        if (floor != null && hasMonitor != null) {
            return deskRepository.findAllByFloorAndHasMonitor(floor, hasMonitor);
        }

        if (floor != null) {
            return deskRepository.findAllByFloor(floor);
        }

        if (hasMonitor != null) {
            return deskRepository.findAllByHasMonitor(hasMonitor);
        }

        return deskRepository.findAll();
    }
}