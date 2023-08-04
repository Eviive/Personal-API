package com.eviive.personalapi.service.webservice;

import com.eviive.personalapi.dto.RevalidateRequestDTO;
import com.eviive.personalapi.dto.RevalidateResponseDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface PortfolioWebService {

    @PostExchange("/revalidate")
    RevalidateResponseDTO revalidate(@RequestBody @Valid RevalidateRequestDTO revalidateRequestDTO);

}
