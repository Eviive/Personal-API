package com.eviive.personalapi.controller;

import com.eviive.personalapi.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("portfolio")
@RequiredArgsConstructor
@Tag(name = "PortfolioController")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PostMapping("revalidate")
    @Operation(
        summary = "Revalidate the portfolio",
        responses = @ApiResponse(responseCode = "204", description = "No Content")
    )
    public ResponseEntity<Void> revalidate() {
        portfolioService.revalidate();
        return ResponseEntity.noContent().build();
    }

}
