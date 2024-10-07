package controller;

import model.enteties.Varaus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VarausControllerTest {

    @Mock
    private VarausController varausController;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByLaskuId() {
        List<Varaus> varausLista = Arrays.asList(new Varaus(), new Varaus());
        when(varausController.findByLaskuId(anyInt())).thenReturn(varausLista);

        List<Varaus> result = varausController.findByLaskuId(1);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testFindByVarausId() {
        Varaus varaus = new Varaus();
        when(varausController.findByVarausId(anyInt())).thenReturn(varaus);

        Varaus result = varausController.findByVarausId(1);
        assertNotNull(result);
        assertEquals(1, result.getVarausId());
    }
}