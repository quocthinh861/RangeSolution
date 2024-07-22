package io.mesoneer.interview_challenges;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RangeController {
    @PostMapping("/range")
    public  ResponseEntity<Range.Response> checkRange(@RequestBody Range.Query query) {
        try {
            Range<Integer> range = Range.parse(query.range, Integer::parseInt);
            return ResponseEntity.ok(new Range.Response(range.contains(Integer.parseInt(query.value)), null));
        }
        catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(new Range.Response(null, "Invalid value"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new Range.Response(null, e.getMessage()));
        }
    }
}
