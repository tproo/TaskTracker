package com.gochachko.pavel.tasktracker.logic.query

import com.gochachko.pavel.tasktracker.logic.destributor.DataType
import org.json.JSONObject

class QueryGenerator {

  fun forCreate(type : DataType, obj : JSONObject) : JSONObject {
    val query = JSONObject()

    query.put("command", "create ${type.value}")
    query.put("object", obj)

    return query
  }

  fun forGet(type : DataType, obj : JSONObject?) : JSONObject {
    val query = JSONObject()

    query.put("command", "get ${type.value}")

    val queryObject : JSONObject
    if (obj == null) {
      queryObject = JSONObject()
      queryObject.put("name", "name")
    } else {
      queryObject = obj
    }
    query.put("object", queryObject)

    return query
  }
}