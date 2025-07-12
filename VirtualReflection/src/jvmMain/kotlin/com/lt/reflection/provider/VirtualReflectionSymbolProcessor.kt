package com.lt.reflection.provider

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.lt.ksp.appendText
import com.lt.reflection.ReflectionObject
import com.lt.reflection.options.KSClassConstructorInfo
import com.lt.reflection.options.KspOptions

/**
 * creator: lt  2022/10/20  lt.dygzs@qq.com
 * effect : ksp处理程序
 * warning:
 */
internal class VirtualReflectionSymbolProcessor(private val environment: SymbolProcessorEnvironment) :
    SymbolProcessor {
    //只做一轮处理
    private var isHandled = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (isHandled) return listOf()
        isHandled = true
        val kspOptions = KspOptions(environment)
        val packageList = kspOptions.getPackageList()
        val functionName = kspOptions.getFunctionName()
        val ret = mutableListOf<KSAnnotated>()
        val classConstructorSet = mutableSetOf<KSClassConstructorInfo>()

        //处理指定包内的构造函数
        resolver.getAllFiles()
            .filter {
                //包名如果是配置的或子包名
                val packageName = it.packageName.asString() + "."
                packageList.any(packageName::startsWith)
            }.forEach { ksFile ->
                handlerObjectWithKSFile(resolver, ksFile, ret, classConstructorSet)
            }
        //处理注解标注的构造函数
        resolver.getSymbolsWithAnnotation(ReflectionObject::class.qualifiedName!!)
            .forEach {
                when (it) {
                    is KSFile -> handlerObjectWithKSFile(resolver, it, ret, classConstructorSet)
                    is KSDeclaration -> handlerObjectWithKSClass(it, ret, classConstructorSet)
                }
            }

        createFile(classConstructorSet, functionName)

        //返回无法处理的符号
        return ret
    }

    //虚拟反射构造函数-处理file
    @OptIn(KspExperimental::class)
    private fun handlerObjectWithKSFile(
        resolver: Resolver,
        ksFile: KSFile,
        ret: MutableList<KSAnnotated>,
        classConstructorSet: MutableSet<KSClassConstructorInfo>
    ) {
        resolver.getDeclarationsInSourceOrder(ksFile).forEach {
            handlerObjectWithKSClass(it, ret, classConstructorSet)
        }
    }

    //虚拟反射构造函数-处理class
    private fun handlerObjectWithKSClass(
        it: KSDeclaration,
        ret: MutableList<KSAnnotated>,
        classConstructorSet: MutableSet<KSClassConstructorInfo>
    ) {
        if (it is KSClassDeclaration && it.classKind == ClassKind.CLASS) {
            if (!it.validate()) ret.add(it)
            else it.accept(
                VirtualReflectionVisitor(environment, classConstructorSet),
                Unit
            )//处理符号
        }
    }

    //生成虚拟反射文件
    private fun createFile(
        classConstructorSet: MutableSet<KSClassConstructorInfo>,
        functionName: String
    ) {
        val file = environment.codeGenerator.createNewFile(
            Dependencies(
                true,
                *classConstructorSet.mapNotNull { it.ksFile }.toSet().toTypedArray()
            ), "", "VirtualReflectionUtil"
        )
        //记录有参数的构造方法,稍后处理
        val haveArgsConstructor = ArrayList<KSClassConstructorInfo>()
        file.appendText(
            "fun <T : Any> kotlin.reflect.KClass<T>.$functionName(): T =\n" +
                    "    VirtualReflectionUtil.$functionName(simpleName!!) as T\n" +
                    "\n" +
                    "fun <T : Any> kotlin.reflect.KClass<T>.$functionName(vararg args: Any?): T =\n" +
                    "    VirtualReflectionUtil.$functionName(simpleName!!, *args) as T\n" +
                    "\n" +
                    "object VirtualReflectionUtil {\n" +
                    "    fun ${functionName}OrNull(name: String): Any? = when (name) {\n"
        )
        //处理空参构造方法
        classConstructorSet.forEach {
            if (it.constructorArgsType.isNotEmpty()) {
                haveArgsConstructor.add(it)
                return@forEach
            }
            val name = "${it.packageName}.${it.className}"
            file.appendText("        \"${it.className}\" -> $name()\n")
        }
        file.appendText(
            "        else -> null\n" +
                    "    }\n" +
                    "\n" +
                    "    fun ${functionName}OrNull(name: String, vararg args: Any?): Any? = when {\n"
        )
        //处理有参构造方法
        haveArgsConstructor.forEach {
            val name = "${it.packageName}.${it.className}"
            file.appendText(
                "        name == \"${it.className}\" && args.size == ${it.constructorArgsType.size} -> $name("
            )
            //处理参数强转
            it.constructorArgsType.forEachIndexed { index, s ->
                file.appendText("args[$index] as $s, ")
            }
            file.appendText(")\n")
        }
        file.appendText(
            "        else -> null\n" +
                    "    }\n\n" +
                    "    fun $functionName(name: String): Any =\n" +
                    "            ${functionName}OrNull(name) ?: throw RuntimeException(\"\$name: Not find in VirtualReflection config\")\n\n" +
                    "    fun $functionName(name: String, vararg args: Any?): Any =\n" +
                    "            ${functionName}OrNull(name, *args) ?: throw RuntimeException(\"\$name: Not find in VirtualReflection config\")\n" +
                    "}"
        )
        file.flush()
        file.close()
    }
}