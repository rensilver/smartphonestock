package com.rensilver.smartphone_api.controller;

import com.rensilver.smartphone_api.dto.SmartphoneDTO;
import com.rensilver.smartphone_api.exception.SmartphoneAlreadyRegisteredException;
import com.rensilver.smartphone_api.exception.SmartphoneNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Api("Manages smartphones stock")
public interface SmartphoneControllerDocs {

    @ApiOperation(value = "Returns a list of all smartphones registered in the system")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all smartphones registered in the system"),
    })
    List<SmartphoneDTO> findAll() throws SmartphoneNotFoundException;

    @ApiOperation(value = "Returns smartphone found by a given id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success smartphone found in the system"),
            @ApiResponse(code = 404, message = "Smartphone with given id not found.")
    })
    SmartphoneDTO findById() throws SmartphoneNotFoundException;

    @ApiOperation(value = "Returns smartphone found by a given name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success smartphone found in the system"),
            @ApiResponse(code = 404, message = "Smartphone with given name not found.")
    })
    SmartphoneDTO findByName() throws SmartphoneNotFoundException;

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success smartphone creation"),
            @ApiResponse(code = 400, message = "Missing required fields or wrong field range value.")
    })
    SmartphoneDTO createSmartphone(SmartphoneDTO smartphoneDTO) throws SmartphoneAlreadyRegisteredException;

    @ApiOperation(value = "Delete a smartphone found by a given valid Id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Success smartphone deleted in the system"),
            @ApiResponse(code = 404, message = "Smartphone with given id not found.")
    })
    void deleteById(@PathVariable Long id) throws SmartphoneNotFoundException;
}
