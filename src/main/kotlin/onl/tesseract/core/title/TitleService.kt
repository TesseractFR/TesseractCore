package onl.tesseract.core.title

class TitleService(private val repository: TitleRepository) {

    fun save(title: Title) {
        repository.save(title)
    }
}