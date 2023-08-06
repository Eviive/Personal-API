package com.eviive.personalapi.filter;

import com.eviive.personalapi.dto.RevalidateRequestDTO;
import com.eviive.personalapi.dto.RevalidateResponseDTO;
import com.eviive.personalapi.service.webservice.PortfolioWebService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RevalidateFilter extends OncePerRequestFilter {

    private final PortfolioWebService portfolioWebService;

    @Value("${portfolio.api.secret}")
    private String portfolioApiSecret;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest req, @NonNull HttpServletResponse res, @NonNull FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(req, res);

        if (!List.of("POST", "PUT", "PATCH", "DELETE").contains(req.getMethod())) {
            return;
        }

        if (!HttpStatus.valueOf(res.getStatus()).is2xxSuccessful()) {
            return;
        }

        if (!req.getRequestURI().startsWith("/project") && !req.getRequestURI().startsWith("/skill")) {
            return;
        }

        try {
            RevalidateRequestDTO revalidateRequestDTO = RevalidateRequestDTO.of(portfolioApiSecret);
            RevalidateResponseDTO revalidateResponseDTO = portfolioWebService.revalidate(revalidateRequestDTO);
            log.info("Revalidated portfolio API: {}", revalidateResponseDTO);
        } catch (Exception e) {
            log.error("Error revalidating portfolio API", e);
        }
    }

}
