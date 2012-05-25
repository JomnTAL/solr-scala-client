package jp.sf.amateras.solr.scala.sample

/**
 * Provides conversion methods for Map and case class.
 */
private[scala] object CaseClassMapper {

  /**
   * Converts the given Map[String, Any] to the case class.
   *
   */
  def map2class[T](map: Map[String, Any])(implicit m: scala.reflect.Manifest[T]): T = {
    val clazz = m.erasure.asInstanceOf[Class[T]]

    val constructor = clazz.getConstructors()(0)
    val paramTypes = constructor.getParameterTypes()
    val params = paramTypes.map { getDefaultValue(_).asInstanceOf[java.lang.Object] }

    println("*********************************************************")
    println(constructor)
    println("*********************************************************")

    val instance = constructor.newInstance(params: _*).asInstanceOf[T]

    map.foreach { case (key, value) =>
      try {
        val field = clazz.getDeclaredField(key)
        if(field != null){
          field.setAccessible(true)
          field.set(instance, value)
        }
      } catch {
        case ex: Exception => // Ignore
      }
    }

    instance
  }

  /**
   * Returns the default value for the given type.
   */
  private def getDefaultValue(clazz: Class[_]): Any = {
    if(clazz == classOf[Int] || clazz == java.lang.Integer.TYPE ||
        clazz == classOf[Short] || clazz == java.lang.Short.TYPE ||
        clazz == classOf[Byte] || clazz == java.lang.Byte.TYPE){
      0
    } else if(clazz == classOf[Double] || clazz == java.lang.Double.TYPE){
      0d
    } else if(clazz == classOf[Float] || clazz == java.lang.Float.TYPE){
      0f
    } else if(clazz == classOf[Long] || clazz == java.lang.Long.TYPE){
      0l
    } else if(clazz == classOf[Char] || clazz == java.lang.Character.TYPE){
      '\0'
    } else if(clazz == classOf[Boolean] || clazz == java.lang.Boolean.TYPE){
      false
    } else {
      null
    }
  }

}
