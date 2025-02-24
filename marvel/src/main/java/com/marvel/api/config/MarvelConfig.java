package com.marvel.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Clase para la  crear y configurar el WebClient.
 * @author rgonzalezc.
 * @since 22/02/2025
 */
@Configuration
public class MarvelConfig {

	@Value("${url.base.api}")
	private String baseUrl;
	
	/**Método que configura y crea un objeto WebClient, que se puede usar para realizar solicitudes HTTP.
	 * @return Objeto WebClient configurado con una cabecera por defecto que acepta JSON y una URL base establecida.
	 * @author rgonzalezc.
     * @since 22/02/2025.
	 */
    @Bean
    public WebClient webClient() {
    	
        return WebClient.builder()
                		.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                		.baseUrl(baseUrl)
                		.build();

    }
	
}