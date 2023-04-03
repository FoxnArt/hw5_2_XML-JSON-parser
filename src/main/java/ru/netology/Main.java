package ru.netology;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Employee> list = parseXML("data.xml");
        String json = listToJson(list);
        String jsonFileName = "data2.json";
        writeString(json, jsonFileName);
    }

    public static List<Employee> parseXML(String fileName) {
        List<Employee> employees = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(fileName));
            Node root = doc.getDocumentElement();

            // Получение списка всех элементов employee внутри корневого элемента, перебор элементов
            NodeList nodeList = root.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    Element element = (Element) node;

                    // Получение списка всех дочерних элементов employee, перебор элементов
                    NodeList childNodeList = element.getChildNodes();
                    List<String> fieldsEmployee = new ArrayList<>();
                    for (int a = 0; a < childNodeList.getLength(); a++) {
                        Node childNode = childNodeList.item(a);
                        if (Node.ELEMENT_NODE == childNode.getNodeType()) {
                            Element childElement = (Element) childNode;
                            fieldsEmployee.add(childElement.getTextContent()); // Получение и сохранение в список значений полей для объекта класса Employee
                        }
                    }
                    employees.add(new Employee(
                            Long.parseLong(fieldsEmployee.get(0)),
                            fieldsEmployee.get(1),
                            fieldsEmployee.get(2),
                            fieldsEmployee.get(3),
                            Integer.parseInt(fieldsEmployee.get(4))));
                }
            }
            } catch(Exception ex){
                ex.printStackTrace();
            }
        return employees;
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        return gson.toJson(list, listType);
    }

    public static void writeString(String json, String jsonFileName) {
        try (FileWriter writer = new FileWriter(jsonFileName)) {
            writer.write(json);
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}