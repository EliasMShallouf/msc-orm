package com.eliasshallouf.msc.seminar2.domain.model.Test;

import com.eliasshallouf.msc.seminar2.domain.model.*;
import com.eliasshallouf.msc.seminar2.service.utils.columns.*;
import com.eliasshallouf.msc.seminar2.service.utils.helpers.ClassHelper;
import com.eliasshallouf.msc.seminar2.service.utils.helpers.LogicalStream;
import com.eliasshallouf.msc.seminar2.service.utils.helpers.Mapper;
import com.eliasshallouf.msc.seminar2.service.utils.table.EntityModel;
import com.eliasshallouf.msc.seminar2.service.utils.table.TableColumns;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class ORMGenerator {
    public static void main(String[] args) throws IOException {
        generate(
            "com.eliasshallouf.msc.seminar2.domain.model.orm",
            new Class[] {
                Category.class,
                Customer.class,
                CustomerDemographics.class,
                Employee.class,
                EmployeeTerritory.class,
                OrderDetail.class,
                Product.class,
                Region.class,
                SalesOrder.class,
                Shipper.class,
                Supplier.class,
                Territory.class
            }
        );
    }

    public static void generate(
        String packageName,
        Class<?>[] models
    ) throws IOException {
        Properties properties = new Properties();

        if(!new File("orm_classes_date").exists())
            new File("orm_classes_date").createNewFile();

        properties.load(new FileInputStream("orm_classes_date"));

        for(Class<?> c : models) {
            doGenerate(properties, c, packageName);
        }

        properties.store(new FileWriter("orm_classes_date"), "");
    }

    private static void doGenerate(Properties properties, Class<?> clazz, String p) throws IOException {
        System.out.println("Generating " + clazz.getName() + "Table");

        if(!generateFile(properties, clazz))
            return;

        properties.setProperty(clazz.getName(), generateTable(clazz, p) + "");
        System.out.println("Generated " + clazz.getName() + "Table");
    }

    private static boolean generateFile(Properties properties, Class<?> clazz) {
        File c = new File(
        System.getProperty("user.dir").replace("\\", "/") +
                "/src/main/java/" +
                clazz.getName().replace(".", "/") + ".java"
        );

        return Long.parseLong(properties.getProperty(clazz.getName(), "0")) != c.lastModified();
    }

    private static long generateTable(Class<?> clazz, String p) throws IOException {
        File folder = new File(
    System.getProperty("user.dir").replace("\\", "/") +
            "/src/main/java/" +
            p.replace(".", "/") + "/"
        );
        if(!folder.exists())
            folder.mkdirs();

        File c = new File(
            folder,
            clazz.getSimpleName() + "Table.java"
        );
        Writer w = new FileWriter(c);
        w.write(createClass(clazz, p));
        w.flush();
        w.close();

        /*boolean addC = true;
        boolean addT = true;
        try {
            Method table = clazz.getDeclaredMethod("table");
            addT = false;

            System.out.println("Method table() already created inside " + clazz.getName());
        } catch (NoSuchMethodException|SecurityException ex) { }

        try {
            Method columns = clazz.getDeclaredMethod("columns");
            addC = false;

            System.out.println("Method columns() already created inside " + clazz.getName());
        } catch (NoSuchMethodException|SecurityException ex) { }

        if(!addC && !addT)
            return c.lastModified();

        c = new File(
    System.getProperty("user.dir").replace("\\", "/") +
            "/src/main/java/" +
            clazz.getName().replace(".", "/") + ".java"
        );

        Scanner in = new Scanner(c);
        String content = "";
        while(in.hasNextLine()) {
            content += in.nextLine() + "\n";
        }
        in.close();
        content = content.trim();

        content = content.substring(0, content.lastIndexOf("}")) + "\n";

        if(addT)
            content += """
                public %s table() {
                    return new %s();
                }
            """.formatted(
                p + "." + clazz.getSimpleName() + "Table",
                p + "." + clazz.getSimpleName() + "Table"
            );

        if(addC)
            content += "\n" + """
                public %s columns() {
                    return table().columns();
                }
            """.formatted(
                p + "." + clazz.getSimpleName() + "Table.Columns"
            );

        content += "}";

        w = new FileWriter(c);
        w.write(content);
        w.flush();
        w.close();*/

        return c.lastModified();
    }

    private static String createClass(Class<?> clazz, String p) {
        StringBuilder classBuilder = new StringBuilder();
        classBuilder.append("package ").append(p).append(";\n");
        Set<Class<?>> importClasses = new HashSet<>();

        StringBuilder builder = new StringBuilder();

        for(Class<?> c : new Class[] {
            clazz,
            EntityModel.class,
            TableColumns.class,
            //ColumnInfo.class,
            //TextColumn.class,
            //NumericColumn.class,
            //DateColumn.class,
            //BlobColumn.class
        })
            classBuilder.append("\nimport ").append(c.getName()).append(";");

        builder.append("\n\npublic class ").append(clazz.getSimpleName()).append("Table extends EntityModel<").append(clazz.getSimpleName()).append("> {\n");

        builder.append("\tpublic static class Columns extends TableColumns<").append(clazz.getSimpleName()).append("> {");

        StringBuilder entityColumnsFunctionsBuilder = new StringBuilder();

        for(Field f : clazz.getDeclaredFields()) {
            String targetColumnClass = "";
            String fieldName = "";
            String realName = "";

            if(f.isAnnotationPresent(Id.class) || f.isAnnotationPresent(Column.class) || f.isAnnotationPresent(Lob.class)) {
                fieldName = f.getName();
                realName = LogicalStream
                        .of(f.getAnnotation(Column.class))
                        .ifTrue(c -> c != null && !c.name().isEmpty())
                        .thenReturn(Column::name)
                        .otherwise(c -> f.getName())
                        .get();

                if(f.isAnnotationPresent(Lob.class)) {
                    targetColumnClass = BlobColumn.class.getSimpleName();

                    importClasses.add(BlobColumn.class);
                } else {
                    Class<?> type = Mapper.mapFromPrimitive(f.getType());

                    if(type.getSuperclass().equals(Number.class)) {
                        targetColumnClass = NumericColumn.class.getSimpleName() + "<" + type.getSimpleName() + ">";

                        importClasses.add(NumericColumn.class);
                        importClasses.add(type);
                    } else if(
                        type.equals(Date.class) ||
                        ClassHelper.isImplements(type, LocalDateTime.class) ||
                        ClassHelper.isImplements(type, LocalDate.class) ||
                        ClassHelper.isImplements(type, LocalTime.class)
                    ) {
                        targetColumnClass = DateColumn.class.getSimpleName() + "<" + type.getSimpleName() + ">";

                        importClasses.add(DateColumn.class);
                        importClasses.add(type);
                    } else if(type.equals(Boolean.class)) {
                        targetColumnClass = BooleanColumn.class.getSimpleName();

                        importClasses.add(BooleanColumn.class);
                    } else {
                        targetColumnClass = TextColumn.class.getSimpleName();

                        importClasses.add(TextColumn.class);
                    }
                }

                builder
                    .append("\n\t\tpublic final ")
                    .append(targetColumnClass)
                    .append(" ")
                    .append(fieldName)
                    .append(" = new ")
                    .append(targetColumnClass)
                    .append("(this, \"")
                    .append(realName)
                    .append("\");");

                entityColumnsFunctionsBuilder
                    .append("\n\t")
                    .append("public ")
                    .append(targetColumnClass)
                    .append(" ")
                    .append(fieldName)
                    .append("() {")
                    .append("\n\t\t")
                    .append("return columns.")
                    .append(fieldName)
                    .append(";")
                    .append("\n\t}\n");
            }
        }

        importClasses.forEach(c -> {
            classBuilder.append("\nimport ").append(c.getName()).append(";");
        });

        builder.append("\n\n\t\tpublic Columns(EntityModel<").append(clazz.getSimpleName()).append("> model) {\n");
        builder.append("\t\t\tsuper(model);");
        builder.append("\n\t\t}");
        builder.append("\n\t}");
        builder.append("\n");

        builder.append("""
            
            private final Columns columns = new Columns(this);
            
            public %s() {
                super(%s.class);
            }
        
            @Override
            public Columns columns() {
                return columns;
            }
        """.formatted(clazz.getSimpleName() + "Table", clazz.getSimpleName()));

        builder.append(entityColumnsFunctionsBuilder);
        builder.append("}");
        builder.append("\n");

        classBuilder.append(builder);
        return classBuilder.toString();
    }
}
