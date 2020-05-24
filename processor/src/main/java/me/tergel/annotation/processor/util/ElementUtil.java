package me.tergel.annotation.processor.util;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.List;

public class ElementUtil {
    public static boolean isAnnotatedWith(Element element,
                                          Class annotationCls) {
        if (element == null) return false;
        List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            TypeElement typeElement = (TypeElement) annotationMirror.getAnnotationType().asElement();
            String s = typeElement.getQualifiedName().toString();
            if (annotationCls.getCanonicalName().equals(s)) {
                Logger.log(element.getSimpleName() + " is annotated with " + annotationCls.getName());
                return true;
            }
        }

        return false;
    }

    public static String getAnnotationParams(Element element, Class annotationCls) {
        if (element == null) return null;
        List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors();
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            TypeElement typeElement = (TypeElement) annotationMirror.getAnnotationType().asElement();
            String s = typeElement.getQualifiedName().toString();
            if (annotationCls.getCanonicalName().equals(s)) {
                typeElement.getTypeParameters();
                Logger.log(element.getSimpleName() + " is annotated with " + annotationCls.getName());
                return "";
            }
        }
        return null;
    }

}