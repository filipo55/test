package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Descriptor;
import com.mycompany.myapp.domain.Study;
import com.mycompany.myapp.repository.StudyRepository;
import com.mycompany.myapp.service.CalculationService;
import com.mycompany.myapp.service.DescriptorService;
import com.mycompany.myapp.service.StudyService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nimbusds.jose.util.JSONObjectUtils;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import net.minidev.json.JSONObject;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import com.mycompany.myapp.parser.*;
import org.xml.sax.XMLReader;

import javax.xml.bind.JAXBContext;
import javax.xml.*;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Descriptor}.
 */
@RestController
@RequestMapping("/api")
public class DescriptorResource {

    private final Logger log = LoggerFactory.getLogger(DescriptorResource.class);

    private static final String ENTITY_NAME = "descriptor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DescriptorService descriptorService;


    //@Autowired
    //TwoDimensionSpatialCoordinateService twoDimensionSpatialCoordinateService;

    @Autowired
    StudyService studyService;

    CalculationService calculationService = new CalculationService();

    public DescriptorResource(DescriptorService descriptorService) {
        this.descriptorService = descriptorService;
    }

    /**
     * {@code POST  /descriptors} : Create a new descriptor.
     *
     * @param xml the descriptor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new descriptor, or with status {@code 400 (Bad Request)} if the descriptor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/descriptors")
    public ResponseEntity<Descriptor> createDescriptor(@RequestBody String xml) throws URISyntaxException, ParseException, IOException, SAXException, ParserConfigurationException, JAXBException, XMLStreamException {
        log.debug("REST request to create descriptor from xml:" + xml);

        File fXmlFile = new File(xml);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();

        //String studyInstanceUID = doc.getElementsByTagName("imageStudy").item(0).getTextContent();
        Descriptor descriptor = new Descriptor();
        descriptor.setDateCreated(LocalDate.now());

//        FileReader fr = null;
//        fr = new FileReader(xml);
//        XMLReader reader = new NamespaceFilterXMLReader("",false);
//        InputSource is = new InputSource(fr);
//        SAXSource ss = new SAXSource(reader, is);

        XMLInputFactory xif = XMLInputFactory.newFactory();
        xif.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
        StreamSource source = new StreamSource(xml);
        XMLStreamReader xsr = xif.createXMLStreamReader(source);

        JAXBContext jaxbContext = JAXBContext.newInstance(ImageAnnotationCollection.class);


        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();


        Source xmlInput = new StreamSource(new File(xml));
        Source xsl = new StreamSource(new File("annotations.xsl"));
        String resultFile = "transormedOutput.xml";
        Result xmlOutput = new StreamResult(new File(resultFile));


        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer(xsl);
            transformer.transform(xmlInput, xmlOutput);
        } catch (TransformerException e) {
            // Handle.
        }



        double area = calculationService.CalculateDataFromFile(xml);

//        ImageAnnotationCollection imageAnnotationCollection = (ImageAnnotationCollection) jaxbUnmarshaller.unmarshal(xsr);
//        List<ImageAnnotation> imageAnnotations = imageAnnotationCollection.getImageAnnotations().getImageAnnotation();
//
//
//        for (ImageAnnotation image: imageAnnotations
//             ) {
//
//            for(int i =0; i< image.getMarkupEntityCollection().getMarkupEntity().size(); i++)
//            {
//                MarkupEntity markupEntity = image.getMarkupEntityCollection().getMarkupEntity().get(i);
//                markupEntity.getUniqueIdentifier();
//            }
//
//        }





//        NodeList coordinates = doc.getElementsByTagName("TwoDimensionSpatialCoordinate");
//        for (int i = 0; i < coordinates.getLength(); i++) {
//
//            Node nNode = coordinates.item(i);
//
//            System.out.println("\nCurrent Element :" + nNode.getNodeName());
//
//            if (nNode.getNodeType() == Node.ELEMENT_NODE)
//            {
//
//                Element eElement = (Element) nNode;
//
//
//                System.out.println("coordinateIndex : " + eElement.getChildNodes().item(1).getAttributes().item(0).getNodeValue());
//                System.out.println("x : " + eElement.getChildNodes().item(3).getAttributes().item(0).getNodeValue());
//                System.out.println("y : " + eElement.getChildNodes().item(5).getAttributes().item(0).getNodeValue());
//
//                TwoDimensionSpatialCoordinate temp = new TwoDimensionSpatialCoordinate();
//                temp.setCoordinateIndex(Integer.parseInt(eElement.getChildNodes().item(1).getAttributes().item(0).getNodeValue()));
//                temp.setX(Float.parseFloat(eElement.getChildNodes().item(3).getAttributes().item(0).getNodeValue()));
//                temp.setY(Float.parseFloat(eElement.getChildNodes().item(5).getAttributes().item(0).getNodeValue()));
//
//                twoDimensionSpatialCoordinateService.save(temp);
//                temp.setDescriptor(descriptor);
//            }
//        }


//        if(!studyInstanceUID.isEmpty())
//        {
//            log.debug("Setting up study with UID: " + studyInstanceUID);
//
//            studyService.GetStudyByUID((studyInstanceUID)).ifPresent(study -> descriptor.setStudy(study));
//            if((studyService.GetStudyByUID((studyInstanceUID))).isPresent() == false)
//                log.debug("NO STUDY WITH UID: " + studyInstanceUID);
//        }


        if (descriptor.getId() != null) {
            throw new BadRequestAlertException("A new descriptor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Descriptor result = descriptorService.save(descriptor);
        return ResponseEntity.created(new URI("/api/descriptors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /descriptors} : Updates an existing descriptor.
     *
     * @param descriptor the descriptor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated descriptor,
     * or with status {@code 400 (Bad Request)} if the descriptor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the descriptor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/descriptors")
    public ResponseEntity<Descriptor> updateDescriptor(@RequestBody Descriptor descriptor) throws URISyntaxException {
        log.debug("REST request to update Descriptor : {}", descriptor);
        if (descriptor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Descriptor result = descriptorService.save(descriptor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, descriptor.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /descriptors} : get all the descriptors.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of descriptors in body.
     */
    @GetMapping("/descriptors")
    public ResponseEntity<List<Descriptor>> getAllDescriptors(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Descriptors");
        Page<Descriptor> page;
        if (eagerload) {
            page = descriptorService.findAllWithEagerRelationships(pageable);
        } else {
            page = descriptorService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /descriptors/:id} : get the "id" descriptor.
     *
     * @param id the id of the descriptor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the descriptor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/descriptors/{id}")
    public ResponseEntity<Descriptor> getDescriptor(@PathVariable String id) {
        log.debug("REST request to get Descriptor : {}", id);
        Optional<Descriptor> descriptor = descriptorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(descriptor);
    }

    /**
     * {@code DELETE  /descriptors/:id} : delete the "id" descriptor.
     *
     * @param id the id of the descriptor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/descriptors/{id}")
    public ResponseEntity<Void> deleteDescriptor(@PathVariable String id) {
        log.debug("REST request to delete Descriptor : {}", id);
        descriptorService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
