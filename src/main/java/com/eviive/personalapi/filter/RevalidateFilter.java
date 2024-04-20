package com.eviive.personalapi.filter;

import com.eviive.personalapi.dto.RevalidateRequestDTO;
import com.eviive.personalapi.properties.PortfolioPropertiesConfig;
import com.eviive.personalapi.service.webservice.PortfolioWebService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RevalidateFilter extends OncePerRequestFilter {

    private final PortfolioWebService portfolioWebService;

    private final PortfolioPropertiesConfig portfolioPropertiesConfig;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void doFilterInternal(
        @NonNull final HttpServletRequest req,
        @NonNull final HttpServletResponse res,
        @NonNull final FilterChain filterChain
    )
        throws ServletException, IOException {
        filterChain.doFilter(req, res);

        if (!shouldFilter(req, res)) {
            return;
        }

        final RevalidateRequestDTO revalidateRequest = new RevalidateRequestDTO(
            portfolioPropertiesConfig.api().secret(),
            "/"
        );

        portfolioWebService
            .revalidate(revalidateRequest)
            .doOnError(e -> log.error("Failed to revalidate portfolio", e))
            .subscribe(revalidateResponse -> {
                final String timestamp = dateTimeFormatter.format(revalidateResponse.timestamp());

                if (revalidateResponse.revalidated()) {
                    log.info(
                        "Revalidated portfolio: {}",
                        timestamp
                    );
                } else {
                    log.warn(
                        "Failed to revalidate portfolio ({})",
                        timestamp
                    );
                }
            });
    }

    private boolean shouldFilter(final HttpServletRequest req, final HttpServletResponse res) {
        if (!List.of("POST", "PUT", "PATCH", "DELETE").contains(req.getMethod())) {
            return false;
        }

        if (!req.getRequestURI().startsWith("/project") &&
            !req.getRequestURI().startsWith("/skill")) {
            return false;
        }

        return HttpStatus.valueOf(res.getStatus()).is2xxSuccessful();
    }

}
