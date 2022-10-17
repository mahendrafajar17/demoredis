package com.mytechnodev.redisdemo.controllers;

import com.google.gson.Gson;
import com.mytechnodev.redisdemo.models.redis.Data;
import com.mytechnodev.redisdemo.models.redis.Login;
import com.mytechnodev.redisdemo.models.redis.Menu;
import com.mytechnodev.redisdemo.pojos.ResponseDataPOJO;
import com.mytechnodev.redisdemo.repositories.DataRepository;
import com.mytechnodev.redisdemo.repositories.LoginRepository;
import com.mytechnodev.redisdemo.repositories.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@RestController
@RequestMapping("/")
public class Controller {

    @Value("${app.api-url}")
    String apiUrl;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    DataRepository dataRepository;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat simpleDateFormatWithZone = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSS'+07.00'");

    Gson gson = new Gson();

    @PostMapping("/login")
    ResponseEntity<Object> login(@RequestBody Login body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> httpEntity = new HttpEntity<>(body, httpHeaders);

        Optional<Login> loginOptional = loginRepository.findById(body.getUsername());
        if (loginOptional.isPresent()) {
            Login login = loginOptional.get();
            if (login.getPassword().equals(body.getPassword())) {
                return new ResponseEntity<>(login.getResponseData(), httpHeaders, HttpStatus.OK);
            }
        }

        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl + "/login", HttpMethod.POST, httpEntity, String.class);

        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            Login login = new Login();
            login.setUsername(body.getUsername());
            login.setPassword(body.getPassword());
            login.setResponseData(responseEntity.getBody());
            loginRepository.save(login);
        }
        return new ResponseEntity<>(responseEntity.getBody(), httpHeaders, responseEntity.getStatusCode());
    }

    @GetMapping("/menu")
    ResponseEntity<Object> menu(@RequestHeader HttpHeaders httpHeadersRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeadersRequest);

        Optional<Menu> menuOptional = menuRepository.findById(simpleDateFormat.format(new Date()));
        if (menuOptional.isPresent()) {
            return new ResponseEntity<>(menuOptional.get().getResponseData(), httpHeaders, HttpStatus.OK);
        }

        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl + "/menu", HttpMethod.GET, httpEntity, String.class);

        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            Menu menu = new Menu();
            menu.setId(simpleDateFormat.format(new Date()));
            menu.setResponseData(responseEntity.getBody());
            menuRepository.save(menu);
        }
        return new ResponseEntity<>(responseEntity.getBody(), httpHeaders, responseEntity.getStatusCode());
    }

    @GetMapping("/data")
    ResponseEntity<Object> data(@RequestParam(value = "page", required = false) Integer page, @RequestHeader HttpHeaders httpHeadersRequest) {
        int index = 0;
        int size = 9;
        if (page != null) {
            size = (page * 10) - 1;
            index = (page * 10) - 10;
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeadersRequest);

        List<Data> dataList = dataRepository.findAll();
        if (dataList.size() > 0) {
            ResponseDataPOJO responseDataPOJO = new ResponseDataPOJO();
            responseDataPOJO.setStatusCode("00");
            responseDataPOJO.setMessage("Success");
            responseDataPOJO.setResponseDate(simpleDateFormatWithZone.format(new Date()));
            for (int i = index; i <= size; i++) {
                ResponseDataPOJO.Data dataPOJO = new ResponseDataPOJO.Data();
                dataPOJO.setId(dataList.get(i).getId());
                dataPOJO.setName(dataList.get(i).getName());
                dataPOJO.setStatus(dataList.get(i).getStatus());
                dataPOJO.setCreatedAt(dataList.get(i).getCreatedAt());
                responseDataPOJO.getResult().add(dataPOJO);
            }
            return new ResponseEntity<>(gson.toJson(responseDataPOJO), httpHeaders, HttpStatus.OK);
        }

        ResponseEntity<ResponseDataPOJO> responseEntity = restTemplate.exchange(apiUrl + "/data", HttpMethod.GET, httpEntity, ResponseDataPOJO.class);

        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            ResponseDataPOJO responsePOJO = responseEntity.getBody();
            if (responsePOJO != null && responsePOJO.getResult() != null) {
                for (ResponseDataPOJO.Data dataPOJO : responsePOJO.getResult()) {
                    Data data = new Data();
                    data.setId(dataPOJO.getId());
                    data.setName(dataPOJO.getName());
                    data.setStatus(dataPOJO.getStatus());
                    data.setCreatedAt(dataPOJO.getCreatedAt());
                    dataRepository.save(data);
                }
            }
        }
        return new ResponseEntity<>(new Gson().toJson(responseEntity.getBody()), httpHeaders, responseEntity.getStatusCode());
    }
}
