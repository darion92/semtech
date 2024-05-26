package org.semtech.program.processor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.semtech.program.controller.FileUploadController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FileUploadController.class)
@Import({ PopulationProcessor.class })
public class PopulationProcessorTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    void testPopulationProcessorOk() throws Exception {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "file.csv",
                MediaType.TEXT_PLAIN_VALUE,
                dataSet().getBytes()
        );
        MvcResult result =  mockMvc.perform(multipart("/uploadFile").file(file))
                .andExpect(status().isOk()).andReturn();
        String expected = "Department name : LOIRE || Total population : 2361 || City with largest population : Échalas\n" +
                "Department name : VILLAINE || Total population : 637 || City with largest population : Sougé\n" +
                "Department name : GARONNE || Total population : 558 || City with largest population : Berru\n" +
                "***********************\n" +
                "Department with smallest population : GARONNE";
        Assertions.assertEquals(expected, result.getResponse().getContentAsString());

    }

    String dataSet(){
        return  "code;commune;population;name\n" +
                "44;Denting;272;LOIRE\n" +
                "44;Échalas;1786;LOIRE\n" +
                "44;Aubigny;303;LOIRE\n" +
                "35;Sougé;482;VILLAINE\n" +
                "35;Bouzic;155;VILLAINE\n" +
                "31;Berru;558;GARONNE";
    }
}
