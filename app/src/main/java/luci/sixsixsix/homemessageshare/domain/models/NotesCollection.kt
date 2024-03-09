package luci.sixsixsix.homemessageshare.domain.models

data class NotesCollection(
    val id: String,
    val dateCreated: String,
    val dateModified: String,
    val colour: String,
    val tags: List<String> = listOf(),
    val serverAddress: String,
    val appTheme: String
)
