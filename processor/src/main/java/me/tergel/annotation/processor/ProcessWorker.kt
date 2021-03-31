package me.tergel.annotation.processor

import me.tergel.annotation.UniqueString
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Stream
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

object ProcessWorker : AbstractProcessor() {
    @JvmStatic
    fun begin(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment): Boolean = process(annotations, roundEnv)

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment): Boolean {
        log("start annotation processing...")
        val classMemberRecords = ConcurrentHashMap<String, MutableSet<String>>()

        val annotatedClasses = roundEnv.getElementsAnnotatedWith(UniqueString::class.java)
        annotatedClasses.buildStream()
                .flatMap { annotatedClsElement ->
                    val clsName = (annotatedClsElement as TypeElement).qualifiedName.toString()
                    log("scan [$clsName]")
                    val scope = annotatedClsElement.scope
                    val key = if (scope.isNullOrEmpty()) clsName else scope
                    classMemberRecords[key] = Collections.synchronizedSet(HashSet<String>())
                    annotatedClsElement.enclosedElements.buildStream()
                }
                .filter { it.kind == ElementKind.FIELD }
                .filter { it is VariableElement }
                .filter { (it as VariableElement).constantValue is String }
                .filter { it.isNotAnnotatedWithSkip() }
                .forEach {
                    val key = it.recordKey
                    val memberVar = (it as VariableElement).constantValue as String
                    val memberVars = classMemberRecords[key]!!
                    if (memberVars.contains(memberVar)) {
                        throw DuplicateMemberError(key, memberVar)
                    }
                    memberVars.add(memberVar)
                }
        return true
    }


    private fun <T> Collection<T>.buildStream(): Stream<T> {
        return if (this.size > 100) this.parallelStream() // no reason, just 100
        else this.stream()
    }


    private val Element.recordKey: String
        get() {
            val annotatedClassElement = this.enclosingElement as TypeElement
            val scope = annotatedClassElement.scope
            return if (scope.isNullOrEmpty()) {
                annotatedClassElement.qualifiedName.toString()
            } else {
                scope
            }
        }

    private val Element.scope: String?
        get() = this.getAnnotation(UniqueString::class.java)?.scope


}


