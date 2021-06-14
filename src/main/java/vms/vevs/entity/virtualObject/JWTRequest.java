package vms.vevs.entity.virtualObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JWTRequest  implements Serializable {

    private String username;
    private String password;

}
