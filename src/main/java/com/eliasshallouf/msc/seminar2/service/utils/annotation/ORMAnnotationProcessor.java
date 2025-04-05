package com.eliasshallouf.msc.seminar2.service.utils.annotation;

import com.google.auto.service.AutoService;
import jakarta.persistence.Entity;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes("com.eliasshallouf.msc.seminar2.service.utils.annotation.A")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class ORMAnnotationProcessor extends AbstractProcessor {
    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        /*Set<Element> annotatedElements = new HashSet<>();
        for (TypeElement annotation : annotations) {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
            annotatedElements.addAll(elements);
        }*/
        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(Entity.class);

        for (Element element : annotatedElements) {
            if (element.getKind() == ElementKind.CLASS) {
                try {
                    logClassFields((TypeElement) element);
                } catch (IOException e) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "Failed to generate tmp method " + e.getMessage());
                }
            }
        }

        return true;
    }

    private void logClassFields(TypeElement classElement) throws IOException {
        new File("D:\\classes\\" + classElement.getQualifiedName()).createNewFile();

        messager.printMessage(Diagnostic.Kind.NOTE, "Class annotated with @Entity: " + classElement.getQualifiedName());

        for (Element enclosedElement : classElement.getEnclosedElements()) {
            if (enclosedElement.getKind() == ElementKind.FIELD) {
                VariableElement field = (VariableElement) enclosedElement;
                messager.printMessage(Diagnostic.Kind.NOTE, "Field: " + field.getSimpleName() + " (" + field.asType() + ")");
            }
        }
    }

    private void generateGetter(VariableElement field) throws IOException {
        TypeElement enclosingClass = (TypeElement) field.getEnclosingElement();
        String className = enclosingClass.getQualifiedName().toString();
        String fieldName = field.getSimpleName().toString();
        String fieldType = field.asType().toString();

        String getterName = "a" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

        String packageName = elementUtils.getPackageOf(enclosingClass).getQualifiedName().toString();
        String classBuilder = String.format(
                "package %s;\n\n" +
                        "public class %s {\n" +
                        "    public %s %s() {\n" +
                        "        return this.%s;\n" +
                        "    }\n" +
                        "}\n",
                packageName,
                className,
                fieldType,
                getterName,
                fieldName
        );

        messager.printMessage(Diagnostic.Kind.NOTE, classBuilder);

        JavaFileObject javaFileObject = filer.createSourceFile(className);
        try (Writer writer = javaFileObject.openWriter()) {
            writer.write(classBuilder);
        }
    }
}
