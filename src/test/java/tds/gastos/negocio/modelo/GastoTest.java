package tds.gastos.negocio.modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class GastoTest {

    private final Categoria comida = new Categoria(1, "Comida", true);
    private final LocalDateTime fecha = LocalDateTime.of(2026, 11, 25, 14, 30);

    @Test
    public void crearGastoValidoYNormalizarDescripcion() {
        Gasto g1 = new Gasto(12.50, fecha, comida, "  Menú del día  ");
        Gasto g2 = new Gasto(5.0, fecha, comida, null);

        assertNull(g1.getId());
        assertEquals(12.50, g1.getImporte());
        assertEquals(fecha, g1.getFecha());
        assertEquals(comida, g1.getCategoria());
        assertEquals("Menú del día", g1.getDescripcion());
        assertEquals("", g2.getDescripcion());
    }

    @Test
    public void editarGastoActualizaCampos() {
        Gasto gasto = new Gasto(1, 10.0, fecha, comida, "Café");
        Categoria ocio = new Categoria(2, "Ocio", true);
        LocalDateTime nuevaFecha = fecha.plusDays(1);

        gasto.setImporte(25.75);
        gasto.setFecha(nuevaFecha);
        gasto.setCategoria(ocio);
        gasto.setDescripcion("Cine");

        assertEquals(25.75, gasto.getImporte());
        assertEquals(nuevaFecha, gasto.getFecha());
        assertEquals(ocio, gasto.getCategoria());
        assertEquals("Cine", gasto.getDescripcion());
    }

    @Test
    public void rechazaImporteNoValido() {
        assertThrows(IllegalArgumentException.class, () -> new Gasto(0, fecha, comida));
        assertThrows(IllegalArgumentException.class, () -> new Gasto(-10.5, fecha, comida));
        assertThrows(IllegalArgumentException.class, () -> new Gasto(Double.NaN, fecha, comida));
        assertThrows(IllegalArgumentException.class, () -> new Gasto(Double.POSITIVE_INFINITY, fecha, comida));
    }

    @Test
    public void rechazaFechaOCategoriaNula() {
        assertThrows(NullPointerException.class, () -> new Gasto(15.0, null, comida));
        assertThrows(NullPointerException.class, () -> new Gasto(15.0, fecha, null));
    }

    @Test
    public void igualdadBasadaEnId() {
        Gasto sinId1 = new Gasto(5.0, fecha, comida);
        Gasto sinId2 = new Gasto(5.0, fecha, comida);
        Gasto conId1 = new Gasto(10, 5.0, fecha, comida, "A");
        Gasto conId2 = new Gasto(10, 20.0, fecha, comida, "B");
        Gasto conOtroId = new Gasto(11, 5.0, fecha, comida, "A");

        assertNotEquals(sinId1, sinId2);
        assertEquals(sinId1, sinId1);
        assertEquals(conId1, conId2);
        assertEquals(conId1.hashCode(), conId2.hashCode());
        assertNotEquals(conId1, conOtroId);
    }
}