package br.com.upf.trabalho.ws.view.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.sun.jersey.api.client.ClientResponse.Status;

import br.com.upf.trabalho.ws.beans.Cliente;

@Path("ClienteResource")
public class ClienteResource {

	public static List<Cliente> clientes;
	public static HashMap<Long, Cliente> hashClientes;

	static {
		clientes = new ArrayList<>();
		hashClientes = new HashMap<>();
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED) // formulario html
	public Response insertPostByForm(
			@FormParam("id") Long id, 
			@FormParam("nome") String nome,
			@FormParam("sobrenome") String sobrenome, 
			@FormParam("cpf") String cpf, 
			@FormParam("rg") String rg,
			@FormParam("telefone") String telefone, 
			@FormParam("email") String email,
			@FormParam("siteId") String siteId) {
		Cliente cliente = new Cliente();
		cliente.setId(id);
		cliente.setNome(nome);
		cliente.setSobrenome(sobrenome);
		cliente.setCpf(cpf);
		cliente.setRg(rg);
		cliente.setTelefone(telefone);
		cliente.setEmai(email);
		cliente.setSiteId(siteId);

		Status status = null;
		String msg = null;

		try {
			if (cliente.getId() == 0 || cliente.getId() == null) {
				msg = "Id do Cliente não informado.";
				status = Status.NOT_FOUND;
			} else if (cliente.getNome() == null) {
				msg = "Nome do Cliente não informado.";
				status = Status.NOT_FOUND;
			} else if (cliente.getSobrenome() == null) {
				msg = "Sobrenome do Cliente não informado.";
				status = Status.NOT_FOUND;
			} else if (cliente.getCpf() == null) {
				msg = "CPF do Cliente não informado.";
				status = Status.NOT_FOUND;
			} else if (cliente.getRg() == null) {
				msg = "Rg do Cliente não informado.";
				status = Status.NOT_FOUND;
			} else if (cliente.getTelefone() == null) {
				msg = "Telefone do Cliente não informado.";
				status = Status.NOT_FOUND;
			} else if (cliente.getEmai() == null) {
				msg = "Email do Cliente não informado.";
				status = Status.NOT_FOUND;
			} else if (cliente.getSiteId() == null) {
				msg = "ID da loja deve ser informado.";
				status = Status.NOT_FOUND;
			} else {
				clientes.add(cliente);
				hashClientes.put(cliente.getId(), cliente);
				return Response.ok("{\"status\":\"sucesso\"}").build();
			}

		} catch (Exception e) {

			return Response.serverError().build();
		}

		return Response.status(status).type(MediaType.APPLICATION_JSON).entity(new Gson().toJson(msg)).build();
	}

	@Path("insereCliente")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response InserePedido(String ClienteJson) {
		Status status = null;
		String msg = null;
		Cliente cliente = new Gson().fromJson(ClienteJson, Cliente.class);

		try {
			if (cliente.getId() == 0) {
				msg = "Id do Cliente não informado.";
				status = Status.NOT_FOUND;
			} else if (cliente.getNome() == null) {
				msg = "Nome do Cliente não informado.";
				status = Status.NOT_FOUND;
			} else if (cliente.getSobrenome() == null) {
				msg = "Sobrenome do Cliente não informado.";
				status = Status.NOT_FOUND;
			} else if (cliente.getCpf() == null) {
				msg = "CPF do Cliente não informado.";
				status = Status.NOT_FOUND;
			} else if (cliente.getRg() == null) {
				msg = "Rg do Cliente não informado.";
				status = Status.NOT_FOUND;
			} else if (cliente.getTelefone() == null) {
				msg = "Telefone do Cliente não informado.";
				status = Status.NOT_FOUND;
			} else if (cliente.getEmai() == null) {
				msg = "Email do Cliente não informado.";
				status = Status.NOT_FOUND;
			} else if (cliente.getSiteId() == null) {
				msg = "ID da loja deve ser informado.";
				status = Status.NOT_FOUND;
			} else {
				clientes.add(cliente);
				hashClientes.put(cliente.getId(), cliente);
				return Response.ok("{\"status\":\"sucesso\"}").build();
			}

		} catch (Exception e) {

			return Response.serverError().build();
		}

		return Response.status(status).type(MediaType.APPLICATION_JSON).entity(new Gson().toJson(msg)).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getCliente() {
		return new Gson().toJson(clientes);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("buscaClientePorCpf/{cpf}")
	public String getClientebyCpf(@PathParam("cpf") String cpf) {
		Gson json = new Gson();
		List<Cliente> costumer = new ArrayList<>();
		
		for (Cliente cl : clientes) {
			if (cl.getCpf().equals(cpf)) {
				costumer.add(cl);
			}
		}

		if (costumer.size() > 0) {
			return json.toJson(costumer);
		} else {
			return json.toJson("Nenhum cliente com esse CPF");
		}
	}

	@DELETE
	@Path("{id}")
	public Response removeCliente(@PathParam("id") Long id) {
		Status status = null;
		String msg = null;
		try {
			if (!hashClientes.containsKey(id)) {
				msg = "Cliente não encontrado.";
				status = Status.NOT_FOUND;
			} else {
				hashClientes.remove(id);
				msg = "Cliente removido.";
				status = Status.OK;

			}
		} catch (Exception e) {
			return Response.serverError().build();
		}

		return Response.status(status).type(MediaType.APPLICATION_JSON).entity(new Gson().toJson(msg)).build();

	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response alteraCliente(String clienteJson) {

		Status status = null;
		String msg = null;
		Cliente cliente = new Gson().fromJson(clienteJson, Cliente.class);

		try {
			if (cliente.getId() == 0) {
				msg = "Id do Cliente não informado.";
				status = Status.NOT_FOUND;
			} else if (cliente.getNome() == null) {
				msg = "Nome do Cliente não informado.";
				status = Status.NOT_FOUND;
			} else if (cliente.getSobrenome() == null) {
				msg = "Sobrenome do Cliente não informado.";
				status = Status.NOT_FOUND;
			} else if (cliente.getCpf() == null) {
				msg = "CPF do Cliente não informado.";
				status = Status.NOT_FOUND;
			} else if (cliente.getRg() == null) {
				msg = "Rg do Cliente não informado.";
				status = Status.NOT_FOUND;
			} else if (cliente.getTelefone() == null) {
				msg = "Telefone do Cliente não informado.";
				status = Status.NOT_FOUND;
			} else if (cliente.getEmai() == null) {
				msg = "Email do Cliente não informado.";
				status = Status.NOT_FOUND;
			} else if (cliente.getSiteId() == null) {
				msg = "ID da loja deve ser informado.";
				status = Status.NOT_FOUND;
			} else {
				hashClientes.put(cliente.getId(), cliente);
				return Response.ok("{\"status\":\"sucesso: campo alterado\"}").build();

			}
		} catch (Exception e) {

			return Response.serverError().build();
		}
		return Response.status(status).type(MediaType.APPLICATION_JSON).entity(new Gson().toJson(msg)).build();

	}

}
