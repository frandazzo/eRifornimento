package it.noesis.erifornimento;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import it.noesis.erifornimento.model.Cliente;
import it.noesis.erifornimento.model.Sdi;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect(){ //throws JsonProcessingException

//        Sdi sdi  = new Sdi();
//        sdi.setPec("aa@aa.it");
//        sdi.setCod("sssss");
//
//        String result = new ObjectMapper().writeValueAsString(sdi);
//
//        System.out.println(result);
//        assertNotNull(result);
    }

    @Test
    public void exampleDeserilizzation() throws IOException {
//        String json = "{\"anag\":{\"naz\":\"IT\",\"cf\":\"05105480726\",\"piva\":\"05105480726\",\"denom\":\"INFO .SIST.SAS DI GIOVANNI BARONE E C.\",\"domFisc\":{\"prov\":\"BA\",\"cap\":\"70022\",\"com\":\"ALTAMURA\",\"ind\":\"VIA EUGENIO MONTALE 10\",\"naz\":\"IT\"}},\"dtGenQr\":\"2018-06-18T11:41:37.407+0200\",\"SDI\":{\"pec\":\"\",\"cod\":\"5RUO82D\"}}";
//
//
//        Cliente f = new ObjectMapper().
//                readerFor(Cliente.class)
//                .readValue(json);
//
//        Assert.assertNotNull(f);
//
//        String serilized = new ObjectMapper().writeValueAsString(f);
//        Assert.assertEquals(serilized.equals(json), true);
    }
}