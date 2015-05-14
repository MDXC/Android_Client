package com.travel.flight.order.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.travel.entity.flight.FlightOrder;
import com.travel.entity.flight.FlightPassenger;
import com.travel.entity.flight.FlightTrip;
import com.travel.flight.agent.api.PriceService;
import com.travel.flight.agent.api.entity.Flight;
import com.travel.flight.agent.api.entity.Price;
import com.travel.flight.agent.api.entity.PriceRequest;
import com.travel.flight.agent.api.entity.PriceResponse;
import com.travel.flight.agent.api.entity.Remark;
import com.travel.flight.db.api.AirlineService;
import com.travel.flight.db.api.AirportService;
import com.travel.flight.db.api.CityService;
import com.travel.flight.db.api.entity.Order;
import com.travel.flight.order.dto.FlightOrderDTO;
import com.travel.mapper.flight.FlightOrderMapper;
import com.travel.mapper.flight.FlightPassengerMapper;
import com.travel.mapper.flight.FlightTripMapper;

/**
 * Created by myliang on 3/14/15.
 */

@Controller
@RequestMapping("/orders/")
public class OrderController {
	
	@Autowired
    private PriceService priceService;
    
	@Autowired
    private AirportService airportService;
    
	@Autowired
    private AirlineService airlineService;
    
	@Autowired
    private CityService cityService;
	
	@Autowired
	private FlightOrderMapper flightOrderMapper;
	
	@Autowired
	private FlightPassengerMapper flightPassengerMapper;
	
	@Autowired
	private FlightTripMapper flightTripMapper;
	
	@RequestMapping(value = "toorder")
	public String toOrder(Model model, PriceRequest priceRequest, HttpServletRequest req, HttpServletResponse res){
		
		PriceResponse priceRes = this.priceService.query(priceRequest);
		
		//assemble order object
		Price price = priceRes.getPrices().get(0);
		FlightOrderDTO orderDTO = new FlightOrderDTO();
		
		orderDTO.setCurrency(price.getCurrency());
		orderDTO.setAmount(price.getPrice()==null ? 0:Double.parseDouble(price.getPrice()));
		orderDTO.setPublishPrice(price.getPublishPrice()==null ? 0:Double.parseDouble(price.getPublishPrice()));
		orderDTO.setTaxes(price.getTaxes()==null?0:Double.parseDouble(price.getTaxes()));
		orderDTO.setSurcharge(price.getSurcharge()==null?0:Double.parseDouble(price.getSurcharge()));
		orderDTO.setExpression(price.getExpression());
		orderDTO.setRefund(price.getRefund()==null?null:price.getRefund().getState().name());
		orderDTO.setReroute(price.getReroute()==null?null:price.getReroute().getState().name());
		
		//baggage info
		JSONArray arryBaggage = new JSONArray();
		if(price.getFromBaggage()!=null){
			arryBaggage.add(price.getFromBaggage().getContent());
		}
		if(price.getToBaggage()!=null){
			arryBaggage.add(price.getToBaggage().getContent());
		}
		if(arryBaggage.size()!=0){
			orderDTO.setBaggages(arryBaggage.toString());
		}
		
		//remarks info
		if(CollectionUtils.isNotEmpty(price.getRemark())){
			JSONArray arryRemarks = new JSONArray();
			for(Remark remark : price.getRemark()){
				arryRemarks.add(remark.getContent());
			}
			orderDTO.setRemarks(arryRemarks.toString());
		}
		
		//assemble flight info
		orderDTO.setTripType(priceRequest.getTripType().getValue());
		
		orderDTO.setAdults(priceRequest.getAdults());
		orderDTO.setChildren(priceRequest.getChildren());
		orderDTO.setInfants(priceRequest.getInfants());
		
		Map<String, Flight> mapFlights = priceRes.getFlights();
		JSONArray arryFlights = new JSONArray();
		for(int i=0;i<price.getFromNumbers().size();i++){
			String strFlight = price.getFromNumbers().get(i);
			Flight flight = mapFlights.get(strFlight);
			JSONObject jsonFlight = this.transferFlight(flight);
			jsonFlight.put("clazz", price.getFromCabin().name());
			jsonFlight.put("sortNo",this.getIndex(1, i));
			arryFlights.add(jsonFlight);
		}
		
		if(CollectionUtils.isNotEmpty(price.getToNumbers())){
			for(int i=0;i<price.getToNumbers().size();i++){
				String strFlight = price.getToNumbers().get(i);
				Flight flight = mapFlights.get(strFlight);
				JSONObject jsonFlight = this.transferFlight(flight);
				jsonFlight.put("clazz", price.getFromCabin().name());
				jsonFlight.put("sortNo",this.getIndex(2, i));
				arryFlights.add(jsonFlight);
			}			
		}
		orderDTO.setTrips(mapFlights.toString());
		
		model.addAttribute("orderDTO", orderDTO);
		
		return "order_one";
	}
	
	private JSONObject transferFlight(Flight flight){
		JSONObject jsonFlight = new JSONObject();
		jsonFlight.put("flight", flight.getNumber());
		jsonFlight.put("departTime", flight.getFromTime());
		jsonFlight.put("arrivalTime", flight.getToAirport());
		jsonFlight.put("fromCity", flight.getFromCity());
		jsonFlight.put("fromAirport", flight.getFromAirport());
		jsonFlight.put("toCity", flight.getToCity());
		jsonFlight.put("toAirport", flight.getToAirport());
		jsonFlight.put("airline", flight.getAirline());
		jsonFlight.put("aircraft", flight.getAircraft());
		return jsonFlight;
	}

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public String index() {
    	FlightOrder order = this.flightOrderMapper.queryById(1234);
    	if(order!=null){
    		System.out.println(order.getContactName());
    	}
    	return "1234567890";
    }
    
    @ResponseBody
    @RequestMapping(value = "new")
    public String nw(FlightOrderDTO orderDTO) {
    	try{
	    	FlightOrder flightOrder = orderDTO.drawFlightOrder();
	    	List<FlightPassenger> flightPass = orderDTO.drawFlightPassenger();
	    	List<FlightTrip> flightTrips = orderDTO.drawFlightTrip();
	    	flightOrder.setCreateTime(new Date());
	    	flightOrder.setState(0);
	    	this.flightOrderMapper.insert(flightOrder);
	    	
	    	for(FlightPassenger pass : flightPass){
	    		pass.setCreateTime(new Date());
	    		pass.setOrderId(flightOrder.getId());
	    		this.flightPassengerMapper.insert(pass);
	    	}
	    	
	    	for(FlightTrip trip : flightTrips){
	    		trip.setCreateTime(new Date());
	    		trip.setOrderId(flightOrder.getId());
	    		this.flightTripMapper.insert(trip);
	    	}
	        return flightOrder.getId()+"";
    	}catch(Exception e){
    		e.printStackTrace();
    		return "fail";
    	}
    }
    
    @ResponseBody
    @RequestMapping(value = "cancel", method = RequestMethod.GET)
    public String cancel(int id) {
    	try{
	    	FlightOrder flightOrder = this.flightOrderMapper.queryById(id);
	    	if(flightOrder==null){
	    		return "order not exists";
	    	}
	    	flightOrder.setState(21);
	    	this.flightOrderMapper.updateState(flightOrder);
    	}catch(Exception e){
    		e.printStackTrace();
    		return "error";
    	}	
        return "success";
    }
    
    @ResponseBody
	@RequestMapping(value = "user/{userId}", method = RequestMethod.GET)
    public String getByUserId(@PathVariable String userId){
    	List<FlightOrder> listOrders = this.flightOrderMapper.queryByUserId(userId);
    	if(CollectionUtils.isNotEmpty(listOrders)){
    		JSONArray jsonArrayRes = new JSONArray();
    		for(FlightOrder order:listOrders){
    			FlightOrderDTO orderDTO = new FlightOrderDTO();
    			List<FlightPassenger> listPass = this.flightPassengerMapper.queryByOrderId(order.getId());
    			List<FlightTrip> listTrips = this.flightTripMapper.queryByOrderId(order.getId());

    			
    			jsonArrayRes.add(orderDTO.drawDTO(order, listPass, listTrips));
    		}
    		return jsonArrayRes.toString();
    	}
    	return "error";
    }
    
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String create(Order order) {
        return null;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Order show(@PathVariable String id) {
        return null;
    }

    @RequestMapping(value = "{id}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable String id) {
        return null;
    }
    
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public String update(@PathVariable String id) {
        return null;
    }
    
    private int getIndex(int prefix,int subIndex){
    	int intTemp = prefix << 26;
    	return intTemp | subIndex;
    }
    
    public static void main(String[] args) {
		int intTemp = 1<<26;
		int intTemp1 = intTemp | 1;
		System.out.println(Integer.toBinaryString(intTemp1));
	}

}
