package fr.ecom.mstr.tire.web.rest;

import fr.ecom.mstr.tire.domain.Customer;
import fr.ecom.mstr.tire.web.rest.Containers.FilterContainer;
import fr.ecom.mstr.tire.web.rest.errors.BadRequestAlertException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/order")
public class PaymentController {

        @PostMapping
        public ResponseEntity<Void> order(@RequestBody Customer customer) {
            throw new BadRequestAlertException("Hey! ordering? nop deal with it", "PaymentController", "notimplemented");
            //return new ResponseEntity<>("Hey! filterbyprice? nop deal with it", HttpStatus.BAD_REQUEST);
        }

    }
