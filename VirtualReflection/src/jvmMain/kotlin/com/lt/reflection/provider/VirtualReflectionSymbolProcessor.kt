package com.lt.reflection.provider

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.*
import com.lt.ksp.appendText
import com.lt.reflection.ReflectionObject
import com.lt.reflection.options.KSClassConstructorInfo
import com.lt.reflection.options.KspOptions
import java.io.File
import java.io.OutputStream

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
        val className = kspOptions.getClassName()
        val classConstructorSet = mutableSetOf<KSClassConstructorInfo>()

        //处理指定包内的构造函数
        resolver.getAllFiles()
            .filter {
                //包名如果是配置的或子包名
                val packageName = it.packageName.asString() + "."
                packageList.any(packageName::startsWith)
            }.forEach { ksFile ->
                handlerObjectWithKSFile(resolver, ksFile, classConstructorSet)
            }
        //处理注解标注的构造函数
        resolver.getSymbolsWithAnnotation(ReflectionObject::class.qualifiedName!!)
            .forEach {
                when (it) {
                    is KSFile -> handlerObjectWithKSFile(resolver, it, classConstructorSet)
                    is KSDeclaration -> handlerObjectWithKSClass(it, classConstructorSet)
                }
            }

        createFile(classConstructorSet, functionName, className)
        //ios临时方案
        val iosTargetName = kspOptions.getIosTargetName()
        val fileName = "$className.kt"
        environment.codeGenerator.generatedFile.find {
            val path = it.absolutePath
            path.endsWith(fileName) && path.contains("${File.separator}ksp${File.separator}ios")
        }?.let {
            val targetFile = File(
                File(it.absolutePath.split("${File.separator}ksp${File.separator}").first(), "ksp${File.separator}ios${File.separator}iosMain${File.separator}kotlin"),
                "$className$iosTargetName.kt"
            )
            targetFile.parentFile?.let {
                if (!it.exists()) it.mkdirs()
            }
            createFile(
                classConstructorSet,
                functionName + iosTargetName,
                className + iosTargetName,
                targetFile.outputStream()
            )
        }

        //返回无法处理的符号
        return listOf()
    }

    //虚拟反射构造函数-处理file
    @OptIn(KspExperimental::class)
    private fun handlerObjectWithKSFile(
        resolver: Resolver,
        ksFile: KSFile,
        classConstructorSet: MutableSet<KSClassConstructorInfo>
    ) {
        resolver.getDeclarationsInSourceOrder(ksFile).forEach {
            handlerObjectWithKSClass(it, classConstructorSet)
        }
    }

    //虚拟反射构造函数-处理class
    private fun handlerObjectWithKSClass(
        it: KSDeclaration,
        classConstructorSet: MutableSet<KSClassConstructorInfo>
    ) {
        if (it is KSClassDeclaration && it.classKind == ClassKind.CLASS) {
            it.accept(
                VirtualReflectionVisitor(environment, classConstructorSet),
                Unit
            )//处理符号
        }
    }

    //生成虚拟反射文件
    private fun createFile(
        classConstructorSet: MutableSet<KSClassConstructorInfo>,
        functionName: String,
        className: String,
        file: OutputStream = environment.codeGenerator.createNewFile(
            Dependencies(
                true,
                *classConstructorSet.mapNotNull { it.ksFile }.toSet().toTypedArray()
            ), "", className
        ),
    ) {
        //记录有参数的构造方法,稍后处理
        val haveArgsConstructor = ArrayList<KSClassConstructorInfo>()
        file.appendText(
            "fun <T : Any> kotlin.reflect.KClass<T>.$functionName(): T =\n" +
                    "    $className.$functionName(this) as T\n" +
                    "\n" +
                    "fun <T : Any> kotlin.reflect.KClass<T>.$functionName(vararg args: Any?): T =\n" +
                    "    $className.$functionName(this, *args) as T\n" +
                    "\n" +
                    "object $className {\n" +
                    "    fun ${functionName}OrNull(kClass: kotlin.reflect.KClass<*>): Any? = when (kClass) {\n"
        )
        //处理空参构造方法
        classConstructorSet.forEach {
            if (it.constructorArgsType.isNotEmpty()) {
                haveArgsConstructor.add(it)
                return@forEach
            }
            val name = "${it.packageName}.${it.className}"
            file.appendText("        $name::class -> $name()\n")
        }
        file.appendText(
            "        else -> null\n" +
                    "    }\n" +
                    "\n" +
                    "    fun ${functionName}OrNull(kClass: kotlin.reflect.KClass<*>, vararg args: Any?): Any? = when {\n"
        )
        //处理有参构造方法
        haveArgsConstructor.forEach {
            val name = "${it.packageName}.${it.className}"
            file.appendText(
                "        kClass == $name::class && args.size == ${it.constructorArgsType.size} -> $name("
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
                    "    fun $functionName(kClass: kotlin.reflect.KClass<*>): Any =\n" +
                    "            ${functionName}OrNull(kClass) ?: throw RuntimeException(\"\$kClass: Not find in VirtualReflection config\")\n\n" +
                    "    fun $functionName(kClass: kotlin.reflect.KClass<*>, vararg args: Any?): Any =\n" +
                    "            ${functionName}OrNull(kClass, *args) ?: throw RuntimeException(\"\$kClass: Not find in VirtualReflection config\")\n" +
                    "}"
        )
        file.flush()
        file.close()
    }
}