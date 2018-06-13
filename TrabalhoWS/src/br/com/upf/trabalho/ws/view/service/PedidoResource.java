package br.com.upf.trabalho.ws.view.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.upf.trabalho.ws.beans.Pedido;
import br.com.upf.trabalho.ws.enumPedido.LojaPedido;
import br.com.upf.trabalho.ws.enumPedido.StatusPedido;

@Path("PedidoResource")
public class PedidoResource {
	
	public static List<Pedido> pedidos;//banco de dados fake
	public static HashMap<Long, Pedido> hashPedidos;
	
	
	
	static {
		pedidos = new ArrayList<>();
		hashPedidos = new HashMap<>();
	}
	
	@Path("inserePedido")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response InserePedido(String pedidoJson) {
		
		Gson gson=  new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
		//May 25, 2018 5:01:30 PM
		
		Status status = null;	
		String msg = null;
		Pedido pedido = gson.fromJson(pedidoJson, Pedido.class);
		System.out.println("sa" + pedido.getDataVenda());
		try {
			if(pedido.getId() == 0){
				msg = "Id do Pedido não informado.";
				status = Status.NOT_FOUND;	
				
			}else if(pedido.getDataVenda() == null){
				msg = "Data da venda do pedido não informada.";
				status = Status.NOT_FOUND;	
				System.out.println(pedido.getStatusAtualPedido());
			}else if (pedido.getStatusAtualPedido() != StatusPedido.PA  &&
					  pedido.getStatusAtualPedido() != StatusPedido.PF 	&&
					  pedido.getStatusAtualPedido() != StatusPedido.PET &&
					  pedido.getStatusAtualPedido() != StatusPedido.PE){
				
					msg = "Deve ser informado status validos: PA=aprovado, PF=faturado, PET=enviado Transportadora, PE=Entregue.";
					status = Status.NOT_FOUND;
					
			}else if(pedido.getLoja() != LojaPedido.AR && 
					pedido.getLoja() != LojaPedido.PB  &&
					pedido.getLoja() != LojaPedido.RE  &&
					pedido.getLoja() != LojaPedido.NS  &&
					pedido.getLoja() != LojaPedido.UPF){
				msg = "Deve ser informada uma loja Valida: PB=PEDROSTORE, AR=ANDRESTORE, UPF=UPFSTORE, RE=RICARDOSTORE, NS=NSEISTORE";
				status = Status.NOT_FOUND;
				
			}else if (pedido.getTotalPedido() < 0){
				msg = "Total do pedido deve ser maior que zero.";
				status = Status.NOT_FOUND;	
				
			}else if(pedido.getCliente().getId() == 0){
				msg = "Id do cliente não informado.";
				status = Status.NOT_FOUND;
			}else{
				
				boolean flag = false;
				for( Pedido ped: pedidos){
					if(ped.getId() == pedido.getId()) {
						flag=true;
					}
				}				
				if(flag==false) {
					pedidos.add(pedido);
					msg = "Pedido integrado com sucesso!";
					hashPedidos.put(pedido.getId(), pedido);
					status = Status.OK;
				}else {
					msg = "Já existe um pedido com este ID, operação não executada!";
					status = Status.BAD_REQUEST;
				}
			}
			
		} catch (Exception e) {
			return Response.serverError().build();
		}
		
		return Response
				.status(status)
				.type(MediaType.APPLICATION_JSON)
				.entity(new Gson().toJson(msg))
				.build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getPedidos(){
		return new Gson().toJson(pedidos);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("pedidosBylojaEstatus/status/{statusAtualPedido}/loja/{loja}")
	public String getPedidoByLojaeStatus(@PathParam("statusAtualPedido") String status, @PathParam("loja") String loja){
		//http://localhost:8081/TrabalhoWS/api/v1/PedidoResource/pedidosBylojaEstatus/status/PE/loja/AR
		
		Gson json = new Gson();
	
		List<Pedido> ordersbyLeS = new ArrayList<>();

		for( Pedido ped: pedidos){
			
			if(ped.getStatusAtualPedido().toString().equals(status) && ped.getLoja().toString().equals(loja)){
				ordersbyLeS.add(ped);
				
			}
		}
		if(ordersbyLeS.size() > 0){
			return json.toJson(ordersbyLeS);
		}else{
			return json.toJson("Nenhum Pedido encontado!");
	}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("coupons/clienteId/{id}")
	public String getCouponbyCliente(@PathParam("id") Long id){
		
		Gson json = new Gson();
	
		List<String> coupons = new ArrayList<>();

		for( Pedido ped: pedidos){
			if(ped.getCliente().getId().equals(id)){
				coupons.add(ped.getCoupon());
				
			}
		}
		if(coupons.size() > 0){
			return json.toJson(coupons);
		}else{
			return json.toJson("Nenhum coupon vinculado a esse Cliente");
	}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes
	@Path("pedidos")
	public String valortoTaldePedidos(String jsonPedido){
		
		Gson gson=  new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();

		Pedido pedidode = gson.fromJson(jsonPedido, Pedido.class);
		System.out.println("TA vindo " + pedidode.getLoja() +" data " + pedidode.getDataVenda());
		List<Pedido> orders = new ArrayList<>();

		for( Pedido ped: pedidos){
			//Date dataformatada = parseDate(pedidode.getDataVenda().toString());
			System.out.println("data no for " + pedidode.getDataVenda());
			System.out.println("VERDADEIRO EQUALS LOJA" + ped.getLoja().equals(pedidode.getLoja()));
			System.out.println("VERDADEIRO EQUALS Data" + ped.getDataVenda().equals(pedidode.getDataVenda()));
			if(ped.getLoja().equals(pedidode.getLoja()) && ped.getDataVenda().equals(pedidode.getDataVenda())){
				orders.add(ped);
	
			}
		}
		if(orders.size() > 0){
			return gson.toJson(orders);
		}else{
			return gson.toJson("Nenhum pedido para a data e loja informados ");
	}
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("couponPedidos/{coupon}")
	public String getPedidosbyId(@PathParam("coupon") String coupon){
		
		Gson json = new Gson();
		List<Pedido> orders = new ArrayList<>();
		
		for( Pedido ped: pedidos){
			if(ped.getCoupon().equals(coupon)){
				orders.add(ped);
			}	
		}

		if(orders.size() > 0){
			return json.toJson(orders);
		}else{
			return json.toJson("Nenhum pedido vinculado ao cupon");
	}
}
	
	@DELETE
	@Path("{id}")
	public Response removePedido(@PathParam("id") Long id){
	
		for( Pedido ped: pedidos){
			if(ped.getId()==id){
				pedidos.remove(ped);
			}
		}
		
		try {
			if (!hashPedidos.containsKey(id)){
				hashPedidos.remove(id);
				return Response.ok("{\"status\":\"erro: pedido não removido\"}").build();
				
			} else {
				hashPedidos.remove(id);
				return Response.ok("{\"status\":\"sucesso: pedido removido\"}").build();
				
			}
		} catch (Exception e) {

			return Response.ok("{\"status\":\"erro: não informado daods\"}" + e).build();
		}
		
		
	}
	

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response alteraProduto(String pedJson){
		Status status = null;	
		String msg = null;
		Pedido pedido = new Gson().fromJson(pedJson, Pedido.class);

		try {
			if(pedido.getId() == 0){
				msg = "Id do Pedido não informado.";
				status = Status.NOT_FOUND;	
				
			}else if(pedido.getDataVenda() == null){
				msg = "Data da venda do pedido não informada.";
				status = Status.NOT_FOUND;	
				System.out.println(pedido.getStatusAtualPedido());
			}else if (pedido.getStatusAtualPedido() != StatusPedido.PA  &&
					  pedido.getStatusAtualPedido() != StatusPedido.PF &&
					  pedido.getStatusAtualPedido() != StatusPedido.PET &&
					  pedido.getStatusAtualPedido() != StatusPedido.PE){
				
					msg = "Deve ser informado status validos: PA=aprovado, PF=faturado, PET=enviado Transportadora, PE=Entregue.";
					status = Status.NOT_FOUND;
					
			}else if(pedido.getLoja() != LojaPedido.AR && 
					pedido.getLoja() != LojaPedido.PB &&
					pedido.getLoja() != LojaPedido.RE &&
					pedido.getLoja() != LojaPedido.NS &&
					pedido.getLoja() != LojaPedido.UPF){
				msg = "Deve ser informada uma loja Valida: PB=PEDROSTORE, AR=ANDRESTORE, UPF=UPFSTORE, RE=RICARDOSTORE, NS=NSEISTORE";
				status = Status.NOT_FOUND;
				
			}else if (pedido.getTotalPedido() < 0){
				msg = "Total do pedido deve ser maior que zero.";
				status = Status.NOT_FOUND;	
				
			}else if(pedido.getCliente().getId() == 0){
				msg = "Id do cliente não informado.";
				status = Status.NOT_FOUND;
				
			}else{
				boolean flag=false;
				for( Pedido ped: pedidos){
					if(ped.getId()==pedido.getId()){
						pedidos.remove(ped);
						pedidos.add(pedido);
						flag=true;
					}
				}
				if(flag==true) {
					hashPedidos.put(pedido.getId(), pedido);
					msg = "Pedido alterado com Sucesso!";
					status = Status.OK;
				}else {
					msg = "Pedido não encontrado!";
					status = Status.BAD_REQUEST;
				}
				
			}
			
		} catch (Exception e) {
			return Response.serverError().build();
		}
		
		return Response
				.status(status)
				.type(MediaType.APPLICATION_JSON)
				.entity(new Gson().toJson(msg))
				.build();
	}
	

	private Date parseDate(String date) {
		try {
			return new SimpleDateFormat("dd/MM/yyyy").parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
