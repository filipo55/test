package com.mycompany.myapp.service;


import com.mycompany.myapp.parser.TwoDimensionSpatialCoordinate;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalculationService {

    List<TwoDimensionSpatialCoordinate> Vertices = new ArrayList<>();

    public double CalculateDataFromFile(String xml) throws ParserConfigurationException, IOException, SAXException {

        File fXmlFile = new File(xml);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        NodeList coordinates = doc.getElementsByTagName("TwoDimensionSpatialCoordinate");
        for (int i = 0; i < coordinates.getLength(); i++) {

            Node nNode = coordinates.item(i);

            System.out.println("\nCurrent Element :" + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE)
            {

                Element eElement = (Element) nNode;


                System.out.println("coordinateIndex : " + eElement.getChildNodes().item(1).getAttributes().item(0).getNodeValue());
                System.out.println("x : " + eElement.getChildNodes().item(3).getAttributes().item(0).getNodeValue());
                System.out.println("y : " + eElement.getChildNodes().item(5).getAttributes().item(0).getNodeValue());

                TwoDimensionSpatialCoordinate temp = new TwoDimensionSpatialCoordinate();
                temp.setCoordinateIndex(Integer.parseInt(eElement.getChildNodes().item(1).getAttributes().item(0).getNodeValue()));
                temp.setX(Float.parseFloat(eElement.getChildNodes().item(3).getAttributes().item(0).getNodeValue()));
                temp.setY(Float.parseFloat(eElement.getChildNodes().item(5).getAttributes().item(0).getNodeValue()));


                Vertices.add(temp);
            }
        }

        // Initialze area
        double area = 0.0;

        // Calculate value of shoelace formula
        int j = Vertices.size() - 1;
        for (int i = 0; i < Vertices.size(); i++)
        {
            area += (Vertices.get(j).getX() + Vertices.get(i).getX()) * (Vertices.get(j).getY() - Vertices.get(i).getY());
            j = i;  // j is previous vertex to i
        }

        System.out.println("AREA: " + area);

        // Return absolute value
        return java.lang.Math.abs(area / 2.0);
    }



}
