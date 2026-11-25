package tds.gastos.negocio.modelo;

import java.time.LocalDateTime;
import java.util.Objects;

import com.google.common.base.Preconditions;

public class Gasto {

    private Integer id;
    private double importe;
    private LocalDateTime fecha;
    private Categoria categoria;
    private String descripcion;

     // Crea un gasto sin descripción (aún sin persistir en base de datos)
    public Gasto(double importe, LocalDateTime fecha, Categoria categoria) {
        this(null, importe, fecha, categoria, "");
    }

    // Crea un gasto con descripción opcional (aún sin persistir en base de datos)
    public Gasto(double importe, LocalDateTime fecha, Categoria categoria, String descripcion) {
        this(null, importe, fecha, categoria, descripcion);
    }

    // Constructor completo, útil al recuperar un gasto desde la base de datos
    public Gasto(Integer id, double importe, LocalDateTime fecha, Categoria categoria, String descripcion) {
        Preconditions.checkArgument(id == null || id > 0, "El id del gasto debe ser positivo");
        this.id = id;
        setImporte(importe);
        setFecha(fecha);
        setCategoria(categoria);
        setDescripcion(descripcion);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        Preconditions.checkNotNull(id, "El id no puede ser nulo");
        Preconditions.checkArgument(id > 0, "El id debe ser estrictamente positivo");
        this.id = id;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        Preconditions.checkArgument(Double.isFinite(importe) && importe > 0,
                "El importe del gasto debe ser un número estrictamente mayor que 0");
        this.importe = importe;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        Preconditions.checkNotNull(fecha, "La fecha del gasto no puede ser nula");
        this.fecha = fecha;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        Preconditions.checkNotNull(categoria, "La categoría del gasto no puede ser nula");
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = (descripcion == null) ? "" : descripcion.trim();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Gasto other)) {
            return false;
        }
        if (this.id == null || other.id == null) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format("Gasto[id=%s, fecha=%s, categoria=%s, importe=%.2f€, descripcion='%s']",
                id, fecha, categoria.getNombre(), importe, descripcion);
    }
}