package com.restapi.BundlesProject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class BundlesController {

  private final BundleRepository repository;

  BundlesController(BundleRepository repository) {
    this.repository = repository;
    
    //CHECK EVERY 30 SECONDS FOR EXPIRED BUNDLES AND DEACTIVATE THEM
	Timer timer = new Timer ();
	TimerTask t = new TimerTask () {
	    @Override
	    public void run () {
	  	  	for(Bundle bundle : repository.findAll()) {
		  	  	Timestamp ts=new Timestamp(System.currentTimeMillis());  
	  	  		if(bundle.getExpiration()!=null && bundle.getExpiration().before(ts) && bundle.getActivated()) {
	  	  			bundle.setActivated(false);
	  	  			repository.save(bundle);
	  	  			System.out.println("-----------[ALERT BUNDLE DEACTIVATED]-----------");
	  	  			System.out.println("Bundle with code="+bundle.getCode()+" and name="+bundle.getName()+" deactivated because expired.");
	  	  			System.out.println("________________________________________________");
	  	  		}
		  	  	if(bundle.getAvailability().after(ts) && bundle.getActivated()) {
		  	  		bundle.setActivated(false);
		  	  		repository.save(bundle);
		  	  		System.out.println("-----------[ALERT BUNDLE DEACTIVATED]-----------");
	  	  			System.out.println("Bundle with code="+bundle.getCode()+" and name="+bundle.getName()+" deactivated because is not available yet.");
	  	  			System.out.println("________________________________________________");
		  	  	}
	  	  			
	  	    }
	    }
	};
	timer.schedule (t, 0l, 1000*30);
  }
  
  //GET ALL BUNDLES ASCENDING BY CODE
  @GetMapping(value="/bundles", produces = "application/json")
  ResponseEntity<List<Bundle>> all() {
	  	List<Bundle> bundles = repository.findAll();
	  	ResponseEntity<List<Bundle>> response;
		if(!bundles.isEmpty()) 
			response = new ResponseEntity<List<Bundle>>(bundles, HttpStatus.OK);
		else
			response = new ResponseEntity<List<Bundle>>(HttpStatus.NOT_FOUND);
		return response;
  }
  
  //FIND BUNDLE BY CODE
  @GetMapping("/bundles/{code}")
  ResponseEntity<Bundle> one(@PathVariable Long code) {
	  Bundle bundle = repository.findById(code)
			  					.orElse(null); 
	  ResponseEntity<Bundle> response;
	  if(bundle!=null) 
		  response = new ResponseEntity<Bundle>(bundle, HttpStatus.OK);
	  else
		  response = new ResponseEntity<Bundle>(HttpStatus.NOT_FOUND);
	  return response;
  }
  
  //ADD NEW BUNDLE
  @PostMapping(path = "/bundles", consumes = "application/json")
  public String addMember(@RequestBody Bundle bundle) {
	  Bundle tempBundle= repository.findById(bundle.getCode())
			  							.orElse(null);
	  Boolean alreadyExists = tempBundle!=null;
      String message;
      if(bundle.getName().isEmpty())
    	  message="Error!";			  
      else
    	  if(alreadyExists)
    		  message = "Bundle already exists. Please use PUT if you want to update it.";
		  else {
			  message="Bundle was succesfully added.";
			  repository.save(bundle);
		  }
      return message;
  }
  
  //UPDATE A BUNDLE
  @PutMapping(path = "/bundles/{code}", consumes = "application/json", produces = "application/json")
  public String updateMember(@RequestBody Bundle bundle) {
	  Bundle tempBundle= repository.findById(bundle.getCode())
			  							.orElse(null);
	  Boolean alreadyExists = tempBundle!=null;
      String message;
      if(!bundle.getName().isEmpty())
    	  if(alreadyExists) {
    		  message = "Bundle was succesfully updated.";
    		  repository.save(bundle);
    	  }
		  else 
			  message="Bundle doesn't exist.";		  
      else
    	  message="Error!";
      return message;
  }
  
  //DELETE A BUNDLE
  @DeleteMapping(path = "/bundles/{code}")
  public String deleteMember(@PathVariable Long code) {
	  Bundle tempBundle= repository.findById(code)
			  						.orElse(null);
	  Boolean alreadyExists = tempBundle!=null;
      String message;
	  if(alreadyExists) {
		  message = "Bundle was succesfully deleted.";
		  repository.deleteById(code);
	  }
	  else 
		  message="Bundle doesn't exist.";		  

      return message;
  }
  
  //ORDER BUNDLES ASCENDING BY NAME
  @GetMapping(value="/bundles/sortbyname", produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<List<Bundle>> ascendingbyname() {
	  List<Bundle> bundles = new ArrayList<>();
	  for(Bundle bundle : repository.findAll(sortByNameAsc())) {
	        bundles.add(bundle);
	    }
	  ResponseEntity<List<Bundle>> response;
	  if(!bundles.isEmpty())
		  response = new ResponseEntity<List<Bundle>>(bundles, HttpStatus.OK);
	  else
		  response = new ResponseEntity<List<Bundle>>(HttpStatus.NOT_FOUND);
	  
	  return response;
  }
  private Sort sortByNameAsc() {
	 Sort sort = Sort.by(Sort.Order.asc("name"));
	 return sort;
  }
  
  //ORDER BUNDLES ASCENDING BY PRICE
  @GetMapping(value="/bundles/sortbypriceasc", produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<List<Bundle>> ascendingbyprice() {
	  List<Bundle> bundles = new ArrayList<>();
	  for(Bundle bundle : repository.findAll(sortByPriceAsc())) {
	        bundles.add(bundle);
	    }
	  ResponseEntity<List<Bundle>> response;
	  if(!bundles.isEmpty())
		  response = new ResponseEntity<List<Bundle>>(bundles, HttpStatus.OK);
	  else
		  response = new ResponseEntity<List<Bundle>>(HttpStatus.NOT_FOUND);
	  
	  return response;
  }
  private Sort sortByPriceAsc() {
	 Sort sort = Sort.by(Sort.Order.asc("price"));
	 return sort;
  }
  
//ORDER BUNDLES DESCENDING BY PRICE
  @GetMapping(value="/bundles/sortbypricedesc", produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<List<Bundle>> descendingbyprice() {
	  List<Bundle> bundles = new ArrayList<>();
	  for(Bundle bundle : repository.findAll(sortByPriceDesc())) {
	        bundles.add(bundle);
	    }
	  ResponseEntity<List<Bundle>> response;
	  if(!bundles.isEmpty())
		  response = new ResponseEntity<List<Bundle>>(bundles, HttpStatus.OK);
	  else
		  response = new ResponseEntity<List<Bundle>>(HttpStatus.NOT_FOUND);
	  
	  return response;
  }
  private Sort sortByPriceDesc() {
	 Sort sort = Sort.by(Sort.Order.desc("price"));
	 return sort;
  }
}