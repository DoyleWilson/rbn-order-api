package com.rcn.order.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.validation.Valid;

import com.rcn.order.exception.ServiceExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rcn.order.entities.FarmData;
import com.rcn.order.entities.OrderData;
import com.rcn.order.exception.ResourceNotFoundException;
import com.rcn.order.model.RcnOrderResponse;
import com.rcn.order.repositories.FarmDataRepository;
import com.rcn.order.repositories.OrderDataRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class OrderJobController {

    private static final Logger logger = LoggerFactory.getLogger(OrderJobController.class);

    private final OrderDataRepository orderDataRepository;
    private final FarmDataRepository farmDataRepository;

    @Autowired
    private RcnOrderResponse rcnOrderResponse;

    @Value("${check.profile}")
    private String sActiveProfile;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

    @Autowired
    public OrderJobController(OrderDataRepository orderDataRepository, FarmDataRepository farmDataRepository) {
        this.orderDataRepository = orderDataRepository;
        this.farmDataRepository = farmDataRepository;
    }

    /**
     * This method is the controller which tags to the endpoint request from the
     * external system.
     *
     * @return Returns the List of all orders
     **/
    @GetMapping("rcn/order")
    public List<OrderData> getAllOrders() {
        return orderDataRepository.findAll();
    }

    /**
     * This method is the controller which tags to the endpoint request from the
     * external system
     *
     * @param farmId
     * @return Returns the response of the farmId details.
     **/
    @GetMapping("rcn/order/{farmId}")
    public List<OrderData> getJobById(@PathVariable(value = "farmId") String farmId) throws ResourceNotFoundException {
        OrderData orderData = orderDataRepository.findById(farmId)
                .orElseThrow(() -> new ResourceNotFoundException("No Data Found for id :: " + farmId));
        List<OrderData> orderDataArrayList = new ArrayList<>();
        orderDataArrayList.add(orderData);
        return orderDataArrayList;
    }

    @PostMapping("rcn/order")
    public ResponseEntity<RcnOrderResponse> createOrder(@Valid @RequestBody OrderData orderData) throws Exception {

        FarmData farmData = farmDataRepository.findById(orderData.getFarmId())
                .orElseThrow(() -> new ResourceNotFoundException("No Farm Id found :: " + orderData.getFarmId()));
        rcnOrderResponse.setFarmId(orderData.getFarmId());

        if(!valOrderFromOverlap(orderData)){
            rcnOrderResponse.setResponseMsg("Order Creation Failed, Dates overlapping. Try with other date !!!");
            return ResponseEntity.ok().body(rcnOrderResponse);
        }
        orderData.setFarmReqStatus("Requested");
        orderData.setEndDateTime(calculateEndDateByDuration(orderData.getStartOrderDateTime(),orderData.getDurationReqd()));
        orderData.setTimeStampFrm(getUTCTimeStr());
        String sFarmId = orderDataRepository.save(orderData).getFarmId();

        if (sFarmId.isEmpty()) {
            rcnOrderResponse.setResponseMsg("Order Creation Failed !!!");
        } else {
            rcnOrderResponse.setResponseMsg("Order Creation Successful !!!");
        }
        return ResponseEntity.ok().body(rcnOrderResponse);
    }

    private Boolean valOrderFromOverlap(OrderData orderData) throws ParseException {
        logger.info("Inside valOrderFromOverlap function");
        logger.info("===================================");
        String[] statusArr = { "Requested", "InProgress"};
        Date calcEndDateWithNewReq = calculateEndDateByDuration(orderData.getStartOrderDateTime(), orderData.getDurationReqd());

        String pattern = "yyyy-mm-dd HH:mm:ss.SSS";
        SimpleDateFormat formatter=new SimpleDateFormat(pattern);
        String date1 = new SimpleDateFormat(pattern).format(calcEndDateWithNewReq);
        Date date2 = formatter.parse(date1);

        List<OrderData> orderDataList = orderDataRepository.findByFarmId(orderData.getFarmId());
        logger.info("Inside valOrderFromOverlap orderDataList - {}", orderDataList);
        for (OrderData order : orderDataList){
            if(Arrays.asList("Requested", "InProgress").contains(order.getFarmReqStatus())){

                logger.info(" valOrderFromOverlap calcEndDateWithNewDurationReq - {}", date2);
                logger.info(" valOrderFromOverlap order.getStartOrderDateTime() - {}", order.getStartOrderDateTime());
                logger.info(" valOrderFromOverlap order.getEndDateTime() - {}", order.getEndDateTime());
                logger.info(" valOrderFromOverlap New Order - orderData.getStartOrderDateTime() - {}", orderData.getStartOrderDateTime());
                if(date2.after(order.getStartOrderDateTime()) &&
                        date2.before(order.getEndDateTime())){
                    return false;
                }
                if(orderData.getStartOrderDateTime().after(order.getStartOrderDateTime()) &&
                orderData.getStartOrderDateTime().before(order.getEndDateTime())){
                    return false;
                }
            }
        }
        logger.info("===================================");
        return true;
    }

    private Date calculateEndDateByDuration(Date calcData, int duration) {
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(calcData); // sets calendar time/date
        cal.add(Calendar.HOUR, duration);
        return cal.getTime();
    }

    /**
     * This method is called internally to populate the UTC time.
     *
     * @return Returns the UTC time
     **/
    private String getUTCTimeStr() {
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(new Date());
    }

    @PutMapping("rcn/order/{farmId}")
    public ResponseEntity<RcnOrderResponse> updateJob(@PathVariable(value = "farmId") String batchId,
                                                      @Valid @RequestBody OrderData orderData) throws Exception {
        return ResponseEntity.ok().body(rcnOrderResponse);
    }

    @DeleteMapping("dex/job/{farmId}")
    public ResponseEntity deleteJob(@PathVariable(value = "farmId") String farmId) throws ResourceNotFoundException {
        OrderData orderData = orderDataRepository.findById(farmId)
                .orElseThrow(() -> new ResourceNotFoundException("Farm Id not found  :: " + farmId));
        orderDataRepository.delete(orderData);
        rcnOrderResponse.setFarmId(farmId);
        rcnOrderResponse.setResponseMsg("Deletion Successful !!!");
        return ResponseEntity.ok().body(rcnOrderResponse);
    }

    @GetMapping("/")
    public ResponseEntity initMapping() throws Exception {

        rcnOrderResponse.setFarmId("Visit: /admin/status");
        String sRespMsg = "Alive and Running !!!! - Profile set - " + sActiveProfile;
        rcnOrderResponse.setResponseMsg(sRespMsg);
        return ResponseEntity.ok().body(rcnOrderResponse);
    }

}
