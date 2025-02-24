package com.marvel.api.service;

import java.security.NoSuchAlgorithmException;

import org.springframework.http.ResponseEntity;

/**Interface de los methods para el consumo de la API Marvel.
 * @author rgonzalezc.
 * @since 22/02/2025
 */

public interface MarvelService {

	/**Método que obtiene los Characters al consumir API de Marvel.
	 * @param characterId Integer: ID del Character a recuperar, solo si se recibe este parámetro se realizará la búsqueda filtrada,
	 * de lo contrario recuperará el catálogo completo.
	 * @return String: Cadena que contiene el JSON obtenido al consumir la API.
	 * @throws NoSuchAlgorithmException: Excepción al utilizar algoritmo de cifrado que no está disponible.
	 * @author rgonzalezc.
     * @since 22/02/2025
	 */
	ResponseEntity<Object> getCharacters(Integer characterId) throws NoSuchAlgorithmException;
	
}