package cc.robart.iot.demoproject.persistent;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain object for a firmware.
 *
 */
@Entity(name="firmware")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Firmware implements Serializable{
	
	private static final long serialVersionUID = 6714150581808220833L;

	@Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator",
        parameters = {
            @Parameter(
                name = "uuid_gen_strategy_class",
                value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
            )
        }
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
	
	@Column(unique=true, nullable=false)
	@NotNull
	private String name;
	
	@Column
	private String data;
	
	@OneToMany(mappedBy="hardwareVersion",cascade = {CascadeType.REMOVE})
    private Set<Robot> robots;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==this)
			return true;
		if (obj instanceof Firmware) {
			Firmware firmware = (Firmware) obj;
				if(firmware.name.equals(this.name) && firmware.data.equals(this.data)) {
					return true;
				}else
					return false;
		}
		
		return false;
	}

	@Override
	public String toString() {
		return "Firmware [name=" + name + ", data=" + data + "]";
	}
	
}
