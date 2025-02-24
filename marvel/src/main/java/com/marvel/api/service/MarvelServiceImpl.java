package com.marvel.api.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

import javax.xml.bind.DatatypeConverter;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.marvel.api.utils.MarvelConstants;
import com.marvel.api.vo.MarvelResponseVO;

/**Clase para el consumo de la API Marvel.
  * @author rgonzalezc.
  * @since 22/02/2025
*/

@Service
public class MarvelServiceImpl implements MarvelService {
	
	@Value("${marvel.api.public-key}")
	private String publicKeyApi;
	
	@Value("${marvel.api.private-key}")
	private String privateKeyApi;
	
	@Autowired
	private WebClient webClient;

	@Override
	public ResponseEntity<Object> getCharacters(Integer characterId) throws NoSuchAlgorithmException {
		
		Instant timeStamp = Instant.now();
		String hash = getHash(timeStamp);
		String path = getPath(characterId);
		String jsonResponse = webClient.get()
				                .uri(uriBuilder -> uriBuilder
					                .path(path)
					                .queryParam(MarvelConstants.QUERY_PARAM_TIME_STAMP, timeStamp)
					                .queryParam(MarvelConstants.QUERY_PARAM_API_KEY, publicKeyApi)
					                .queryParam(MarvelConstants.QUERY_PARAM_HASH, hash)
					                .build())
				                .retrieve()
				                .bodyToMono(String.class)
				                .block();
	    
		return validResponseApi(jsonResponse);
		
	}
	
	/**Método que crea un hash para el consumo de la API.
	 * @param timeStamp Instant: Marca de tiempo para crear la cadena hash, esta misma marca se envía como parámetro a la API (ts).
	 * @throws NoSuchAlgorithmException: Excepción al utilizar algoritmo de cifrado que no está disponible.
	 * @return hash String: Cadena que contiene el valor del hash resultado de timeStamp + privateKeyApi + publicKeyApi.
	 * @author rgonzalezc.
  	 * @since 22/02/2025
	*/
	private String getHash(Instant timeStamp) throws NoSuchAlgorithmException {
		
		StringBuilder parametros = new StringBuilder();
		
		parametros.append(timeStamp);
		parametros.append(privateKeyApi);
		parametros.append(publicKeyApi);
		
		MessageDigest md = MessageDigest.getInstance(MarvelConstants.ALGORITHM_MD5);
		byte[] hash = md.digest(parametros.toString().getBytes());
		
		return DatatypeConverter.printHexBinary(hash).toLowerCase();
		
	} 
	
	/**Método que crea el path para consumir la API.
	 * @param characterId Integer: ID del Character a recuperar, solo si se recibe este parámetro se realizará la búsqueda filtrada,
	 * de lo contrario recuperará el catálogo completo.
	 * @return path String: Cadena que contiene el path para consumir la API.
	 * @author rgonzalezc.
	 * @since 22/02/2025
	*/
	private String getPath(Integer characterId) {
		
		StringBuilder path = new StringBuilder();
	
		path.append(MarvelConstants.PATH_CHARACTERS);
		
		if(characterId != null && characterId > 0) {
			
			path.append("/");
			path.append(characterId.toString());
			
		}
		
		return path.toString();
		
	}

	/**
     * Metodo en el cual se agregan los Headers al ResponseEntity
     * para asignar el tipo de objeto de respuesta asi como el encoding que este tendra
     * al leer la informacion que se obtiene al consultar el API de Marvel
	 * @author rgonzalezc.
  	 * @since 23/02/2025
	*/
    private ResponseEntity<Object> validResponseApi(String responseApi){

        MarvelResponseVO marvelResponseVO = new MarvelResponseVO();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8.toString());

        if(responseApi.equals("404")){

            marvelResponseVO.setStatusCode(Integer.valueOf(responseApi));
            marvelResponseVO.setMensaje(MarvelConstants.MENSAJE_RESPONSE_NO_ENCONTRADO);
            return new ResponseEntity<>(marvelResponseVO, httpHeaders, HttpStatus.NOT_FOUND);

		}else if(responseApi.equals("500")){

            marvelResponseVO.setStatusCode(Integer.valueOf(responseApi));
            marvelResponseVO.setMensaje(MarvelConstants.MENSAJE_RESPONSE_ERROR);
            return new ResponseEntity<>(marvelResponseVO, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);

		}else{

			JSONObject jsonResponseApiObject = new JSONObject(responseApi);
			marvelResponseVO.setStatusCode(Integer.valueOf(jsonResponseApiObject.get("code").toString()));
			marvelResponseVO.setCharacterData(jsonResponseApiObject.toString());
			marvelResponseVO.setMensaje(MarvelConstants.MENSAJE_RESPONSE_OK);

		}

        return new ResponseEntity<>(marvelResponseVO, httpHeaders, HttpStatus.OK);
    }

}