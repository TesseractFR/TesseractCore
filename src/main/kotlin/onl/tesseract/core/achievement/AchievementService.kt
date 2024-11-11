package onl.tesseract.core.achievement

class AchievementService(private val repository: AchievementRepository) {

    private val tutoriel: List<Achievement?> = listOf(
        getByName("TUTO_CLIC_BOUSSOLE"),
        getByName("TUTO_INVOQUE_AILE_MENU"),
        getByName(
            "TUTO_PROPULSION_SYNERGIQUE"
        ),
        getByName("TUTO_BOOST_VOL"),
        getByName("TUTO_WARP_ANTERRA"),
        getByName("TUTO_SELECT_METIER"),
        getByName("TUTO_TP_SPAWN"),
        getByName("TUTO_VLIST"),
        getByName("TUTO_VSPAWN"),
        getByName(
            "TUTO_MENU_SEARCH_OBJECT"
        ),
        getByName(
            "TUTO_MENU_SEARCH_PARCELLE"
        )
    )

    fun getByName(name: String): Achievement? {
        return repository.getByName(name)
    }
}
