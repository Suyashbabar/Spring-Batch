package in.suyash.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    private Long id;

    private String firstName;

    private String lastName;
    private String email;
    private String gender;
    private String contactNo;
    private String country;

    private String dob;

}