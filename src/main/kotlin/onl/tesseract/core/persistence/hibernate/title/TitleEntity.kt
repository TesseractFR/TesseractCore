package onl.tesseract.core.persistence.hibernate.title

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import onl.tesseract.core.title.Title

@Entity
@Table(name = "t_title")
class TitleEntity(
    @Id
    @Column(name = "name")
    var id: String,
    @Column(name = "text_m")
    var nameM: String,
    @Column(name = "text_f")
    var nameF: String,
) {
    fun toModel(): Title {
        return Title(id, nameM, nameF)
    }

    companion object {
        fun fromModel(model: Title): TitleEntity {
            return TitleEntity(model.id, model.nameM, model.nameF)
        }
    }
}