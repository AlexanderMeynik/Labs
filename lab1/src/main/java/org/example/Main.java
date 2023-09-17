package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import org.w3c.dom.Element;


public class Main {

    private final static String dedaultAlphabet ="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
    private final  static String targetAlphabet ="fTz82MqejlxsgK3NOitdVypb64Ph7EkB1aQLn9WGXYuwZJRDISUAcmHov05rFC";
    private final  static String  folderpath="lab1/src/main/resources/";

    public static String obfuscate(String s) {
        char[] result= new char[s.length()];
        for (int i=0;i<s.length();i++) {
            char c=s.charAt(i);
            int index= dedaultAlphabet.indexOf(c);
            result[i]= targetAlphabet.charAt(index);
        }
        return new String(result);
    }

    public static String unobfuscate(String s) {
        char[] result= new char[s.length()];
        for (int i=0;i<s.length();i++) {
            char c=s.charAt(i);
            int index= targetAlphabet.indexOf(c);
            result[i]= dedaultAlphabet.charAt(index);
        }
        return new String(result);
    }

    private static List<Employee> readFromXML(String filename) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(folderpath+filename));

        List<Employee> employees = new ArrayList<>();
        NodeList nodeList = document.getDocumentElement().getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);//get curect node

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;

                //get id
                String id = node.getAttributes().getNamedItem("id").getNodeValue();
                //get first name
                String firstname = elem.getElementsByTagName("firstName")
                        .item(0).getChildNodes().item(0).getNodeValue();
                //get last name
                String lastname = elem.getElementsByTagName("lastName").item(0)
                        .getChildNodes().item(0).getNodeValue();
                //get location
                String location = elem.getElementsByTagName("location")
                        .item(0).getChildNodes().item(0).getNodeValue();
                //add new Employee to array
                employees.add(new Employee(id, firstname, lastname, location));
            }
        }
        return employees;
    }
    public static void writeArrayToXML(String filename, List<Employee> employees)
            throws ParserConfigurationException, TransformerException {

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        //create new document
        Document doc = docBuilder.newDocument();//create new document
        //create root element
        Element rootElement = doc.createElement("employees");

        doc.appendChild(rootElement);

        //Create Elements for all employees in array and append them to root
        for (Employee e:employees) {
            Element staff = doc.createElement("employee");
            //id is attribute of employee
            staff.setAttribute("id",e.getId());

            //all other fields are contained in other elements, that are appended to employee element
            Element firstName= doc.createElement("firstName");
            firstName.setTextContent(e.getFirstname());
            Element lastName= doc.createElement("lastName");
            lastName.setTextContent(e.getLastname());
            Element loaction= doc.createElement("location");
            loaction.setTextContent(e.getLocation());

            staff.appendChild(firstName);
            staff.appendChild(lastName);
            staff.appendChild(loaction);

            rootElement.appendChild(staff);
        }


        try (FileOutputStream output = new FileOutputStream(folderpath+filename)) {
            writeXml(doc, output);//write document to xml
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void writeXml(Document doc, OutputStream output) throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        //to print xml in several strings
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);
    }
    public static void obfruscateDeobfruscate(String sourceFile, String destinationFile, boolean mode) throws ParserConfigurationException, TransformerException, IOException, SAXException {
        List<Employee>employees=readFromXML(sourceFile);


        /*for (Employee empl: employees)
            System.out.println(empl.toString());*/

        if(mode)
        {
            //obfuscate all properties
            for (Employee empl: employees)
            {
                empl.setId(obfuscate(empl.getId()));
                empl.setFirstname(obfuscate(empl.getFirstname()));
                empl.setLastname(obfuscate(empl.getLastname()));
                empl.setLocation(obfuscate(empl.getLocation()));
            }
        }
        else {
            //deobfuscate
            for (Employee empl: employees)
            {
                empl.setId(unobfuscate(empl.getId()));
                empl.setFirstname(unobfuscate(empl.getFirstname()));
                empl.setLastname(unobfuscate(empl.getLastname()));
                empl.setLocation(unobfuscate(empl.getLocation()));
            }
        }
        writeArrayToXML(destinationFile,employees);
    }

    public static void main(String[] args) throws ParserConfigurationException,
            SAXException, IOException, TransformerException {

        //obfuscate file contents
        obfruscateDeobfruscate("test.xml","obfuscated.xml",true);

        //deobfuscate file contents
        obfruscateDeobfruscate("obfuscated.xml","unobfuscated.xml",false);

        //compare arrays of employees that we get from files
        List<Employee>l1=readFromXML("test.xml");
        List<Employee>l2=readFromXML("unobfuscated.xml");

        System.out.println((l1.equals(l2))?"Contents of files are identical!":"Contents files are different!");



    }
}