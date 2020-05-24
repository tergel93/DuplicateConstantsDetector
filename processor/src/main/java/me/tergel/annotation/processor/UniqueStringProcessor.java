package me.tergel.annotation.processor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import me.tergel.annotation.Skip;
import me.tergel.annotation.UniqueString;
import me.tergel.annotation.processor.util.ElementUtil;
import me.tergel.annotation.processor.util.Logger;

@SupportedAnnotationTypes("me.tergel.annotation.UniqueString")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class UniqueStringProcessor extends AbstractProcessor {
    private final ConstStringBags bags;

    public UniqueStringProcessor() {
        this.bags = new ConstStringBags();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Logger.log("start annotation processing...");
        Set<? extends Element> clsElements = roundEnv.getElementsAnnotatedWith(UniqueString.class);

        for (Element clsElement : clsElements) {
            TypeElement clsTypeElement = (TypeElement) clsElement;
            String className = clsTypeElement.getQualifiedName().toString();
            Logger.log("scan [" + className + "]");
            List<? extends Element> memberElements = clsElement.getEnclosedElements();
            for (Element memberElement : memberElements) {
                ElementKind kind = memberElement.getKind();

                if (kind != ElementKind.FIELD) continue;
                if (ElementUtil.isAnnotatedWith(memberElement, Skip.class)) continue;

                VariableElement fieldElement = (VariableElement) memberElement;
                Object constantValue = fieldElement.getConstantValue();
                if (constantValue instanceof String) {
                    bags.put(className, (String) constantValue);
                }
            }
            Logger.log("pass [" + className + "]");
        }
        Logger.log("all is well. :D");
        return true;
    }

    private static class ConstStringBags {
        private Map<String, Set<String>> bags = new HashMap<>();

        public void put(String key, String constVal) {
            assertVal(key, constVal);
            takeBag(key).add(constVal);
        }

        public void assertVal(String key, String constVal) {
            if (takeBag(key).contains(constVal)) {
                throw new DuplicateException(key, constVal);
            }
        }

        private Set<String> takeBag(String key) {
            Set<String> bag = bags.get(key);
            if (bag == null) {
                bag = new HashSet<>();
                bags.put(key, bag);
            }
            return bag;
        }
    }

}