package $PACKAGE_NAME.data.local.converter

import androidx.room.TypeConverter

class StringListConverter {
    @TypeConverter
    fun toStringList(value: String): List<String> {
        return value.split(",")
    }

    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return list.joinToString(separator = ",")
    }
}
