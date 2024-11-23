package onl.tesseract.core.title

class TitleService(private val repository: TitleRepository) {

    fun save(title: Title) {
        repository.save(title)
    }

    fun getById(id: String): Title {
        return repository.getById(id)?:throw NoSuchElementException("Title with id '$id' not found");
    }
}