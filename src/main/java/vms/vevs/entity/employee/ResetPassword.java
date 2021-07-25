package vms.vevs.entity.employee;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@AllArgsConstructor
@NoArgsConstructor
public class ResetPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String token;

    private String userEmail;

    @JsonIgnore
    private Timestamp tokenCreationTime;

    @JsonIgnore
    private Long userId;

    @Transient
    private String message= StringUtils.EMPTY;

    @Transient
    private String password;
}
