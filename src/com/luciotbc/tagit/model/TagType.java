package com.luciotbc.tagit.model;

public enum TagType {
	
	I_GIVE_UP("Desisto"),
	LOOCKS_FINE_TO_ME("Para mim est� bom"),
	WHERE_I_AM("Onde estou?"),
	OOPS("Epa!"),
	I_CANT_DO_IT_THISWAY("Assim n�o d�!"),
	WHAT_HAPPENED("U�, o que houve?"), 
	WHERE_IS_IT("Cad�?"),
	WHAT_IS_THIS("O que � isso?"),
	HELP("Socorro!"),
	WY_DOESENT_IT("Por que n�o funciona"),
	WHAT_NOW("E agora?"),
	THANKS_BUT_NO("N�o, obrigado!"),
	I_CAN_DO_OTHERWIDE("Vai de outro jeito");
	
	private final String tag;
	
	private TagType(String tag){
		this.tag = tag;
	}

	public String toString() {
		return tag;
	}
	
}
