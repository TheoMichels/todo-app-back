package com.todo.todoappback.trackingpoint;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/tracking-points")
public class TrackingPointController {

    private final TrackingPointService trackingPointService;

    public TrackingPointController(TrackingPointService trackingPointService) {
        this.trackingPointService = trackingPointService;
    }

    @GetMapping
    public List<TrackingPointResponse> list(@RequestParam(required = false) UUID sectionId) {
        return trackingPointService.find(sectionId).stream().map(TrackingPointResponse::from).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrackingPointResponse create(
            @Valid @org.springframework.web.bind.annotation.RequestBody TrackingPointCreateRequest request
    ) {
        return TrackingPointResponse.from(trackingPointService.create(request));
    }

    @PatchMapping("/{pointId}")
    @RequestBody(content = @Content(schema = @Schema(implementation = TrackingPointUpdateRequest.class)))
    public TrackingPointResponse update(
            @PathVariable UUID pointId,
            @org.springframework.web.bind.annotation.RequestBody Map<String, Object> patch
    ) {
        return TrackingPointResponse.from(trackingPointService.applyPatch(pointId, patch));
    }

    @DeleteMapping("/{pointId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID pointId) {
        trackingPointService.delete(pointId);
    }
}
