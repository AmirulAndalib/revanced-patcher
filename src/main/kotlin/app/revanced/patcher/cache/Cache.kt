package app.revanced.patcher.cache

import app.revanced.patcher.proxy.ClassProxy
import app.revanced.patcher.signature.MethodSignatureScanResult
import org.jf.dexlib2.iface.ClassDef

class Cache(
    internal val classes: Set<ClassDef>,
    val resolvedMethods: MethodMap
) {
    internal val classProxy = mutableListOf<ClassProxy>()

    fun findClass(predicate: (ClassDef) -> Boolean): ClassProxy? {
        // if a class has been found with the given predicate,
        val foundClass = classes.singleOrNull(predicate) ?: return null
        // create a class proxy with the index of the class in the classes list
        // TODO: There might be a more elegant way to the comment above
        val classProxy = ClassProxy(foundClass, classes.indexOf(foundClass))
        // add it to the cache and
        this.classProxy.add(classProxy)
        // return the proxy class
        return classProxy
    }
}

class MethodMap : LinkedHashMap<String, MethodSignatureScanResult>() {
    override fun get(key: String): MethodSignatureScanResult {
        return super.get(key) ?: throw MethodNotFoundException("Method $key was not found in the method cache")
    }
}

class MethodNotFoundException(s: String) : Exception(s)