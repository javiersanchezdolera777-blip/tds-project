package tds.gastos.negocio.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CategoriaTest {

    @Test
    public void crearCategoriaValidaNormalizaEspacios() {
        Categoria cat = new Categoria("  Alimentación  ");

        assertNull(cat.getId());
        assertEquals("Alimentación", cat.getNombre());
        assertFalse(cat.isPredefinida());
        assertEquals("Alimentación", cat.toString());
    }

    @Test
    public void crearCategoriaPredefinidaConId() {
        Categoria cat = new Categoria(1, "Transporte", true);

        assertEquals(1, cat.getId());
        assertEquals("Transporte", cat.getNombre());
        assertTrue(cat.isPredefinida());
    }

    @Test
    public void rechazaNombreNuloOVacio() {
        assertThrows(NullPointerException.class, () -> new Categoria(null));
        assertThrows(IllegalArgumentException.class, () -> new Categoria(""));
        assertThrows(IllegalArgumentException.class, () -> new Categoria("   "));
    }

    @Test
    public void rechazaIdNoPositivo() {
        assertThrows(IllegalArgumentException.class, () -> new Categoria(0, "Ocio", false));
        assertThrows(IllegalArgumentException.class, () -> new Categoria(-3, "Ocio", false));

        Categoria cat = new Categoria("Ocio");
        assertThrows(NullPointerException.class, () -> cat.setId(null));
        assertThrows(IllegalArgumentException.class, () -> cat.setId(0));
    }

    @Test
    public void igualdadIgnoraMayusculasYEspacios() {
        Categoria c1 = new Categoria("Comida");
        Categoria c2 = new Categoria("  comida  ", true);
        Categoria c3 = new Categoria("Transporte");

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotEquals(c1, c3);
    }
}