package com.lt.reflection

/**
 * creator: lt  2023/11/21  lt.dygzs@qq.com
 * effect : Modifying to a class or file indicates that the class requires virtual reflection, which will generate the corresponding constructor virtual reflection code
 *          修饰到类或文件内表示此类需要虚拟反射,会生成对应的构造函数虚拟反射代码
 * warning: Even outside the configured package name, corresponding code can be generated
 *          即使在配置的包名之外也可以生成对应代码
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.SOURCE)
annotation class ReflectionObject