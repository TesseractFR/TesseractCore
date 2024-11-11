package onl.tesseract.core.title

enum class StaffTitle(val nameM: String, val nameF: String) {
    ADMINISTRATEUR("Administrateur", "Administratrice"),
    MODERATEUR("Modérateur", "Modératrice"),
    MEDIATEUR("Médiateur", "Médiatrice"),
    ANIMATEUR("Animateur", "Animatrice"),
    GUIDE("Guide", "Guide"),
    ;

    val title: Title get() = Title(name, nameM, nameF)
}