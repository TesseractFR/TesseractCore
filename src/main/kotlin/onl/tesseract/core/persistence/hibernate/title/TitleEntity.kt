package onl.tesseract.core.persistence.hibernate.title

import jakarta.persistence.Entity
import jakarta.persistence.Id
import onl.tesseract.core.title.Title

@Entity
class TitleEntity {
    @Id
    var id: String? = null
    var nameM: String? = null
    var nameF: String? = null

    fun toModel(): Title {
        return Title(id, nameM, nameF)
    }

    companion object {
        fun fromModel(model: Title): TitleEntity {
            return TitleEntity(model.id, model.nameM, model.nameF)
        }
    }
}