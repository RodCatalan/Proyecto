package com.marvel.api.security;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import com.marvel.api.constants.SearchMarvelConstants;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtilImpl implements JwtUtil{

    @Value(value = "${secret.Key.jwt}")
	private String secretKey;

    @Override
    public String validateCredentials(String username, String password){

        boolean usuarioEncontrado = (SearchMarvelConstants.ROOT.equals(username) && SearchMarvelConstants.ADMIN.equals(password));
		JSONObject response = new JSONObject();
		
		if(usuarioEncontrado) {
			
			String token = getJWTToken(username);
			
			response.put(SearchMarvelConstants.KEY_STATUS, HttpStatus.OK.value());
			response.put(SearchMarvelConstants.KEY_TOKEN, token);
			
		}else {
			
			response.put(SearchMarvelConstants.KEY_STATUS, HttpStatus.FORBIDDEN.value());
			
		}
		
		return response.toString();

    }

    /**Método para validar la existencia de un token JWT en la solicitud.
	 *
	 * @param request  La solicitud HTTP. Puedes usar esto para obtener información sobre la solicitud,
	 *                 como los encabezados, que pueden incluir el token JWT.
	 * @param response La respuesta HTTP. Puedes usar esto para modificar la respuesta que se enviará al cliente.
	 * @return boolean Si existe un token JWT en la solicitud, devuelve true, de lo contrario devuelve false.
	 * @autor regonzalezc.
     * @since 23/02/2025.
	 */
	private String getJWTToken(String user) {
		
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
		
		String token = Jwts
				.builder()
				.setId(SearchMarvelConstants.MARVEL_JWT)
				.setSubject(user)
				.claim(SearchMarvelConstants.AUTHORITIES,
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 60000))
				.signWith(SignatureAlgorithm.HS512, secretKey.getBytes()).compact();

		return SearchMarvelConstants.PREFIX + token;
		
	}

}
