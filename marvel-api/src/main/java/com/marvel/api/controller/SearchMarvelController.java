package com.marvel.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.marvel.api.repository.BitacoraAccesoMarvelEntity;
import com.marvel.api.security.JwtUtil;
import com.marvel.api.service.SearchCharacterService;

@RestController
@RequestMapping(value = "api/marvel")
public class SearchMarvelController {

	@Autowired
    private SearchCharacterService searchCharacterService;

	@Autowired
	private JwtUtil jwtUtil;

    /**Método que obtiene los Characters al consumir API de Marvel.
	 * @param characterId Integer: ID del Character a recuperar, solo si se recibe este parámetro se realizará la búsqueda filtrada,
	 * de lo contrario recuperará el catálogo completo.
	 * @return responseEntity ResponseEntity<Object>: Objeto que contiene la respuesta exitosa con formato.
	 * @author rgonzalezc.
     * @since 22/02/2025.
	 */

	@GetMapping(value = "/getCharacters", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String getCharacters(@RequestParam(value = "characterId", required = false) Integer characterId){
		 
		return searchCharacterService.getCharacters(characterId);
    }

	/**Este método se utiliza para obtener los datos de la bitácora de acceso a la API de Marvel.
	 * @return Una lista de entidades BitacoraAccesoMarvelEntity que representan los registros de la bitácora de acceso.
	 * @author rgonzalezc.
     * @since 23/02/2025.
	 */
	@GetMapping(value = "/getDataBitacora", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<BitacoraAccesoMarvelEntity> getDataBitacora(){
		 
		return searchCharacterService.getDataBitacora();
		 
	}

	/**Este método se utiliza para validar las credenciales de un usuario y generar un token JWT si las credenciales son válidas.
	 *
	 * @param username El nombre de usuario que se va a validar.
	 * @param pwd La contraseña que se va a validar.
	 * @return Un token JWT si las credenciales son válidas, o un mensaje de error si no lo son.
	 */
	@PostMapping(value = "/access", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String access(@RequestParam("user") String username, @RequestParam("password") String pwd) {
		
		return jwtUtil.validateCredentials(username, pwd);
		
	}
		 
}
