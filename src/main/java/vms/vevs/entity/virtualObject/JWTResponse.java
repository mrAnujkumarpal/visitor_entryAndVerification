package vms.vevs.entity.virtualObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JWTResponse implements Serializable {

    private  String jwtToken;
}
