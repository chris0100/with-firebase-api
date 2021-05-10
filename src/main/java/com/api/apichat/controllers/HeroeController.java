package com.api.apichat.controllers;


import com.api.apichat.entities.Heroe;
import com.api.apichat.services.HeroeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "*"})
@Slf4j
public class HeroeController {

    @Autowired
    private HeroeService heroeServiceObj;


    //Crear un Heroe
    @PostMapping("/heroe")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity crearHeroe(@Valid @RequestBody Heroe heroe, BindingResult result){
        Heroe heroeNuevo = null;
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()){
            log.error("Se han encontrado errores");
            // Se crea lista de errores
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo " + err.getField() + " " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }

        try{
            //Se guarda el heroe
            heroeNuevo = heroeServiceObj.save(heroe);
        }
        catch(DataAccessException e){
            log.error("error por excepcion de acceso a base de datos");
            response.put("mensaje", "Error al acceder a la base de datos");
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El heroe ha sido creado con exito");
        response.put("heroe", heroeNuevo);
        return new ResponseEntity(response, HttpStatus.CREATED);
    }


    //Obtener un heroe por el id
    @GetMapping("/heroe/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity obtenerHeroe(@PathVariable("id") Long id){
        Heroe heroe = null;
        Map<String, Object> response = new HashMap<>();

        try{
            heroe = heroeServiceObj.findById(id);
        } catch (DataAccessException de){
            response.put("mensaje", "Error al consultar la base de datos");
            response.put("error", de.getMessage());
            return new ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Si el heroe no aparece en la base de datos
        if (heroe == null){
            response.put("mensaje", "el Heroe ID: " + id.toString() + " no existe en la base de datos");
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(heroe, HttpStatus.OK);
    }


    //obtener todos los heroes
    @GetMapping("/heroes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity obtenerHeroes(){
        List<Heroe> listaHeroes = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();

        try{
            listaHeroes = heroeServiceObj.findAll();
        } catch (DataAccessException de){
            response.put("mensaje", "Error al consultar la base de datos");
            response.put("error", de.getMostSpecificCause().getMessage());
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(listaHeroes, HttpStatus.OK);
    }



    //Borrar el heroe a traves del id
    @DeleteMapping("/heroe/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity eliminarHeroe(@PathVariable("id") Long id){
        log.info("el id a borrar es " + id);
        Heroe heroe = null;
        Map<String, Object> response = new HashMap<>();


        try{
            //Se localiza el heroe
            heroe = heroeServiceObj.findById(id);
            if (heroe == null){
                response.put("mensaje", "El Heroe no se encuentra en la base de datos");
                return new ResponseEntity(response, HttpStatus.NOT_FOUND);
            }

            //Se borra el heroe
            heroeServiceObj.deleteById(id);
        }catch (DataAccessException de){
            response.put("mensaje", "Error al eliminar Heroe");
            response.put("error", de.getMessage());
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje","Heroe eliminado con exito");
        return new ResponseEntity(response,HttpStatus.OK);
    }



    // Editar un heroe
    @PutMapping("/heroe")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity actualizarHeroe(@Valid @RequestBody Heroe heroe, BindingResult result){
        Heroe heroeActual = heroeServiceObj.findById(heroe.getId());

        Heroe heroeUpdated = null;

        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()){
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo " + err.getField() + " " + err.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }

        //Si el heroe que se quiere editar no existe en la BD
        if (heroeActual == null){
            response.put("mensaje", "Error: El Heroe no se encuentra en la BD");
            return new ResponseEntity(response,HttpStatus.NOT_FOUND);
        }

        try{
            heroeActual.setNombre(heroe.getNombre());
            heroeActual.setVivo(heroe.getVivo());
            heroeActual.setPoder(heroe.getPoder());

            heroeUpdated = heroeServiceObj.save(heroeActual);
        }catch (DataAccessException dex){
            response.put("mensaje","Error al actualizar el Heroe en la base de datos");
            response.put("error", dex.getMessage());
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "El Heroe ha sido actualizado con exito");
        response.put("heroe", heroeUpdated);
        return new ResponseEntity(response, HttpStatus.CREATED);
    }


}
