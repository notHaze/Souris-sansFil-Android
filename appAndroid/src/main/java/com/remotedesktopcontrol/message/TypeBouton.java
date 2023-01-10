package com.remotedesktopcontrol.message;

public enum TypeBouton {
    BOUTON_1 ("Click gauche"),
    BOUTON_2 ("Click molette"),
    BOUTON_3 ("Click droit"),
    SCROLL("Scroll");

    private String typeDeBouton;

    private TypeBouton(String typeDeBouton) {
        this.typeDeBouton = typeDeBouton;
    }

    public String getTypeDeBouton() {
        return this.typeDeBouton;
    }
}
