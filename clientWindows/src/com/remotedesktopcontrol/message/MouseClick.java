package com.remotedesktopcontrol.message;

public class MouseClick extends Message{

	private static final long serialVersionUID = 1774505589961566543L;
	private TypeBouton bouton;
	private int value;

	public MouseClick(TypeBouton typedeBouton, int value) {
		this.bouton = typedeBouton;
		this.value = value;
	}

	public MouseClick(TypeBouton typedeBouton) {
		this(typedeBouton, 0);
	}

	public int getValue() {
		return value;
	}

	public TypeBouton getTypeBouton() {
		return bouton;
	}

	public String toString() {
		return "[ Bouton : "+this.bouton.getTypeDeBouton()+", value : "+value+"]";
	}
}
