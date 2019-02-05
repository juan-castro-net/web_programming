package org.comercio.producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.comercio.utilidad.BaseDato;

@Path("/producto")
public class ProductoServicio {
	@POST
	@Path("/adicionar")
	@Consumes({ "application/json" })
	@Produces("application/json")
	public Response adicionar(Producto producto) {
		BaseDato basedato = new BaseDato();
		Connection conexion1 = null;
		PreparedStatement sentenciaPreparada1 = null;
		String sql = "";
		String mensaje = "";
		int insertados = 0;
		try {
			conexion1 = basedato.getConexion();
			sql = "INSERT INTO producto (pro_nombre, pro_precio)";
			sql = sql + " VALUES (?,?)";

			sentenciaPreparada1 = conexion1.prepareStatement(sql);
			sentenciaPreparada1.setString(1, producto.getNombre());
			sentenciaPreparada1.setDouble(2, producto.getPrecio());
			insertados = sentenciaPreparada1.executeUpdate();
		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
		}
		if (insertados > 0) {
			mensaje = "{\"mensaje\":\"Adicionar OK\"}";
			return Response.status(200).entity(mensaje).build();
		} else {
			mensaje = "{\"mensaje\":\"Error al adicionar\"}";
			return Response.status(400).entity(mensaje).build();
		}
	}

	@PUT
	@Path("/modificar/{codigo}")
	@Consumes({ "application/json" })
	@Produces("application/json")
	public Response modificar(Producto producto, @PathParam(value = "codigo") int codigo) {
		BaseDato basedato = new BaseDato();
		Connection conexion1 = null;
		PreparedStatement sentenciaPreparada1 = null;
		String sql = "";
		String mensaje = "";
		int modificados = 0;
		try {
			conexion1 = basedato.getConexion();
			sql = "UPDATE producto set pro_nombre=?, pro_precio=?";
			sql = sql + " WHERE pro_codigo=?";

			sentenciaPreparada1 = conexion1.prepareStatement(sql);
			sentenciaPreparada1.setString(1, producto.getNombre());
			sentenciaPreparada1.setDouble(2, producto.getPrecio());
			sentenciaPreparada1.setInt(3, codigo);
			modificados = sentenciaPreparada1.executeUpdate();
		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
		}
		// return Response.ok().entity(mensaje).build();
		if (modificados > 0) {
			mensaje = "{\"mensaje\":\"Modificar OK\"}";
			return Response.status(200).entity(mensaje).build();
		} else {
			mensaje = "{\"mensaje\":\"Error al modificar\"}";
			return Response.status(400).entity(mensaje).build();
		}
	}

	@DELETE
	@Path("/eliminar/{codigo}")
	@Consumes({ "application/json" })
	@Produces("application/json")
	public Response adicionar(@PathParam(value = "codigo") int codigo) {
		BaseDato basedato = new BaseDato();
		Connection conexion1 = null;
		PreparedStatement sentenciaPreparada1 = null;
		String sql = "";
		String mensaje = "";
		int eliminados = 0;
		try {
			conexion1 = basedato.getConexion();
			sql = "DELETE FROM producto WHERE pro_codigo=?";

			sentenciaPreparada1 = conexion1.prepareStatement(sql);
			sentenciaPreparada1.setInt(1, codigo);

			eliminados = sentenciaPreparada1.executeUpdate();
		} catch (Exception e) {

			System.out.println("Error: " + e.toString());
		}
		if (eliminados > 0) {
			mensaje = "{\"mensaje\":\"Eliminar OK\"}";
			return Response.status(200).entity(mensaje).build();
		} else {
			mensaje = "{\"mensaje\":\"Error al eliminar\"}";
			return Response.status(400).entity(mensaje).build();
		}
	}

	@GET
	@Path("/listar")
	@Produces("application/json")
	// @Produces("application/xml")
	public ProductoLista getProductos() {
		ArrayList lista = new ArrayList();
		BaseDato basedato = new BaseDato();
		Connection conexion1 = null;
		Statement sentencia1 = null;
		ResultSet rs1 = null;
		String sql = "";
		try {
			conexion1 = basedato.getConexion();
			sentencia1 = conexion1.createStatement();
			sql = "select * from producto";

			rs1 = sentencia1.executeQuery(sql);

			while (rs1.next()) {
				int codigo = rs1.getInt("pro_codigo");
				String nombre = rs1.getString("pro_nombre");
				double precio = rs1.getDouble("pro_precio");

				Producto producto = new Producto();

				producto.setCodigo(codigo);
				producto.setNombre(nombre);
				producto.setPrecio(precio);

				lista.add(producto);
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
		}
		return new ProductoLista(lista);
	}
}
