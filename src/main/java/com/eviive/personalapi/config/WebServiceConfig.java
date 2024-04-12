package com.eviive.personalapi.config;

import com.eviive.personalapi.properties.PortfolioPropertiesConfig;
import com.eviive.personalapi.service.webservice.PortfolioWebService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebServiceConfig {

    private <E> E buildWebClient(final WebClient webClient, final Class<E> webServiceInterface) {
        return HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient))
            .build()
            .createClient(webServiceInterface);
    }

    @Bean
    public PortfolioWebService portfolioWebService(
        final PortfolioPropertiesConfig portfolioPropertiesConfig
    ) {
        final WebClient webClient = WebClient.builder()
            .baseUrl(portfolioPropertiesConfig.api().url())
            .build();

        return buildWebClient(webClient, PortfolioWebService.class);
    }

}
