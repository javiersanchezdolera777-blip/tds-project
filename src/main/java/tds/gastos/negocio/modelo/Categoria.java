package tds.gastos.negocio.modelo;

import java.util.Locale;
import java.util.Objects;

import com.google.common.base.Preconditions;  // Precondiciones de Guava

public class Categoria {

    private Integer id;
    private final String nombre;
    private final boolean predefinida;

    
     // Crea una categoría personalizada (aún sin persistir en base de datos)
    public Categoria(String nombre) {
        this(null, nombre, false);
    }

     // Crea una categoría indicando si es predefinida del sistema (sin persistir)
    public Categoria(String nombre, boolean predefinida) {
        this(null, nombre, predefinida);
    }

    // Constructor completo
    public Categoria(Integer id, String nombre, boolean predefinida) {
        Preconditions.checkArgument(id == null || id > 0, "El id de la categoría debe ser positivo");
        Preconditions.checkNotNull(nombre, "El nombre de la categoría no puede ser nulo");

        String nombreNormalizado = nombre.trim();
        Preconditions.checkArgument(!nombreNormalizado.isEmpty(), "El nombre de la categoría no puede estar vacío");

        this.id = id;
        this.nombre = nombreNormalizado;
        this.predefinida = predefinida;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        Preconditions.checkNotNull(id, "El id no puede ser nulo");
        Preconditions.checkArgument(id > 0, "El id debe ser estrictamente positivo");
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isPredefinida() {
        return predefinida;
    }

    private String getNombreNormalizado() {
        return nombre.toLowerCase(Locale.ROOT);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Categoria other)) {
            return false;
        }
        return Objects.equals(getNombreNormalizado(), other.getNombreNormalizado());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNombreNormalizado());
    }

    @Override
    public String toString() {
        return nombre;
    }
}