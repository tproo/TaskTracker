package com.gochachko.pavel.tasktracker.logic.destributor

enum class DataType(val value : String) {
  TASK("task"),
  PROJECT("project"),
  USER("user"),
  OTHER("other")
}

fun convertStringToDataType(value : String) : DataType {
  val types = DataType.values()
  types.forEach { type ->
    if (type.value == value) {
      return type
    }
  }
  return DataType.OTHER
}