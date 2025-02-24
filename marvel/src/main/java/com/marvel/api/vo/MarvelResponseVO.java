package com.marvel.api.vo;

import java.io.Serializable;

/**
 * Vo para las respuestas correctas.
 * @author rgonzalezc.
 * @since 22/02/2025
 */

public class MarvelResponseVO implements Serializable{
	
	private static final long serialVersionUID = 412721928275406722L;
	
	private String mensaje;
    private Integer statusCode;
    private String characterData;

    /**
	 * @return the mensaje
	 */
    public String getMensaje() {
        return mensaje;
    }

    /**
	 * @param mensaje the mensaje to set
	 */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
	 * @return the statusCode
	 */
    public Integer getStatusCode() {
        return statusCode;
    }

    /**
	 * @param statusCode the statusCode to set
	 */
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    /**
	 * @return the characterData
	 */
    public String getCharacterData() {
        return characterData;
    }

    /**
	 * @param characterData the characterData to set
	 */
    public void setCharacterData(String characterData) {
        this.characterData = characterData;
    }

}