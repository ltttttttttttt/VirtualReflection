package com.lt.virtual_reflection.bean

/**
 * creator: lt  2023/3/2  lt.dygzs@qq.com
 * effect :
 * warning:
 */
class A constructor() {
}

class A2 constructor() {
    constructor(list: List<String?>?) : this()
}

class A3 private constructor()

class A4 private constructor() {
    constructor(a: A) : this()
    constructor(a: HashMap<List<String?>?, Int?>?) : this()
}

abstract class A5 {}

interface A6