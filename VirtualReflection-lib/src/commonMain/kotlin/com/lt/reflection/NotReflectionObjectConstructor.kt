package com.lt.reflection

/**
 * creator: lt  2023/11/21  lt.dygzs@qq.com
 * effect : Represents virtual reflection code that does not generate this constructor
 *          表示不生成此构造函数的虚拟反射代码
 * warning:
 */
@Target(AnnotationTarget.CONSTRUCTOR)
@Retention(AnnotationRetention.SOURCE)
annotation class NotReflectionObjectConstructor