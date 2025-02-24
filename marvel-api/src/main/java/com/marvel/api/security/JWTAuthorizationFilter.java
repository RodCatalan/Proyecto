package com.marvel.api.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.marvel.api.constants.SearchMarvelConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthorizationFilter extends OncePerRequestFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(JWTAuthorizationFilter.class);
	private String secretKey; 
	
	public JWTAuthorizationFilter(String secretKey) {
		
		this.secretKey = secretKey;
		
	}

	/**Método que se invoca una vez por cada solicitud HTTP para realizar la autorización basada en JWT.
	 *
	 * @param request  La solicitud HTTP. Puedes usar esto para obtener información sobre la solicitud,
	 *                 como los encabezados, que pueden incluir el token JWT.
	 * @param response La respuesta HTTP. Puedes usar esto para modificar la respuesta que se enviará al cliente.
	 * @param chain    La cadena de filtros. Después de hacer tu lógica de autorización, debes invocar
	 *                 chain.doFilter(request, response) para pasar la solicitud y la respuesta al siguiente filtro en la cadena.
	 *
	 * @throws ServletException Si ocurre un error durante el procesamiento de la solicitud. Esto generalmente indicará
	 *                          un error en tu código, no un error en la solicitud del cliente.
	 * @throws IOException      Si ocurre un error de entrada/salida. Esto generalmente indicará un problema de red o un error
	 *                          al leer o escribir datos en el cuerpo de la solicitud o respuesta.
	 * @autor rgonzalezc.
     * @since 23/02/2025.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		
		try {
			
			 String path = request.getRequestURI();
			 boolean validarToken = path.contains("/getDataBitacora");  
			
			logger.debug("Request URI: " + path);
			logger.debug("Validar Token: " + validarToken);
			
			if (validarToken && existeJWTToken(request)) {
				
				Claims claims = validateToken(request);
				
				if (claims.get(SearchMarvelConstants.KEY_AUTHORITIES) != null) {
					
					setUpSpringAuthentication(claims);
					
				} else {
					
					SecurityContextHolder.clearContext();
					
				}
				
			} else {
				
				SecurityContextHolder.clearContext();
				
			}
			
			chain.doFilter(request, response);
			
		} catch (Exception e) {
			
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
			
		}
		
	}	

	/**Método para validar el token JWT de la solicitud HTTP.
	 *
	 * @param request La solicitud HTTP. Este método extraerá el token JWT del encabezado de autorización de esta solicitud.
	 *
	 * @return Claims Objeto que contiene todas las declaraciones (claims) del token JWT. 
	 *                Puedes usar esto para obtener información sobre el usuario y otras datos incluidos en el token.
	 *                Si el token no es válido, este método puede devolver null o lanzar una excepción, dependiendo de tu implementación.
	 *
	 * @throws JwtException Si el token no es válido, este método puede lanzar una excepción. 
	 *                      La excepción exacta dependerá de la biblioteca que estés utilizando para manejar los tokens JWT.
	 * @autor rgonzalezc.
     * @since 23/02/2025.
	 */
	private Claims validateToken(HttpServletRequest request) {
		String jwtToken = request.getHeader(SearchMarvelConstants.HEADER).replace(SearchMarvelConstants.PREFIX, "");
       	return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(jwtToken).getBody();
	}

	/**Método para establecer la autenticación en el contexto de seguridad de Spring a partir de las declaraciones (claims) de un token JWT.
	 *
	 * @param claims Objeto que contiene todas las declaraciones del token JWT. 
	 *               Este método utilizará estas declaraciones para crear un objeto de autenticación y establecerlo en el contexto de seguridad de Spring.
	 *
	 * Este método no devuelve ningún valor. En su lugar, modifica el contexto de seguridad de Spring, que es global para la solicitud actual.
	 * Si este método se invoca con éxito, los métodos posteriores que necesiten información de autenticación podrán obtenerla del contexto de seguridad de Spring.
	 * @autor rgonzalezc.
     * @since 23/02/2025.
	 */
	private void setUpSpringAuthentication(Claims claims) {
		
		List<String> authorities = (List) claims.get(SearchMarvelConstants.KEY_AUTHORITIES);

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
				authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
		SecurityContextHolder.getContext().setAuthentication(auth);

	}

	/**Método para verificar si el token JWT existe en la solicitud HTTP.
	 * 
	 * @param request  La solicitud HTTP. Puedes usar esto para obtener información sobre la solicitud,
	 *                 como los encabezados, que pueden incluir el token JWT.
	 * @return boolean: true si el token JWT existe en la solicitud HTTP, de lo contrario false.
	 * @autor rgonzalezc.
     * @since 23/02/2025.
	 */
	private boolean existeJWTToken(HttpServletRequest request) {
		
		String authenticationHeader = request.getHeader(SearchMarvelConstants.HEADER);
		logger.debug("Authentication Header: " + authenticationHeader);
		return (authenticationHeader != null && authenticationHeader.startsWith(SearchMarvelConstants.PREFIX));
	}

}