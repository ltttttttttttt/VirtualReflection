package com.lt.reflection.provider

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.lt.reflection.appendText
import com.lt.reflection.getKSTypeInfo
import com.lt.reflection.options.CustomOptionsInfo
import com.lt.reflection.options.FunctionFieldsInfo
import com.lt.reflection.options.KspOptions

/**
 * creator: lt  2022/10/20  lt.dygzs@qq.com
 * effect : 访问并处理相应符号
 * warning:
 */
internal class VirtualReflectionVisitor(private val environment: SymbolProcessorEnvironment) : KSVisitorVoid() {
    private val options = KspOptions(environment)

    /**
     * 访问class的声明
     */
    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        //获取class信息并创建kt文件
        val packageName = classDeclaration.packageName.asString()
        val originalClassName = classDeclaration.simpleName.asString()
        val fullName = classDeclaration.qualifiedName?.asString()
            ?: (classDeclaration.packageName.asString() + classDeclaration.simpleName.asString())
        val className = "$originalClassName${options.suffix}"
        val file = environment.codeGenerator.createNewFile(
            Dependencies(
                true,
                classDeclaration.containingFile!!
            ), packageName, className
        )
        //写入头文件
        file.appendText("package $packageName\n\n")
        file.appendText(
            "import androidx.compose.runtime.MutableState\n" +
                    "import androidx.compose.runtime.mutableStateListOf\n" +
                    "import androidx.compose.runtime.mutableStateOf\n" +
                    "import androidx.compose.runtime.snapshots.SnapshotStateList\n" +
                    "import androidx.compose.runtime.toMutableStateList\n\n"
        )
        file.appendText(
            "${options.getClassSerializeAnnotation()}\n" +
                    "class $className(\n"
        )
        //类内的字段(非构造内的)
        val classFields = mutableListOf<String>()
        //addBuff和removeBuff函数用到的字段
        val functionFields = mutableListOf<FunctionFieldsInfo>()
        //遍历构造内的字段
        classDeclaration.primaryConstructor?.parameters?.forEach {
            val name = it.name?.getShortName() ?: ""
            val (ksType, isBuffBean, typeName, nullable, finallyTypeName) = getKSTypeInfo(it.type, options)
            //写入构造内的普通字段
            file.appendText("    ${if (it.isVal) "val" else "var"} $name: $finallyTypeName,\n")
            functionFields.add(FunctionFieldsInfo(name, true, isBuffBean, nullable))
        }
        //遍历所有字段
        classDeclaration.getAllProperties().forEach {
            //只解析类成员
            if (it.parent is KSClassDeclaration) {
                val fieldName = it.simpleName.getShortName()
                if (!it.isMutable)
                    throw RuntimeException("$originalClassName.$fieldName: It is meaningless for the field of val to change to the MutableState<T>")
                val info = getKSTypeInfo(it.type, options)
                val (ksType, isBuffBean, typeName, nullable, finallyTypeName) = info
                val stateFieldName = "_${fieldName}_state"
                //写入构造内的state字段,普通state或list state
                if (!info.isList) {
                    file.appendText("    ${options.getFieldSerializeTransientAnnotation()} val $stateFieldName: MutableState<$finallyTypeName> = null!!,\n")
                    classFields.add(
                        "    var $fieldName: $finallyTypeName = $stateFieldName.value\n" +
                                "        get() {\n" +
                                "            $stateFieldName.value = field\n" +
                                "            return $stateFieldName.value\n" +
                                "        }\n" +
                                "        set(value) {\n" +
                                "            field = value\n" +
                                "            $stateFieldName.value = value\n" +
                                "        }\n"
                    )
                } else {
                    file.appendText("    ${options.getFieldSerializeTransientAnnotation()} val $stateFieldName: SnapshotStateList${info.typeString} = null!!,\n")
                    classFields.add(
                        "    val $fieldName: $typeName${info.typeString} = $stateFieldName\n"
                    )
                }
                functionFields.add(FunctionFieldsInfo(fieldName, false, isBuffBean, nullable, info.isList))
            }
        }
        file.appendText(") {\n")

        fun getInfo() = CustomOptionsInfo(
            originalClassName, className
        )
        //写入非构造内的字段
        classFields.forEach(file::appendText)
        //写入removeBuff
        file.appendText(
            "\n    fun removeBuff(): $fullName =\n" +
                    "        $fullName(${
                        functionFields.filter { it.isInTheConstructor }
                            .map {
                                if (it.isBuffBean)
                                    "${it.fieldName}${it.nullable}.removeBuff()"
                                else
                                    it.fieldName
                            }
                            .joinToString()
                    }).also {\n"
        )
        functionFields.filter { !it.isInTheConstructor }.forEach {
            val isList = if (it.isList) it.nullable + ".toList()" else ""
            file.appendText(
                "            it.${it.fieldName} = ${
                    if (it.isBuffBean)
                        "${it.fieldName}$isList${it.nullable}.removeBuff()"
                    else
                        it.fieldName + isList
                }\n"
            )
        }
        file.appendText("        }\n")
        file.appendText("\n${options.getCustomInClass(::getInfo)}\n\n")
        file.appendText("}\n\n")
        //写入addBuff
        file.appendText(
            "fun $fullName.addBuff(): $className =\n" +
                    "    $className(\n"
        )
        functionFields.forEach {
            if (it.isInTheConstructor)
                file.appendText(
                    "        ${
                        if (it.isBuffBean)
                            "${it.fieldName}${it.nullable}.addBuff()"
                        else
                            it.fieldName
                    },\n"
                )
            else {
                if (!it.isList) {
                    file.appendText(
                        "        mutableStateOf(${
                            if (it.isBuffBean)
                                "${it.fieldName}${it.nullable}.addBuff()"
                            else
                                it.fieldName
                        }),\n"
                    )
                } else {
                    file.appendText(
                        "        ${
                            if (it.isBuffBean)
                                "${it.fieldName}${it.nullable}.addBuff()"
                            else
                                it.fieldName
                        }${it.nullable}.toMutableStateList() ?: mutableStateListOf(),\n"
                    )
                }
            }
        }
        file.appendText("    )\n\n${options.getCustomInFile(::getInfo)}")
        //写入Collection<addBuff>
        file.appendText(
            "\n\nfun Collection<$fullName?>.addBuff() =\n" +
                    "    map { it?.addBuff() } as List<$className>"
        )
        //写入Collection<removeBuff>
        file.appendText(
            "\n\nfun Collection<$className?>.removeBuff() =\n" +
                    "    map { it?.removeBuff() } as List<$fullName>"
        )
        file.close()
    }
}