package com.restapi.BundlesProject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sun.istack.NotNull;

@RestController
class BundlesController {

  private final BundleRepository repository;
  
  @Autowired
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
  public void addMember(@Validated @RequestBody Bundle bundle) throws ResponseStatusException{
	  Bundle tempBundle= repository.findById(bundle.getCode())
			  							.orElse(null);
	  Boolean alreadyExists = tempBundle!=null;

	  if(alreadyExists)
		  throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bundle already exists. Please use PUT if you want to update it.");
	  else {
		  repository.save(bundle);
		  throw new ResponseStatusException(HttpStatus.OK, "Bundle was succesfully added.");  
	  }
	  
  }
  
  //UPDATE A BUNDLE
  @PutMapping(path = "/bundles/{code}", consumes = "application/json")
  public void updateMember(@Validated @RequestBody Bundle bundle, @PathVariable Long code) throws ResponseStatusException{
	  Bundle tempBundle= repository.findById(code)
			  							.orElse(null);
	  Boolean alreadyExists = tempBundle!=null;
	  if(alreadyExists) {
		  repository.save(bundle);
		  throw new ResponseStatusException(HttpStatus.OK, "Bundle was succesfully updated.");
	  }else 
		  throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bundle doesn't exist");	  
	  
  }
  
  //DELETE A BUNDLE
  @DeleteMapping(path = "/bundles/{code}")
  public void deleteMember(@PathVariable Long code) throws ResponseStatusException {
	  Bundle tempBundle= repository.findById(code)
			  						.orElse(null);
	  Boolean alreadyExists = tempBundle!=null;
	  if(alreadyExists) {
		  repository.deleteById(code);
		  throw new ResponseStatusException(HttpStatus.OK, "Bundle was succesfully deleted.");
	  }
	  else 
		  throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bundle doesn't exist");			  

  }
  
  //ORDER BUNDLES
  @GetMapping(value="/bundles/sort", produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<List<Bundle>> ascendingbyname(@Param("by") String by, @Param("dir") String dir) {
	 
	  List<Bundle> bundles = new ArrayList<>();
	  for(Bundle bundle : repository.findAll(sortBy(by,dir))) {
	        bundles.add(bundle);
	    }
	  ResponseEntity<List<Bundle>> response;
	  if(!bundles.isEmpty())
		  response = new ResponseEntity<List<Bundle>>(bundles, HttpStatus.OK);
	  else
		  response = new ResponseEntity<List<Bundle>>(HttpStatus.NOT_FOUND);
	  
	  return response;
  }
  private Sort sortBy(String by, String dir) {
	  Sort sort = null;
	  if(dir.equals("asc")) {
		  sort = Sort.by(Sort.Order.asc(by));	  
	  }else if((dir.equals("desc"))){
		  sort = Sort.by(Sort.Order.desc(by));
	  }
	return sort;	 
  }
  
  
}